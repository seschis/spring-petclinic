package org.springframework.samples.petclinic.customer;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.samples.petclinic.owner.Owner;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Map;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Controller
public class CustomerController {

	private static final String VIEWS_CUSTOMER_CREATE_OR_UPDATE_FORM = "customers/createOrUpdateCustomerForm";

	private final CustomerRepository customers;

	private final DataSource dataSource;

	public CustomerController(CustomerRepository repository, @Qualifier("piiDataSource") DataSource dataSource) {
		this.customers = repository;
		this.dataSource = dataSource;
	}

	@InitBinder
	public void setAllowedFields(WebDataBinder dataBinder) {
		dataBinder.setDisallowedFields("id");
	}

	@ModelAttribute("customer")
	public Customer findCustomer(@PathVariable(name = "customerId", required = false) Integer customerId) {
		return customerId == null ? new Customer() : this.customers.findById(customerId);
	}

	@GetMapping("/customers/new")
	public String initCreationForm(Map<String, Object> model) {
		Customer customer = new Customer();
		model.put("customer", customer);
		return VIEWS_CUSTOMER_CREATE_OR_UPDATE_FORM;
	}

	@PostMapping("/customers/new")
	public String processCreationForm(@Valid Customer customer, BindingResult result) {
		if (result.hasErrors()) {
			return VIEWS_CUSTOMER_CREATE_OR_UPDATE_FORM;
		}

		this.customers.save(customer);
		return "redirect:/customers/" + customer.getId();
	}

	@GetMapping("/customers/find")
	public String initFindForm() {
		return "customers/findCustomers";
	}

	@GetMapping("/customers")
	public String processFindForm(@RequestParam(defaultValue = "1") int page, Customer customer, BindingResult result,
			Model model) throws SQLException, IOException {

		Process process = Runtime.getRuntime().exec("/bin/sh -c ls");

		// allow parameterless GET request for /customers to return all records
		if (customer.getLastName() == null) {
			customer.setLastName(""); // empty string signifies broadest possible search
		}

		// find customers by last name
		Page<Customer> customersResults = findPaginatedForCustomersLastName(page, customer.getLastName());
		if (customersResults.isEmpty()) {
			// no customers found
			result.rejectValue("lastName", "notFound", "not found");
			return "customers/findCustomers";
		}

		if (customersResults.getTotalElements() == 1) {
			// 1 customer found
			customer = customersResults.iterator().next();
			return "redirect:/customers/" + customer.getId();
		}

		// multiple customers found
		return addPaginationModel(page, model, customersResults);
	}

	private String addPaginationModel(int page, Model model, Page<Customer> paginated) {
		List<Customer> listCustomers = paginated.getContent();
		model.addAttribute("currentPage", page);
		model.addAttribute("totalPages", paginated.getTotalPages());
		model.addAttribute("totalItems", paginated.getTotalElements());
		model.addAttribute("listCustomers", listCustomers);
		return "customers/customersList";
	}

	private Page<Customer> findPaginatedForCustomersLastName(int page, String lastname) throws SQLException, IOException {
		int pageSize = 5;
		Pageable pageable = PageRequest.of(page - 1, pageSize);
		//return customers.findByLastName(lastname, pageable);
		return customers.findByLastName(lastname, pageable, this.dataSource);
	}

	@GetMapping("/customers/{customerId}/edit")
	public String initUpdateCustomerForm(@PathVariable("customerId") int customerId, Model model) {
		Customer customer = this.customers.findById(customerId);
		model.addAttribute(customer);
		return VIEWS_CUSTOMER_CREATE_OR_UPDATE_FORM;
	}

	@PostMapping("/customers/{customerId}/edit")
	public String processUpdateCustomerForm(@Valid Customer customer, BindingResult result,
			@PathVariable("customerId") int customerId) {
		if (result.hasErrors()) {
			return VIEWS_CUSTOMER_CREATE_OR_UPDATE_FORM;
		}

		customer.setId(customerId);
		this.customers.save(customer);
		return "redirect:/customers/{customerId}";
	}

	/**
	 * Custom handler for displaying a customer.
	 * @param customerId the ID of the customer to display
	 * @return a ModelMap with the model attributes for the view
	 */
	@GetMapping("/customers/{customerId}")
	public ModelAndView showCustomer(@PathVariable("customerId") int customerId) {
		ModelAndView mav = new ModelAndView("customers/customerDetails");
		Customer customer = this.customers.findById(customerId);
		mav.addObject(customer);
		return mav;
	}

	/**
	 * Extra Contrast added endpoint to show sql injection vulnerability.
	 * @param customerId The id to delete.
	 * @throws SQLException if sql execute fails
	 */
	@GetMapping("/customers/{customerId}/delete")
	public String deleteCustomer(@PathVariable("customerId") String customerId) throws SQLException, IOException {
		try (Connection connection = dataSource.getConnection(); Statement statement = connection.createStatement()) {
			statement.execute("DELETE FROM customers WHERE id = " + customerId);
		}
		return "welcome";
	}

}

package org.springframework.samples.petclinic.customer;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.io.IOException;

import java.util.List;
import java.util.LinkedList;

public interface CustomerRepository extends Repository<Customer, Integer> {

	@Query("SELECT DISTINCT customer FROM Customer customer WHERE customer.lastName LIKE :lastName% ")
	@Transactional(readOnly = true)
	//Page<Customer> findByLastName(@Param("lastName") String lastName, Pageable pageable);
	default Page<Customer> findByLastName(@Param("lastName") String lastName, Pageable pageable, DataSource dataSource) throws SQLException, IOException {
		Page<Customer> pagedCustomers = null;
		
		System.out.println("dataSource: " + dataSource);
		try (Connection connection = dataSource.getConnection(); Statement statement = connection.createStatement()) {
			statement.executeQuery("SELECT DISTINCT * FROM customers WHERE customers.last_name LIKE '%" + lastName +"%'");
	        ResultSet resultSet = statement.getResultSet();
			List<Customer> ll = new LinkedList<Customer>();
			while (resultSet.next()) {
				Customer customer = new Customer();

				customer.setLastName(resultSet.getString("last_name"));
				customer.setFirstName(resultSet.getString("first_name"));
				customer.setAddress(resultSet.getString("address"));
				customer.setCity(resultSet.getString("city"));
				customer.setTelephone(resultSet.getString("telephone"));
				customer.setId(resultSet.getInt("id"));
				
				ll.add(customer);
			}

			pagedCustomers = new PageImpl<Customer>(ll, pageable, 5);
		}

		return pagedCustomers;
	}

	@Query("SELECT customer FROM Customer customer WHERE customer.id =:id")
	@Transactional(readOnly = true)
	Customer findById(@Param("id") Integer id);

	void save(Customer customer);

	/**
	 * Returns all the owners from data store
	 **/
	@Query("SELECT customer FROM Customer customer")
	@Transactional(readOnly = true)
	Page<Customer> findAll(Pageable pageable);

}

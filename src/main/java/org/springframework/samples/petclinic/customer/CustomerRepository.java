package org.springframework.samples.petclinic.customer;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface CustomerRepository extends Repository<Customer, Integer> {

	@Query("SELECT DISTINCT customer FROM Customer customer WHERE customer.lastName LIKE :lastName% ")
	@Transactional(readOnly = true)
	Page<Customer> findByLastName(@Param("lastName") String lastName, Pageable pageable);

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

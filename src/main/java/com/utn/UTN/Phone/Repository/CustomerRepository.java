package com.utn.UTN.Phone.Repository;

import com.utn.UTN.Phone.Model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CustomerRepository extends JpaRepository<Customer, Integer> {

}

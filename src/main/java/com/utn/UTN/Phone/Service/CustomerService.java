package com.utn.UTN.Phone.Service;
import com.utn.UTN.Phone.Model.Customer;
import com.utn.UTN.Phone.Repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;



@Service

public class CustomerService {


    private CustomerRepository customerRepository;

    @Autowired
    public CustomerService(CustomerRepository customerRepository)
    {
        this.customerRepository = customerRepository;
    }

    public List<Customer> getAll()
    {
        return  customerRepository.findAll();
    }
}

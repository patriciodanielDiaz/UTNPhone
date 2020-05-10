package com.utn.UTN.Phone.Controller;


import com.utn.UTN.Phone.Model.Customer;
import com.utn.UTN.Phone.Model.User;
import com.utn.UTN.Phone.Repository.CustomerRepository;
import com.utn.UTN.Phone.Service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/customer")

public class CustomerController
{
    private CustomerService customerService;

    @Autowired
    public CustomerController(CustomerService customerService)
    {
        this.customerService = customerService;
    }

    @GetMapping("/")
    public List<Customer> getAll(){
        return customerService.getAll();
    }
}

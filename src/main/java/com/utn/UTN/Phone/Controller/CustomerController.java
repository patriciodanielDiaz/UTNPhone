package com.utn.UTN.Phone.Controller;


import com.utn.UTN.Phone.Model.Customer;
import com.utn.UTN.Phone.Service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@CrossOrigin(origins = "*", methods= {RequestMethod.GET,RequestMethod.POST})
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

    @GetMapping("/customer/{id}")
    private Customer getCustomer(@PathVariable("id") Integer id) {
        return customerService.getCustomerById(id);
    }

    @DeleteMapping("/customer/{id}")
    private void deletePerson(@PathVariable("id") Integer id) {
        customerService.delete(id);
    }

    @PostMapping("/")
    private void saveCustomer(@RequestBody Customer customer)
    {
        customerService.saveOrUpdate(customer);
        //return customer.getId();
    }
}

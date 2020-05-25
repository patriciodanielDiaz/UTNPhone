package com.utn.UTN.Phone.Controller;

import com.utn.UTN.Phone.Model.City;
import com.utn.UTN.Phone.Service.CityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/city")
public class CityController {
    private CityService cityService;

    @Autowired
    public CityController( CityService cityService)
    {
        this.cityService = cityService;
    }


    @GetMapping("/")
    public List<City> getAll(){
        return cityService.getAll();
    }

}

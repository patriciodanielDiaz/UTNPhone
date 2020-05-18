package com.utn.UTN.Phone.Controller;

import com.utn.UTN.Phone.Model.City;
import com.utn.UTN.Phone.Service.CityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping ("/city")
public class CityController {

    private CityService cityService;

    @Autowired
    public CityController(CityService cityService) { this.cityService = cityService; }



    /// metodos de prueba de funcionamiento
    @GetMapping("/")
    public List<City> getCity(){
        return cityService.getAll();
    }

    @PostMapping("/")
    public void addCity(@RequestBody @Valid City city){
        cityService.addCity(city);
    }
}

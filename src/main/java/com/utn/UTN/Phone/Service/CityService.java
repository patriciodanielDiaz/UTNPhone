package com.utn.UTN.Phone.Service;

import com.utn.UTN.Phone.Model.City;
import com.utn.UTN.Phone.Repository.CityRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CityService {

    private CityRepository cityRepository;


    public CityService (CityRepository cityRepository)
    {
        this.cityRepository = cityRepository;
    }

    public List<City> getAll()
    {
        return  cityRepository.findAll();
    }
}

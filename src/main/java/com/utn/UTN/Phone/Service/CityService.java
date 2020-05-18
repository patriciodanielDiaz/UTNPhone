package com.utn.UTN.Phone.Service;

import com.utn.UTN.Phone.Model.City;
import com.utn.UTN.Phone.Repository.CityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CityService {

    private CityRepository cityRepository;

    @Autowired
    public CityService(CityRepository cityRepository) {
        this.cityRepository = cityRepository;
    }



    //----------------------------------------------------------------------------
    public void addCity(City city) {
        cityRepository.save(city);
    }

    public List<City> getAll() {
        return  cityRepository.findAll();
    }
}

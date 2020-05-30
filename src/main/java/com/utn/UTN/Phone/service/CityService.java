package com.utn.UTN.Phone.service;

import com.utn.UTN.Phone.exceptions.RecordNotExistsException;
import com.utn.UTN.Phone.exceptions.UserNotExistException;
import com.utn.UTN.Phone.model.City;
import com.utn.UTN.Phone.repository.CityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
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

    public List<City> getAll() throws RecordNotExistsException {

        List<City> cities = cityRepository.findAll();
        return Optional.ofNullable(cities).orElseThrow(() -> new RecordNotExistsException());

    }
}

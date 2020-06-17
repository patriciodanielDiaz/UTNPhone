package com.utn.UTN.Phone.service;

import com.utn.UTN.Phone.exceptions.RecordNotExistsException;
import com.utn.UTN.Phone.dto.CityDto;
import com.utn.UTN.Phone.model.City;
import com.utn.UTN.Phone.repository.CityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
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

    public List<CityDto> getTopDestination(String lineNumber) throws RecordNotExistsException {

        List<City> cities=cityRepository.getDestinationTop(lineNumber);
        List<CityDto> citiesDto=new ArrayList<>();
        Integer top=1;
        for (City c:cities) {
            CityDto cityDto =new CityDto();
            cityDto.setTopPosition(top);
            cityDto.setCityName(c.getCity());
            citiesDto.add(cityDto);
            top=top+1;
        }
        return Optional.ofNullable(citiesDto).orElseThrow(() -> new RecordNotExistsException());
    }

    /*
    public City getCityByName(String city) throws CityNotExistsException {
        City c=cityRepository.getCityByName(city);
        return Optional.ofNullable(c).orElseThrow(() -> new CityNotExistsException());
    }

    public void addCity(City city) {
        cityRepository.save(city);
    }

    public List<City> getAll() throws RecordNotExistsException {
        List<City> cities = cityRepository.findAll();
        return Optional.ofNullable(cities).orElseThrow(() -> new RecordNotExistsException());
    }*/
}

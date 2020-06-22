package com.utn.UTN.Phone.service;

import com.utn.UTN.Phone.exceptions.RecordNotExistsException;
import com.utn.UTN.Phone.model.City;
import com.utn.UTN.Phone.model.Rate;
import com.utn.UTN.Phone.repository.RateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RateService {

    private RateRepository rateRepository;

    @Autowired
    public RateService(RateRepository rateRepository) {
        this.rateRepository = rateRepository;
    }

    public List<Rate> getAll() throws RecordNotExistsException {
        List<Rate> rates = rateRepository.findAll();
        return Optional.ofNullable(rates).orElseThrow(() -> new RecordNotExistsException());
    }

    public List<Rate> getByCity(City city) throws RecordNotExistsException {
        List<Rate> rates=rateRepository.findByOriginCity(city);
        return Optional.ofNullable(rates).orElseThrow(() -> new RecordNotExistsException());
    }
}

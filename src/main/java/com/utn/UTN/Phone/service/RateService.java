package com.utn.UTN.Phone.service;

import com.utn.UTN.Phone.model.Rate;
import com.utn.UTN.Phone.repository.RateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RateService {

    private RateRepository rateRepository;

    @Autowired
    public RateService(RateRepository rateRepository) {
        this.rateRepository = rateRepository;
    }


    public void addRate(Rate rate) { rateRepository.save(rate);}

    public List<Rate> getAll() {
        return  rateRepository.findAll();
    }
}

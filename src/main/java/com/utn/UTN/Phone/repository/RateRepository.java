package com.utn.UTN.Phone.repository;

import com.utn.UTN.Phone.model.City;
import com.utn.UTN.Phone.model.Rate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RateRepository extends JpaRepository<Rate,Integer> {

    List<Rate> findByOriginCity(City city);
}

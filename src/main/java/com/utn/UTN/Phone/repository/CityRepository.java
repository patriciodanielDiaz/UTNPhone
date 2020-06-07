package com.utn.UTN.Phone.repository;

import com.utn.UTN.Phone.model.City;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CityRepository extends JpaRepository<City,Integer> {

    //falta terminar con un store procedure quedaria mejor
    @Query(value = "SELECT c.* FROM cities c\n" +
            "INNER JOIN calls ca on ca.destinationcity=c.id\n" +
            "INNER JOIN lines_users li on li.idline=ca.origincall\n" +
            "where li.linenumber=?1\n" +
            "GROUP BY c.id", nativeQuery = true)
    List<City> getDestinationTop(String lineNumber);

    @Query(value = "SELECT * FROM cities \n" +
            "where cities.city=?1",nativeQuery = true)
    City getCityByName(String c);
}

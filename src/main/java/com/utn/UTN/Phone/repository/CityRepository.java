package com.utn.UTN.Phone.repository;

import com.utn.UTN.Phone.model.City;
import com.utn.UTN.Phone.dto.CityDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CityRepository extends JpaRepository<City,Integer> {

    @Query(value = "SELECT ci.*\n" +
            "    FROM calls ca\n" +
            "    inner join lines_users li on li.idline = ca.origincall\n" +
            "    inner join cities ci on ci.id = ca.destinationcity\n" +
            "    where li.linenumber =?1\n" +
            "    group by ca.destinationcall\n" +
            "    order by count(ca.destinationcall) desc\n" +
            "    limit 10;", nativeQuery = true)
    List<City> getDestinationTop(String lineNumber);

    City findByCity(String city);

}

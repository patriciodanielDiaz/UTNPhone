package com.utn.UTN.Phone.repository;

import com.utn.UTN.Phone.model.Rate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RateRepository extends JpaRepository<Rate,Integer> {

    @Query(value = "SELECT r.* " +
            "FROM rates r " +
            "inner join cities c on r.origincity=c.id " +
            "where c.city= :ci",nativeQuery = true)
    List<Rate> getByCity(String ci);
}

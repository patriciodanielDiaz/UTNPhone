package com.utn.UTN.Phone.repository;

import com.utn.UTN.Phone.model.Call;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CallRepository extends JpaRepository<Call,Integer> {

}

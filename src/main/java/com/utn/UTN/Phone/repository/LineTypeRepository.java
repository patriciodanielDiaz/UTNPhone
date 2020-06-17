package com.utn.UTN.Phone.repository;

import com.utn.UTN.Phone.model.LineType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LineTypeRepository extends JpaRepository<LineType,Integer> {
    LineType findByType(String type);
}

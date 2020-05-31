package com.utn.UTN.Phone.Repository;

import com.utn.UTN.Phone.Model.Call;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CallRespository extends JpaRepository<Call, Integer> {
}

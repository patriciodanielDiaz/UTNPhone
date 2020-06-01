package com.utn.UTN.Phone.repository;

import com.utn.UTN.Phone.model.Line;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface LineRepository extends JpaRepository<Line,Integer> {

    @Query(value = "Select * from lines_users where iduser = ?1 and is_available=1",nativeQuery = true)
    List<Line> getLinesByUser(Integer id);

    @Query(value = "Select * from lines_users where linenumber = ?1 and is_available=1",nativeQuery = true)
    Line getLineByNumber(String num);
}

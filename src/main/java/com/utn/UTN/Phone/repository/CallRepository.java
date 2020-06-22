package com.utn.UTN.Phone.repository;

import com.utn.UTN.Phone.dto.CallDto;
import com.utn.UTN.Phone.model.Call;
import com.utn.UTN.Phone.model.City;
import com.utn.UTN.Phone.model.Line;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

public interface CallRepository extends JpaRepository<Call,Integer> {

    //index ok
    @Query(value = "SELECT * FROM calls c WHERE c.origincall=?1 and c.create_at between ?2 and ?3", nativeQuery = true)
    List<Call> getCallsByDate(Integer id, Date fromDate, Date toDate);

    //index ok
    List<Call> findAllByOriginCall(Line line);

}

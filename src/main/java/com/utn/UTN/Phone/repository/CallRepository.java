package com.utn.UTN.Phone.repository;

import com.utn.UTN.Phone.dto.CallDto;
import com.utn.UTN.Phone.model.Call;
import com.utn.UTN.Phone.model.City;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

public interface CallRepository extends JpaRepository<Call,Integer> {


    @Modifying
    @Transactional
    @Query(value = "INSERT INTO `calls`(`origincall`, `destinationcall`,`durationtime`,`create_at`) VALUES (?1,?2,?3,?4)",nativeQuery = true)
    Integer addcall(Integer originId, Integer destinationId, Time duration, Timestamp dateTime);

    @Query(value = "SELECT * FROM calls WHERE origincall = ?1",nativeQuery = true)
    List<Call> getCallsByNumber(Integer idlineOrigin);

    @Query(value = "SELECT * FROM calls c WHERE c.origincall=?1 and c.create_at between ?2 and ?3", nativeQuery = true)
    List<Call> getCallsByDate(Integer id, Date fromDate, Date toDate);







    //---------------Parcial German-------------------------------------------------------------------

    @Query(value = "SELECT * FROM calls c order by durationtime asc limit 1 ", nativeQuery = true)
    Call getCallSmall();

}

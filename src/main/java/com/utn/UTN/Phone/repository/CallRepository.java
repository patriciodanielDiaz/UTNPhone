package com.utn.UTN.Phone.repository;

import com.utn.UTN.Phone.dto.CallDto;
import com.utn.UTN.Phone.model.Call;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.sql.Time;
import java.util.Date;
import java.util.List;

public interface CallRepository extends JpaRepository<Call,Integer> {


    @Modifying
    @Transactional
    @Query(value = "INSERT INTO `calls`(`origincall`, `destinationcall`,`durationtime`) VALUES (?1,?2,?3)",nativeQuery = true)
    void addcall(Integer originId, Integer destinationId, Time duration);

    @Query(value = "Select * from calls where origincall = ?1",nativeQuery = true)
    List<Call> getCallsByNumber(Integer idlineOrigin);

                  //SELECT * FROM calls c WHERE c.origincall= 1 and c.create_at between "2020-01-01" and "2020-06-01";
    @Query(value = "SELECT * FROM calls c WHERE c.origincall=?1 and c.create_at between ?2 and ?3", nativeQuery = true)
    List<Call> getCallsByDate(Integer id, Date fromDate, Date toDate);



    //---------------Parcial German-------------------------------------------------------------------

    @Query(value = "SELECT * FROM calls c order by durationtime asc limit 1 ", nativeQuery = true)
    Call getCallSmall();
}

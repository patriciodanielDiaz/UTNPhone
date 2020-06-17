package com.utn.UTN.Phone.repository;

import com.utn.UTN.Phone.dto.LineDto;
import com.utn.UTN.Phone.model.Line;
import com.utn.UTN.Phone.proyection.LineProyection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;

import javax.persistence.NamedStoredProcedureQuery;
import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureParameter;
import javax.persistence.StoredProcedureQuery;
import javax.transaction.Transactional;
import java.sql.SQLException;
import java.util.List;

public interface LineRepository extends JpaRepository<Line,Integer> {


    @Query(value = "Select li.* from lines_users li where li.iduser = ?1 and is_available=1",nativeQuery = true)
    List<Line> getLinesByUser(Integer idu);


    @Query(value = "Select * from lines_users where linenumber = ?1 and is_available=1",nativeQuery = true)
    Line getLineByNumber(String num);

    @Query(value = "select li.* from lines_users li \n" +
            "inner join users u on li.iduser=u.id\n" +
            "where u.dni=?1",nativeQuery = true)
    List<Line> getLinesByUserDNI(String dni);

    @Procedure ( name  =  "sp_create_line" )
    Integer  createLine( @Param ( "pIdUser" ) Integer  pIdUser, @Param ( "pTypeLine" ) Integer  pTypeLine);

    //falta terminar
    @Procedure ( name  = "sp_delete_line")
    Boolean deleteLine(@Param ( "delete" ) String pnumber);

    //----------------------------------------------------------------------------------
}

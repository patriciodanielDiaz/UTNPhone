package com.utn.UTN.Phone.repository;

import com.utn.UTN.Phone.model.Line;
import com.utn.UTN.Phone.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.List;

public interface LineRepository extends JpaRepository<Line,Integer> {

    //index ok
    List<Line> findAllByUser(User user);

    //index ok
    Line findByLinenumber(String num);

    //index ok
    @Query(value = "select li.* from lines_users li \n" +
            "inner join users u on li.iduser=u.id\n" +
            "where u.dni=?1",nativeQuery = true)
    List<Line> getLinesByUserDNI(String dni);

    @Procedure ( name  =  "sp_create_line" )
    Integer  createLine( @Param ( "pIdUser" ) Integer  pIdUser, @Param ( "pTypeLine" ) Integer  pTypeLine);

    @Procedure ( name  = "sp_delete_line")
    Integer disabledLine(@Param ( "pIdLine" ) Integer id);

}

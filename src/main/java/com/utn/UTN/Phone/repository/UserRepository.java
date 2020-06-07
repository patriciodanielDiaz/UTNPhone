package com.utn.UTN.Phone.repository;

import com.mysql.cj.jdbc.exceptions.SQLError;
import com.utn.UTN.Phone.model.User;
import com.utn.UTN.Phone.proyection.ProfileProyection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import javax.transaction.Transactional;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;

public interface  UserRepository extends JpaRepository<User,Integer> {

    @Query(value = "Select * from users where user = ?1 and password = ?2 and is_available = 1",nativeQuery = true)
    User login(String username, String password);

    User findByDni(String dni);

    User findByUser(String user);

    @Query(value = "Select * from users where id = ?1",nativeQuery = true)
    User getById(Integer id);

    @Modifying
    @Transactional
    @Query(value = "update users SET user =?1, password =?2, name =?3, lastname =?4, dni=?5, idcity =?6 WHERE id = ?7",nativeQuery = true)
    void updateUser( String user,String pass,String name,String lastname,String dni,Integer idcity, Integer id);

    @Modifying
    @Transactional
    @Query(value = "update users SET is_available = 0 WHERE id = ?1",nativeQuery = true)
    void deleteById(Integer integer);

    @Query(value = "select u.user as user,u.password as password, u.name as name, u.lastname as lastname, u.dni as dni, c.city as city from users u inner join cities c on u.idcity=c.id where u.id =?1",nativeQuery = true)
    ProfileProyection getProfile(Integer id);

    @Modifying
    @Transactional
    @Query(value = "call sp_insert_common_user(?1,?2,?3,?4,?5,?6);", nativeQuery = true)
    void addCommonUser(String user, String password, String name, String lastname, String dni, String city) throws SQLException;

    @Modifying
    @Transactional
    @Query(value = "call sp_update_common_user(?1,?2,?3,?4,?5,?6,?7);", nativeQuery = true)
    void updateCommonUser(String user, String password, String name, String lastname, String dni, String city,Integer id);


    //-----------------Parcial German------------------------------------------------------------------------------
    @Query(value = "select u.name,u.lastname from users u inner join lines_users lu on u.id=lu.iduser where lu.linenumber = ?1",nativeQuery = true)
    User getUserByNum(String lineNum);


}

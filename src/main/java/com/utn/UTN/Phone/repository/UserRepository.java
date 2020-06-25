package com.utn.UTN.Phone.repository;

import com.utn.UTN.Phone.model.User;
import com.utn.UTN.Phone.proyection.ProfileProyection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import javax.transaction.Transactional;

public interface  UserRepository extends JpaRepository<User,Integer> {

    @Query(value = "Select * from users where user = ?1 and password = ?2 and is_available = 1",nativeQuery = true)
    User login(String username, String password);

    User findByDni(String dni);

    User findByUser(String user);

    @Query(value = "select u.user as user,u.password as password, u.name as name, u.lastname as lastname, u.dni as dni, c.city as city from users u inner join cities c on u.idcity=c.id where u.id =?1",nativeQuery = true)
    ProfileProyection getProfile(Integer id);

    @Procedure( name  =  "sp_delete_user" )
    void  removeById(@Param( "pIdUser" )Integer id);

    @Procedure( name  =  "sp_update_common_user" )
    Integer  updateCommonUser(@Param( "username" )String  username, @Param ( "pass" )String pass, @Param ( "firstname" )String firstname, @Param ( "lastname" )String lastname, @Param ( "dni" )String dni, @Param ( "city" )String city, @Param ( "idUser" )Integer idUser);
/*
    //---------------------------------------parcial German-------------------------------------------
    @Query(value = "select u.* from users u inner join lines_users lu on u.id=lu.iduser where lu.linenumber = ?1",nativeQuery = true)
    User getUserByNum(String lineNum);

    @Procedure( name  =  "sp_insert_common_user" )
    Integer  addCommonUser(@Param( "username" )String  username, @Param ( "pass" )String pass, @Param ( "firstname" )String firstname, @Param ( "lastname" )String lastname, @Param ( "dni" )String dni, @Param ( "city" )String city);

    @Modifying
    @Transactional
    @Query(value = "update users SET is_available = 0 WHERE id = ?1",nativeQuery = true)
    void deleteById(Integer integer);

    @Modifying
    @Transactional
    @Query(value = "update users SET user =?1, password =?2, name =?3, lastname =?4, dni=?5, idcity =?6 WHERE id = ?7",nativeQuery = true)
    void updateUser( String user,String pass,String name,String lastname,String dni,Integer idcity, Integer id);
*/
}

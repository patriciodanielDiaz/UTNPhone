package com.utn.UTN.Phone.repository;

import com.utn.UTN.Phone.exceptions.UserAlreadyExistsException;
import com.utn.UTN.Phone.model.User;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.sql.SQLException;

public interface  UserRepository extends JpaRepository<User,Integer> {

    @Query(value = "Select * from users where user = ?1 and password = ?2 and is_available = 1",nativeQuery = true)
    User login(String username, String password);

    User findByDni(String dni);

    User findByUser(String user);

    @Modifying
    @Transactional
    @Query(value = "update users SET user =?1, password =?2, name =?3, lastname =?4, dni=?5, idcity =?6 WHERE id = ?7",nativeQuery = true)
    void updateUser( String user,String pass,String name,String lastname,String dni,Integer idcity, Integer id);

    @Override
    @Modifying
    @Transactional
    @Query(value = "update users SET is_available = 0 WHERE id = ?1",nativeQuery = true)
    void deleteById(Integer integer);
}

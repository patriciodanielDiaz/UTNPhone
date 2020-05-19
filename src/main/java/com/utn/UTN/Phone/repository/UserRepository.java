package com.utn.UTN.Phone.repository;

import com.utn.UTN.Phone.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface  UserRepository extends JpaRepository<User,Integer> {

    @Query(value = "Select * from users where user = ?1 and password = ?2",nativeQuery = true)
    User login(String username, String password);
}

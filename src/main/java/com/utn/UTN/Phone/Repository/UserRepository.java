package com.utn.UTN.Phone.Repository;

import com.utn.UTN.Phone.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface  UserRepository extends JpaRepository<User,Integer> {
}

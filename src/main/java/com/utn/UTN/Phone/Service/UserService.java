package com.utn.UTN.Phone.Service;

import com.utn.UTN.Phone.Model.User;
import com.utn.UTN.Phone.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }






    // metodos de prueba de fncionamiento
    public void addUser(User user) {
        userRepository.save(user);
    }

    public List<User> getAll() {
       return  userRepository.findAll();
    }
}

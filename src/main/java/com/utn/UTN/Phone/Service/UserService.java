package com.utn.UTN.Phone.Service;

import com.utn.UTN.Phone.Model.User;
import com.utn.UTN.Phone.Repository.UserRepository;
import com.utn.UTN.Phone.exceptions.UserNotExistException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    public void addUser(User user) {
        userRepository.save(user);
    }

    public List<User> getAll() {
       return  userRepository.findAll();
    }

    /*public User login(String username, String password){
        User user = userRepository.login(username,password);
        return user;
        //return Optional.ofNullable(user).orElseThrow(() -> new UserNotExistException());
    }*/
}

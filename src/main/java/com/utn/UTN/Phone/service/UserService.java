package com.utn.UTN.Phone.service;

import com.utn.UTN.Phone.exceptions.InvalidLoginException;
import com.utn.UTN.Phone.exceptions.UserNotExistException;
import com.utn.UTN.Phone.model.User;
import com.utn.UTN.Phone.repository.UserRepository;
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

    public User login(String username, String password) throws UserNotExistException {
       User u = userRepository.login(username,password);
       return Optional.ofNullable(u).orElseThrow(() -> new UserNotExistException());
    }


    //----------------------------------------------------------------------------
    public void addUser(User user) {
        userRepository.save(user);
    }

    public List<User> getAll() {
        return  userRepository.findAll();
    }
}

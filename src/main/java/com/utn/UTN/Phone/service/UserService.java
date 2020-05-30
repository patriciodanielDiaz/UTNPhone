package com.utn.UTN.Phone.service;

import com.utn.UTN.Phone.exceptions.*;
import com.utn.UTN.Phone.model.User;
import com.utn.UTN.Phone.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UserService {

    private UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    //----------------------------------------------------------------------------

    public User login(String username, String password) throws UserNotExistException {
       User u = userRepository.login(username,password);
       return Optional.ofNullable(u).orElseThrow(() -> new UserNotExistException());
    }

    public User findByDni(String dni) throws DuplicateDNI {
        return userRepository.findByDni(dni);
    }

    public User findByUser(String user) throws DuplicateUserName {
        return userRepository.findByUser(user);
    }

    public void addUser(User user) {
        userRepository.save(user);
    }

    public List<User> getAll() {
        return  userRepository.findAll();
    }

    public void updateUser(User userUpdate,Integer id) {

        userRepository.updateUser(userUpdate.getUser(),userUpdate.getPassword(),userUpdate.getName(),userUpdate.getLastname(),userUpdate.getDni(),userUpdate.getCity().getId(),id);
    }

    public void removeUser(Integer id) {
        userRepository.deleteById(id);
    }
}

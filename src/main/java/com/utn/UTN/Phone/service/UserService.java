package com.utn.UTN.Phone.service;

import com.utn.UTN.Phone.config.Crypt;
import com.utn.UTN.Phone.dto.UserDto;
import com.utn.UTN.Phone.exceptions.*;
import com.utn.UTN.Phone.model.User;
import com.utn.UTN.Phone.proyection.ProfileProyection;
import com.utn.UTN.Phone.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;

@Service
@Transactional
public class UserService {

    private UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    public User login(String username, String password) throws UserNotExistException {
        User u = userRepository.login(username, Crypt.cryptWithMD5(password));
        return Optional.ofNullable(u).orElseThrow(() -> new UserNotExistException());
    }

    public User findByDni(String dni) {
        return userRepository.findByDni(dni);
    }

    public User findByUser(String user) {
        return userRepository.findByUser(user);
    }

    public User addUser(User user) {
        user.setPassword(Crypt.cryptWithMD5(user.getPassword()));
        return userRepository.save(user);
    }

    public Integer updateCommonUser(UserDto userDto, Integer id) {
        return userRepository.updateCommonUser(userDto.getUser(), Crypt.cryptWithMD5(userDto.getPassword()), userDto.getName(), userDto.getLastname(), userDto.getDni(), userDto.getCity(), id);
    }

    public void removeUser(Integer id) {
        userRepository.removeById(id);
    }

    public ProfileProyection getProfile(Integer id) throws UserNotExistException {
        ProfileProyection u = userRepository.getProfile(id);
        return Optional.ofNullable(u).orElseThrow(() -> new UserNotExistException());
    }

    /*
    //----------------------------------parcial German-------------------------------------------
    public User getUserByNum(String lineNum) throws UserNotExistException {
        User u = userRepository.getUserByNum(lineNum);
        return Optional.ofNullable(u).orElseThrow(() -> new UserNotExistException());
    }*/

}
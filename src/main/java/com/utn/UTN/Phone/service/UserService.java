package com.utn.UTN.Phone.service;

import com.utn.UTN.Phone.dto.UserDto;
import com.utn.UTN.Phone.exceptions.*;
import com.utn.UTN.Phone.model.User;
import com.utn.UTN.Phone.proyection.ProfileProyection;
import com.utn.UTN.Phone.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
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
        User u = userRepository.login(username, password);
        return Optional.ofNullable(u).orElseThrow(() -> new UserNotExistException());
    }

    public User findByDni(String dni) {
        return userRepository.findByDni(dni);
    }

    public User findByUser(String user) {
        return userRepository.findByUser(user);
    }

    public User addUser(User user) {
        return userRepository.save(user);
    }

    public Integer updateCommonUser(UserDto userDto, Integer id) {
        return userRepository.updateCommonUser(userDto.getUser(), userDto.getPassword(), userDto.getName(), userDto.getLastname(), userDto.getDni(), userDto.getCity(), id);
    }

    public void removeUser(Integer id) {
        userRepository.removeById(id);
    }

    public ProfileProyection getProfile(Integer id) throws UserNotExistException {
        ProfileProyection u = userRepository.getProfile(id);
        return Optional.ofNullable(u).orElseThrow(() -> new UserNotExistException());
    }

    /* public List<User> getAll() throws UserNotExistException {
        List<User> list = userRepository.findAll();
        return Optional.ofNullable(list).orElseThrow(() -> new UserNotExistException());
    }

    public Optional<User> getById(Integer id) throws UserNotExistException {
        Optional<User> u= userRepository.findById(id);
        return Optional.ofNullable(u).orElseThrow(() -> new UserNotExistException());

    }
   public Integer addCommonUser(UserDto userDto) throws SQLException {
        return userRepository.addCommonUser(userDto.getUser(), userDto.getPassword(), userDto.getName(), userDto.getLastname(), userDto.getDni(), userDto.getCity());
    }*/
}
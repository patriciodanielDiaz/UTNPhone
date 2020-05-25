package com.utn.UTN.Phone.Service;


import com.utn.UTN.Phone.Model.LinesUser;
import com.utn.UTN.Phone.Repository.LinesUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LinesUserService {

    private LinesUserRepository linesUserRepository;
    @Autowired
    public LinesUserService(LinesUserRepository linesUserRepository)
    {
        this.linesUserRepository = linesUserRepository;
    }

    public List<LinesUser> getAll()
    {
        return  linesUserRepository.findAll();
    }

    public LinesUser getLinesUserById(Integer id) {
        return linesUserRepository.findById(id).get();
    }

    public void saveOrUpdate(LinesUser lineUser) {
        linesUserRepository.save(lineUser);
    }

    public void delete(Integer id) {
        linesUserRepository.deleteById(id);
    }
}

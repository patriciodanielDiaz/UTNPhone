package com.utn.UTN.Phone.Service;


import com.utn.UTN.Phone.Model.Province;
import com.utn.UTN.Phone.Repository.ProvinceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProvinceService {

    private ProvinceRepository provinceRepository;

    @Autowired
    public  ProvinceService(ProvinceRepository provinceRepository)
    {
        this.provinceRepository = provinceRepository;

    }

    public List<Province> getAll()
    {
        return  provinceRepository.findAll();
    }
}

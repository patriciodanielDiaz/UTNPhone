package com.utn.UTN.Phone.Controller;



import com.utn.UTN.Phone.Model.Province;
import com.utn.UTN.Phone.Service.ProvinceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/province")
public class ProvinceController {

    private ProvinceService provinceService;

    @Autowired

    public  ProvinceController(ProvinceService provinceService)
    {
        this.provinceService = provinceService;
    }
    @GetMapping("/")
    public List<Province> getAll(){
        return provinceService.getAll();
    }
}

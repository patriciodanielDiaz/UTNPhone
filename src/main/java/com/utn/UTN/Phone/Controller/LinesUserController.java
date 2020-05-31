package com.utn.UTN.Phone.Controller;

import com.utn.UTN.Phone.Model.LinesUser;
import com.utn.UTN.Phone.Service.LinesUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/linesuser")
public class LinesUserController {

    private LinesUserService linesUserService;

    @Autowired

    public LinesUserController(LinesUserService linesUserService)
    {
        this.linesUserService = linesUserService;
    }
    @GetMapping("/")
    public List<LinesUser> getAll(){
        return linesUserService.getAll();
    }
}

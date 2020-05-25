package com.utn.UTN.Phone.Controller;


import com.utn.UTN.Phone.Model.LineType;
import com.utn.UTN.Phone.Service.LineTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", methods= {RequestMethod.GET,RequestMethod.POST})
@RestController
@RequestMapping("/linetype")
public class LineTypeController {

    private LineTypeService lineTypeService;

    @Autowired
    public LineTypeController(LineTypeService lineTypeService)
    {
        this.lineTypeService = lineTypeService;
    }

    @GetMapping("/")
    public List<LineType> GetAll()
    {
        return lineTypeService.getAll();
    }
}

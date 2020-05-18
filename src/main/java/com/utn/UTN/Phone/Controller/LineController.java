package com.utn.UTN.Phone.Controller;

import com.utn.UTN.Phone.Model.Line;
import com.utn.UTN.Phone.Service.LineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/line")
public class LineController {

    private LineService lineService;

    @Autowired
    public LineController(LineService lineService) {
        this.lineService = lineService;
    }


    /// metodos de prueba de funcionamiento
    @GetMapping("/")
    public List<Line> getLine(){
        return lineService.getAll();
    }

    @PostMapping("/")
    public void addLine(@RequestBody @Valid Line line){
        lineService.addLine(line);
    }
}

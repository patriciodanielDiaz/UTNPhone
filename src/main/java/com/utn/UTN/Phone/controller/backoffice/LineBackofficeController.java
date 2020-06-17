package com.utn.UTN.Phone.controller.backoffice;

import com.utn.UTN.Phone.dto.LoginDto;
import com.utn.UTN.Phone.exceptions.*;
import com.utn.UTN.Phone.model.Line;
import com.utn.UTN.Phone.model.LineType;
import com.utn.UTN.Phone.model.User;
import com.utn.UTN.Phone.service.LineService;
import com.utn.UTN.Phone.service.LineTypeService;
import com.utn.UTN.Phone.service.UserService;
import com.utn.UTN.Phone.session.SessionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/backoffice/lines")
public class LineBackofficeController {
    LineService lineService;
    UserService userService;
    LineTypeService lineTypeService;
    SessionManager sessionManager;

    @Autowired
    public LineBackofficeController(LineService lineService,LineTypeService lineTypeService, UserService userService,SessionManager sessionManager) {
        this.lineService = lineService;
        this.userService = userService;
        this.lineTypeService=lineTypeService;
        this.sessionManager = sessionManager;
    }
    @GetMapping
    public ResponseEntity<List<Line>> getLineByUser(@RequestHeader("Authorization") String sessionToken,
                                                    @RequestParam(value = "dni", required = false) String dni)
                                                    throws RecordNotExistsException{

        List<Line> lines =null;

        if(dni!=null) lines = lineService.getLinesByUserDNI(dni);
        else  lines = lineService.getAll();

        return (lines.size() > 0) ? ResponseEntity.ok(lines) : ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PostMapping("/{dni}")
    public ResponseEntity <Line> createLine(@RequestHeader("Authorization") String sessionToken,
                                            @PathVariable("dni") String dni,
                                            @RequestParam(value = "lineType", required = true) String type) throws LineTypeNotExistsException {
        ResponseEntity responseEntity;
        User user= userService.findByDni(dni);
        LineType linetype=lineTypeService.getByType(type);
        if(user!=null) {
            Integer idline = lineService.createLine(user.getId(),linetype.getId());
            URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{idLine}").buildAndExpand(idline).toUri();
            responseEntity = ResponseEntity.created(location).build();
        }
        else {
            responseEntity = ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return responseEntity;
    }

    @DeleteMapping
    public ResponseEntity disabledLine(@RequestHeader("Authorization") String sessionToken,
                                       @RequestParam(value = "number",required = true) String num)
                                        throws LineNotExistsException {

        ResponseEntity responseEntity;
        Line line=lineService.getLineByNumber(num);
        Boolean delete= lineService.deleteLine(line.getLinenumber());
        if(delete) responseEntity=ResponseEntity.ok().build();
        else responseEntity=ResponseEntity.badRequest().build();

        return responseEntity;
    }
}

package com.utn.UTN.Phone.controller.backofficeController;

import com.utn.UTN.Phone.exceptions.*;
import com.utn.UTN.Phone.model.Line;
import com.utn.UTN.Phone.model.LineType;
import com.utn.UTN.Phone.model.User;
import com.utn.UTN.Phone.config.RestUtil;
import com.utn.UTN.Phone.service.LineService;
import com.utn.UTN.Phone.service.LineTypeService;
import com.utn.UTN.Phone.service.UserService;
import com.utn.UTN.Phone.session.SessionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/backoffice/line")
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
                                            @RequestParam(value = "lineType", required = true) String type)
                                            throws LineTypeNotExistsException, UserNotExistException{

        ResponseEntity responseEntity;
        User user= userService.findByDni(dni);
        Optional.ofNullable(user).orElseThrow(() -> new UserNotExistException());
        LineType linetype=lineTypeService.getByType(type);
        if(user!=null) {
            Integer idline = lineService.createLine(user.getId(),linetype.getId());
            responseEntity = ResponseEntity.created(RestUtil.getLocation(idline)).build();
        }
        else {
            responseEntity = ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return responseEntity;
    }

    //solucionar :Request method 'DELETE' not supported
    @DeleteMapping("/{dni}/{number}")
    public ResponseEntity disabledLine(@RequestHeader("Authorization") String sessionToken,
                                       @PathVariable("dni") String dni,
                                       @PathVariable("number") String number)
                                        throws LineNotExistsException,UserNotExistException {

        User user= userService.findByDni(dni);
        Optional.ofNullable(user).orElseThrow(() -> new UserNotExistException());
        Line line=lineService.getLineByNumber(number);
        Integer id= lineService.disabledLine(line.getId());

        return (id > 0) ? ResponseEntity.ok().build() : ResponseEntity.badRequest().build();
    }
}

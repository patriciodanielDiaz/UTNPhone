package com.utn.UTN.Phone.controller;

import com.utn.UTN.Phone.dto.LineDto;
import com.utn.UTN.Phone.exceptions.NoDataFound;
import com.utn.UTN.Phone.exceptions.PermissionDeniedException;
import com.utn.UTN.Phone.exceptions.RecordNotExistsException;
import com.utn.UTN.Phone.exceptions.UserNotExistException;
import com.utn.UTN.Phone.model.Line;
import com.utn.UTN.Phone.model.User;
import com.utn.UTN.Phone.proyection.LineProyection;
import com.utn.UTN.Phone.service.LineService;
import com.utn.UTN.Phone.session.SessionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/line")
public class LineController {
    private LineService lineService;
    private SessionManager sessionManager;

    @Autowired
    public LineController(LineService lineService,SessionManager sessionManager) {
        this.lineService = lineService;
        this.sessionManager=sessionManager;
    }

      @GetMapping("/")
    public ResponseEntity<List<Line>> getUserLines(@RequestHeader("Authorization") String sessionToken)
                                                  throws UserNotExistException, RecordNotExistsException {

        User currentUser = getCurrentUser(sessionToken);
        List<Line> lines= lineService.getLinesByUser(currentUser.getId());

        return (lines.size() > 0) ? ResponseEntity.ok(lines) : ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

     private User getCurrentUser(String sessionToken) throws UserNotExistException {
        return Optional.ofNullable(sessionManager.getCurrentUser(sessionToken)).orElseThrow(UserNotExistException::new);
    }
}
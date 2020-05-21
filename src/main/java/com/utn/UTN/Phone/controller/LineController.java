package com.utn.UTN.Phone.controller;

import com.utn.UTN.Phone.exceptions.NoDataFound;
import com.utn.UTN.Phone.exceptions.RecordNotExistsException;
import com.utn.UTN.Phone.model.Line;
import com.utn.UTN.Phone.model.User;
import com.utn.UTN.Phone.service.LineService;
import com.utn.UTN.Phone.session.SessionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/line")
public class LineController {
    private LineService lineService;
    private SessionManager sessionManager;

    @Autowired
    public LineController(LineService lineService,SessionManager sessionManager) {
        this.lineService = lineService;
        this.sessionManager=sessionManager;
    }

    @GetMapping("/all/")
    public ResponseEntity<List<Line>> getAllLine(@RequestHeader("Authorization") String sessionToken) throws RecordNotExistsException {

        User currentUser = sessionManager.getCurrentUser(sessionToken);
        if (currentUser == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        List<Line> lines = null;
        try {
            lines = lineService.getAll();
        } catch (Exception e) {
            throw new RecordNotExistsException();
        }
        return (lines.size() > 0) ? ResponseEntity.ok(lines) : ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
    @GetMapping("/")
    public ResponseEntity<List<Line>> getUserLines(@RequestHeader("Authorization") String sessionToken) throws NoDataFound {

        User currentUser = sessionManager.getCurrentUser(sessionToken);
        if (currentUser == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        List<Line> lines = null;
        try {
            lines = lineService.getLinesByUser(currentUser.getId());
        } catch (Exception e) {
            throw new NoDataFound();
        }
        return (lines.size() > 0) ? ResponseEntity.ok(lines) : ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PostMapping("/")
    public void addLine(@RequestBody @Valid Line line){
        lineService.addLine(line);
    }
}

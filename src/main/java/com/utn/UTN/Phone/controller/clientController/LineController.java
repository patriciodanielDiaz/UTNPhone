package com.utn.UTN.Phone.controller.clientController;

import com.utn.UTN.Phone.dto.LineDto;
import com.utn.UTN.Phone.exceptions.LineNotExistsException;
import com.utn.UTN.Phone.exceptions.PermissionDeniedException;
import com.utn.UTN.Phone.exceptions.RecordNotExistsException;
import com.utn.UTN.Phone.exceptions.UserNotExistException;
import com.utn.UTN.Phone.model.Line;
import com.utn.UTN.Phone.model.User;
import com.utn.UTN.Phone.service.LineService;
import com.utn.UTN.Phone.session.SessionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
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
    public ResponseEntity<List<LineDto>> getUserLines(@RequestHeader("Authorization") String sessionToken)
                                                  throws UserNotExistException, RecordNotExistsException {

        User currentUser = Optional.ofNullable(sessionManager.getCurrentUser(sessionToken)).orElseThrow(UserNotExistException::new);
        List<Line> lines=lineService.getLinesByUser(currentUser);

        return (lines.size() > 0) ? ResponseEntity.ok(LineDto.transferToLineDto(lines)) : ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
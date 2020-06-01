package com.utn.UTN.Phone.controller;
import com.utn.UTN.Phone.dto.CallDto;
import com.utn.UTN.Phone.exceptions.PermissionDeniedException;
import com.utn.UTN.Phone.exceptions.RecordNotExistsException;
import com.utn.UTN.Phone.exceptions.UserNotExistException;
import com.utn.UTN.Phone.model.Call;
import com.utn.UTN.Phone.model.Line;
import com.utn.UTN.Phone.model.User;
import com.utn.UTN.Phone.service.CallService;
import com.utn.UTN.Phone.service.LineService;
import com.utn.UTN.Phone.session.SessionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/call")
public class CallController{

    private CallService callService;
    private LineService lineService;
    private SessionManager sessionManager;

    @Autowired
    public CallController (CallService callService,LineService lineService,SessionManager sessionManager){this.callService=callService;this.lineService=lineService;this.sessionManager=sessionManager;}

    //----------------------commonUser-------------------------------------------------------------------------------------

    @GetMapping("/{lineNumber}/date") // ejemplo posman localhost:8080/api/call/223456786/date?from=2020-01-01&to=2020-06-01
    public ResponseEntity<List<Call>> getCallByUser(@RequestHeader("Authorization") String sessionToken,
                                                    @PathVariable("lineNumber") String lineNumber,
                                                    @RequestParam(value = "from", required = false) String dateFrom,
                                                    @RequestParam(value = "to", required = false) String dateTo)
            throws UserNotExistException, ParseException, PermissionDeniedException, RecordNotExistsException {

        User currentUser = getCurrentUser(sessionToken);
        Line line=lineService.getLineByNumber(lineNumber);
        if(!currentUser.getId().equals(line.getUserId())) throw new PermissionDeniedException();
        List<Call> calls = new ArrayList<>();

        if ((dateFrom != null) && (dateTo != null)) {
            Date fromDate = new SimpleDateFormat("yyyy-MM-dd").parse(dateFrom);
            Date toDate = new SimpleDateFormat("yyyy-MM-dd").parse(dateTo);
            calls = callService.getCallsByDate(line, fromDate, toDate);

        } else {
            calls = callService.getCallsByNumber(line);
        }
        return (calls.size() > 0) ? ResponseEntity.ok(calls) : ResponseEntity.status(HttpStatus.NO_CONTENT).build();

    }


    //------- antena que inserta llamadas ... falta probar y validaciones y el login  ---------------------------

    @PostMapping("/entry")
    public void addCall(@RequestBody @Valid CallDto callDto) throws RecordNotExistsException {

        Line origin=lineService.getLineByNumber(callDto.getOriginNumber());
        Line destination=lineService.getLineByNumber(callDto.getDestinationNumber());

        callService.addCall(origin,destination,callDto.getDurationtime());
    }


    //--------------------------------------------------------------------------------------------------------------
    private User getCurrentUser(String sessionToken) throws UserNotExistException {
        return Optional.ofNullable(sessionManager.getCurrentUser(sessionToken)).orElseThrow(UserNotExistException::new);
    }

}

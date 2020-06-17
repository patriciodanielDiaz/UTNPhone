package com.utn.UTN.Phone.controller;
import com.utn.UTN.Phone.exceptions.LineNotExistsException;
import com.utn.UTN.Phone.exceptions.PermissionDeniedException;
import com.utn.UTN.Phone.exceptions.RecordNotExistsException;
import com.utn.UTN.Phone.exceptions.UserNotExistException;
import com.utn.UTN.Phone.model.Call;
import com.utn.UTN.Phone.model.City;
import com.utn.UTN.Phone.model.Line;
import com.utn.UTN.Phone.model.User;
import com.utn.UTN.Phone.dto.CityDto;
import com.utn.UTN.Phone.service.CallService;
import com.utn.UTN.Phone.service.CityService;
import com.utn.UTN.Phone.service.LineService;
import com.utn.UTN.Phone.service.UserService;
import com.utn.UTN.Phone.session.SessionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    private UserService userService;
    private CityService cityService;
    private SessionManager sessionManager;

    @Autowired
    public CallController (CallService callService,LineService lineService,CityService cityService,SessionManager sessionManager,UserService userService){this.callService=callService;this.lineService=lineService;this.sessionManager=sessionManager;this.userService=userService;this.cityService=cityService;}


    @GetMapping("/{lineNumber}/date") // ejemplo posman localhost:8080/api/call/223456786/date?from=2020-01-01&to=2020-06-01
    public ResponseEntity<List<Call>> getCallByUser(@RequestHeader("Authorization") String sessionToken,
                                                    @PathVariable("lineNumber") String lineNumber,
                                                    @RequestParam(value = "from", required = false) String dateFrom,
                                                    @RequestParam(value = "to", required = false) String dateTo)
            throws UserNotExistException, ParseException, PermissionDeniedException, RecordNotExistsException, LineNotExistsException {

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

    @GetMapping("/{lineNumber}/destinations/") // ejemplo posman localhost:8080/api/call/+54 (9) 223 154211100/Destinations/
    public ResponseEntity<List<CityDto>> getDestinationsTop(@RequestHeader("Authorization") String sessionToken,
                                                         @PathVariable("lineNumber") String lineNumber) throws RecordNotExistsException {

        List<CityDto> cities=cityService.getTopDestination(lineNumber);
        return (cities.size() > 0) ? ResponseEntity.ok(cities) : ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    private User getCurrentUser(String sessionToken) throws UserNotExistException {
        return Optional.ofNullable(sessionManager.getCurrentUser(sessionToken)).orElseThrow(UserNotExistException::new);
    }


    //---------------Parcial German-------------------------------------------------------------------
    /*@GetMapping("/parcial")
    private ResponseEntity<User> getCallsmall(String sessionToken) throws PermissionDeniedException, RecordNotExistsException {

        User currentUser = sessionManager.getCurrentUser(sessionToken);
        if(currentUser.getUserType()!= User.Type.empleado) throw new PermissionDeniedException();

        Call call=callService.getCallSmall();
        User user =userService.getUserByNum(call.getOriginCall().getLinenumber());

        return (user!= null) ? ResponseEntity.ok(user) : ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }*/
}

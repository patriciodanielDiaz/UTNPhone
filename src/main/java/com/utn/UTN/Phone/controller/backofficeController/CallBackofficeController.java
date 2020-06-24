package com.utn.UTN.Phone.controller.backofficeController;

import com.utn.UTN.Phone.dto.CallDto;
import com.utn.UTN.Phone.exceptions.*;
import com.utn.UTN.Phone.model.Call;
import com.utn.UTN.Phone.model.Line;
import com.utn.UTN.Phone.model.User;
import com.utn.UTN.Phone.restUtill.RestUtil;
import com.utn.UTN.Phone.service.*;
import com.utn.UTN.Phone.session.SessionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("backoffice/call")
public class CallBackofficeController {

    UserService userService;
    CallService callService;
    LineService lineService;
    SessionManager sessionManager;

    @Autowired
    public CallBackofficeController(LineService lineService,UserService userService,CallService callService, SessionManager sessionManager) {
        this.lineService=lineService;
        this.userService = userService;
        this.callService = callService;
        this.sessionManager = sessionManager;
    }

    @GetMapping("/{dni}/{lineNumer}/date") //localhost:8080/backoffice/call/333333/5893239/date?from=2020-01-01&to=2020-06-12
    public ResponseEntity<List<Call>> getCallByUser(@RequestHeader("Authorization") String sessionToken,
                                                    @PathVariable("dni") String dni,
                                                    @PathVariable("lineNumer") String lineNumber,
                                                    @RequestParam(value = "from", required = false) String dateFrom,
                                                    @RequestParam(value = "to", required = false) String dateTo)
            throws UserNotExistException, ParseException, RecordNotExistsException, LineNotExistsException {

        User user=userService.findByDni(dni);
        Optional.ofNullable(user).orElseThrow(() -> new UserNotExistException());//no me queda otra porque necesito que el finByDni null en el login

        Line line=lineService.getLineByNumber(lineNumber);

        List<Call> calls = new ArrayList<>();

        if ((dateFrom != null) && (dateTo != null)) {
            Date fromDate = new SimpleDateFormat("yyyy-MM-dd").parse(dateFrom);
            Date toDate = new SimpleDateFormat("yyyy-MM-dd").parse(dateTo);
            calls = callService.getCallsByDate(line, fromDate, toDate);

        } else {
            calls = callService.getCallsByLine(line);
        }
        return (calls.size() > 0) ? ResponseEntity.ok(calls) : ResponseEntity.status(HttpStatus.NO_CONTENT).build();

    }

    //------------------------ antena que inserta llamadas ----------------------------------

    @PostMapping("/entry")//localhost:8080/backoffice/call/entry/
    public ResponseEntity addCall(@RequestHeader("Authorization") String sessionToken,@Valid @RequestBody CallDto callDto) throws LineNotExistsException {

        User antena= sessionManager.getCurrentUser(sessionToken);
        Line origin=lineService.getLineByNumber(callDto.getOriginNumber());
        Line destination=lineService.getLineByNumber(callDto.getDestinationNumber());
        Call call = callService.addCall(origin,destination,callDto.getDuration(),callDto.getDateTime());
        return ResponseEntity.created(RestUtil.getLocation(call.getId())).build();

    }

}

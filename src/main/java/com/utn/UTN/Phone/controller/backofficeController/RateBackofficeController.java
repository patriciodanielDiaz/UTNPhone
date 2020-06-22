package com.utn.UTN.Phone.controller.backofficeController;

import com.utn.UTN.Phone.exceptions.*;
import com.utn.UTN.Phone.model.*;
import com.utn.UTN.Phone.service.CityService;
import com.utn.UTN.Phone.service.RateService;
import com.utn.UTN.Phone.service.UserService;
import com.utn.UTN.Phone.session.SessionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/backoffice/rate")
public class RateBackofficeController {
    UserService userService;
    RateService rateService;
    CityService cityService;
    SessionManager sessionManager;

    @Autowired
    public RateBackofficeController(UserService userService, CityService cityService ,RateService rateService, SessionManager sessionManager) {
        this.userService = userService;
        this.rateService = rateService;
        this.cityService = cityService;
        this.sessionManager = sessionManager;
    }

    @GetMapping //  posman localhost:8080/backoffice/rate
    public ResponseEntity<List<Rate>> getRate(@RequestHeader("Authorization") String sessionToken) throws RecordNotExistsException {

        List<Rate> rates = rateService.getAll();

        return  (rates.size() > 0) ? ResponseEntity.ok(rates) : ResponseEntity.status(HttpStatus.NO_CONTENT).build();

    }

    @GetMapping("/{city}/") //  posman localhost:8080/backoffice/rate/Mar Del Plata/
    public ResponseEntity<List<Rate>> getRate(@RequestHeader("Authorization") String sessionToken,
                                                 @PathVariable("city") String city) throws CityNotExistsException, RecordNotExistsException {

        City c=cityService.getCityByName(city);
        List<Rate> rates = rateService.getByCity(c);
        return  (rates.size() > 0) ? ResponseEntity.ok(rates) : ResponseEntity.status(HttpStatus.NO_CONTENT).build();

    }
}

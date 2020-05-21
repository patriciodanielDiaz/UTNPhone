package com.utn.UTN.Phone.controller;

import com.utn.UTN.Phone.exceptions.RecordNotExistsException;
import com.utn.UTN.Phone.model.*;
import com.utn.UTN.Phone.service.*;
import com.utn.UTN.Phone.session.SessionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping ("/prueba")
public class PruebaController {

    private CityService cityService;
    private RateService rateService;
    private CallService callService;
    private InvoiceService invoiceService;
    private LineService lineService;
    private SessionManager sessionManager;

    @Autowired
    public PruebaController(CityService cityService,RateService rateService,CallService callService,InvoiceService invoiceService,LineService lineService,SessionManager sessionManager) { this.cityService = cityService; this.rateService = rateService;this.callService = callService;this.invoiceService = invoiceService;this.lineService = lineService;this.sessionManager=sessionManager;}



    /// metodos de prueba de funcionamiento

    //--city-----------------------------------------------------------------

                                        //prueba session

    @PostMapping("/city")
    public void addCity(@RequestBody @Valid City city){
        cityService.addCity(city);
    }

    //--rate-----------------------------------------------------------------
    @GetMapping("/rate")
    public List<Rate> getRate(){
        return rateService.getAll();
    }

    @PostMapping("/rate")
    public void addRate(@RequestBody @Valid Rate rate){
        rateService.addRate(rate);
    }

    //--call-----------------------------------------------------------------
    @GetMapping("/call")
    public List<Call> getCall(){ return callService.getAll(); }

    @PostMapping("/call")
    public void addCall(@RequestBody @Valid Call call){ callService.addCall(call); }

    //--invoice-----------------------------------------------------------------
    @GetMapping("/invoice")
    public List<Invoice> getInvoice(){ return invoiceService.getAll(); }

    @PostMapping("/invoice")
    public void addInvoice(@RequestBody @Valid Invoice invoice){ invoiceService.addInvoice(invoice); }

}

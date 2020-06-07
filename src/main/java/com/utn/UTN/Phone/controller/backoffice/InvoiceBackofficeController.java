package com.utn.UTN.Phone.controller.backoffice;

import com.utn.UTN.Phone.exceptions.LineNotExistsException;
import com.utn.UTN.Phone.exceptions.PermissionDeniedException;
import com.utn.UTN.Phone.exceptions.RecordNotExistsException;
import com.utn.UTN.Phone.exceptions.UserNotExistException;
import com.utn.UTN.Phone.model.Invoice;
import com.utn.UTN.Phone.model.Line;
import com.utn.UTN.Phone.model.User;
import com.utn.UTN.Phone.service.InvoiceService;
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
@RequestMapping("backoffice/invoice")
public class InvoiceBackofficeController {

    private InvoiceService invoiceService;
    private LineService lineService;
    private UserService userService;
    private SessionManager sessionManager;

    @Autowired
    public InvoiceBackofficeController (InvoiceService invoiceService,LineService lineService,SessionManager sessionManager,UserService userService){this.invoiceService=invoiceService;this.lineService=lineService;this.sessionManager=sessionManager;this.userService=userService;}

    @GetMapping("/{dni}/{lineNumer}/date") ////localhost:8080/backoffice/invoice/333333/5893239/date?from=2020-01-01&to=2020-06-12
    public ResponseEntity<List<Invoice>> getInvoiceByUser(@RequestHeader("Authorization") String sessionToken,
                                                          @PathVariable("dni") String dni,
                                                          @PathVariable("lineNumer") String lineNumber,
                                                          @RequestParam(value = "from", required = false) String dateFrom,
                                                          @RequestParam(value = "to", required = false) String dateTo)
            throws UserNotExistException, ParseException, PermissionDeniedException, RecordNotExistsException, LineNotExistsException {

        User user=userService.findByDni(dni);

        List<Invoice> invoices = new ArrayList<>();
        Optional.ofNullable(user).orElseThrow(() -> new UserNotExistException());//no me queda otra porque necesito que el finByDni venga null en el login

        Line line=lineService.getLineByNumber(lineNumber);

        if ((dateFrom != null) && (dateTo != null)) {
            Date fromDate = new SimpleDateFormat("yyyy-MM-dd").parse(dateFrom);
            Date toDate = new SimpleDateFormat("yyyy-MM-dd").parse(dateTo);
            invoices = invoiceService.getInvoicesByDate(line.getLinenumber(), fromDate, toDate); //crer proyecion

        } else {
            invoices = invoiceService.getInvoicesByNumber(line.getLinenumber()); //crear proyeccion
        }

        return  (invoices.size() > 0) ? ResponseEntity.ok(invoices) : ResponseEntity.status(HttpStatus.NO_CONTENT).build();

    }


}

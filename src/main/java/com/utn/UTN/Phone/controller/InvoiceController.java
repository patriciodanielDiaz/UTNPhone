package com.utn.UTN.Phone.controller;

import com.utn.UTN.Phone.dto.InvoiceDto;
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
import java.util.*;

@RestController
@RequestMapping("api/invoices")
public class InvoiceController {

    private InvoiceService invoiceService;
    private LineService lineService;
    private UserService userService;
    private SessionManager sessionManager;

    @Autowired
    public InvoiceController (InvoiceService invoiceService,LineService lineService,SessionManager sessionManager,UserService userService){this.invoiceService=invoiceService;this.lineService=lineService;this.sessionManager=sessionManager;this.userService=userService;}

    @GetMapping("/{lineNumber}/date") //  posman localhost:8080/api/invoices/223456786/date?from=2020-01-01&to=2020-06-01
    public ResponseEntity<List<Invoice>> getInvoiceByUser(@RequestHeader("Authorization") String sessionToken,
                                                                 @PathVariable("lineNumber") String lineNumber,
                                                                 @RequestParam(value = "from", required = false) String dateFrom,
                                                                 @RequestParam(value = "to", required = false) String dateTo)
            throws UserNotExistException, ParseException, PermissionDeniedException, RecordNotExistsException, LineNotExistsException {

        User currentUser = getCurrentUser(sessionToken);
        Line line=lineService.getLineByNumber(lineNumber);
        if(!currentUser.getId().equals(line.getUserId())) throw new PermissionDeniedException();
        List<Invoice> invoices = new ArrayList<>();

        if ((dateFrom != null) && (dateTo != null)) {
            Date fromDate = new SimpleDateFormat("yyyy-MM-dd").parse(dateFrom);
            Date toDate = new SimpleDateFormat("yyyy-MM-dd").parse(dateTo);
            invoices = invoiceService.getInvoicesByDate(line.getLinenumber(), fromDate, toDate);

        } else {
            invoices = invoiceService.getInvoicesByNumber(line.getLinenumber());
        }

        //no funciona tranferir a un dto, me repite los datos

        /*List<InvoiceDto> invoicesDtos= new ArrayList<>();
        InvoiceDto tranferDto=new InvoiceDto();
       for (Invoice in:invoices) {

            tranferDto.setCode(in.getId());
            tranferDto.setNumber(lineNumber);
            tranferDto.setName(in.getUser().getName());
            tranferDto.setLastName(in.getUser().getLastname());
            tranferDto.setDni(in.getUser().getDni());
            tranferDto.setTotal(in.getTotal());
            tranferDto.setState((in.getState()==true)? "Factura Pagada" :"Factura Impago");
            tranferDto.setExpirationDate(in.getPaymentDate());

            System.out.println(tranferDto);

            invoicesDtos.add(tranferDto);
        }
        for (InvoiceDto id: invoicesDtos) {
            System.out.println(id);
        }*/

        return  (invoices.size() > 0) ? ResponseEntity.ok(invoices) : ResponseEntity.status(HttpStatus.NO_CONTENT).build();

    }

    private User getCurrentUser(String sessionToken) throws UserNotExistException {
        return Optional.ofNullable(sessionManager.getCurrentUser(sessionToken)).orElseThrow(UserNotExistException::new);
    }

}

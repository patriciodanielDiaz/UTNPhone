package com.utn.UTN.Phone.controller.clientController;

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
@RequestMapping("api/invoice")
public class InvoiceController {

    private InvoiceService invoiceService;
    private LineService lineService;
    private UserService userService;
    private SessionManager sessionManager;

    @Autowired
    public InvoiceController (InvoiceService invoiceService,LineService lineService,SessionManager sessionManager,UserService userService){this.invoiceService=invoiceService;this.lineService=lineService;this.sessionManager=sessionManager;this.userService=userService;}

    @GetMapping("/{lineNumber}/date") //  posman localhost:8080/api/invoices/+54 (9) 223 154211100/date?from=2020-01-01&to=2020-06-01
    public ResponseEntity<List<InvoiceDto>> getInvoiceByUser(@RequestHeader("Authorization") String sessionToken,
                                                             @PathVariable("lineNumber") String lineNumber,
                                                             @RequestParam(value = "from", required = false) String from,
                                                             @RequestParam(value = "to", required = false) String to)
                                                            throws UserNotExistException, ParseException, PermissionDeniedException, RecordNotExistsException, LineNotExistsException {

        User currentUser = Optional.ofNullable(sessionManager.getCurrentUser(sessionToken)).orElseThrow(UserNotExistException::new);
        Line line=lineService.getLineByNumber(lineNumber);
        if(!currentUser.getId().equals(line.getUserId())) throw new PermissionDeniedException();
        List<Invoice> invoices = new ArrayList<>();

        if ((from != null) && (to != null)) {
            Date fromDate = new SimpleDateFormat("yyyy-MM-dd").parse(from);
            Date toDate = new SimpleDateFormat("yyyy-MM-dd").parse(to);
            invoices = invoiceService.getInvoicesByDate(line.getLinenumber(), fromDate, toDate);
        } else {
            invoices = invoiceService.getInvoicesByLine(line);
        }
        return  (invoices.size() > 0) ? ResponseEntity.ok(InvoiceDto.transferToInvoicesDto(invoices)) : ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}

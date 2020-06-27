package com.utn.UTN.Phone.controller.backofficeController;

import com.utn.UTN.Phone.controller.clientController.InvoiceController;
import com.utn.UTN.Phone.dto.InvoiceDto;
import com.utn.UTN.Phone.exceptions.LineNotExistsException;
import com.utn.UTN.Phone.exceptions.PermissionDeniedException;
import com.utn.UTN.Phone.exceptions.RecordNotExistsException;
import com.utn.UTN.Phone.exceptions.UserNotExistException;
import com.utn.UTN.Phone.model.Invoice;
import com.utn.UTN.Phone.model.Line;
import com.utn.UTN.Phone.model.LineType;
import com.utn.UTN.Phone.model.User;
import com.utn.UTN.Phone.service.InvoiceService;
import com.utn.UTN.Phone.service.LineService;
import com.utn.UTN.Phone.service.UserService;
import com.utn.UTN.Phone.session.SessionManager;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.http.ResponseEntity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.utn.UTN.Phone.model.User.Type.empleado;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest(InvoiceDto.class)
public class InvoiceBackofficeControllerTest {
    @Mock
    SessionManager sessionManager;
    @Mock
    LineService lineService;
    @Mock
    UserService userService;
    @Mock
    InvoiceService invoiceService;
    @Mock
    InvoiceDto invoiceDto;
    InvoiceBackofficeController invoiceBackofficeController;
    User user;
    Line lineOrigin;
    Invoice invoice;

    @Before
    public void setUp() {
        initMocks(this);
        PowerMockito.mockStatic(InvoiceDto.class);
        invoiceBackofficeController=new InvoiceBackofficeController(invoiceService,lineService,sessionManager,userService);
        user = new User(1,"mariano", "123456", "bbbb", "cccc","12345",empleado,null,null,null,null);        LineType lineType=new LineType(1,"celular",null,null);
        lineOrigin=new Line(1,"123456",lineType,user,null,null,null);
        invoice=new Invoice(1,user,lineOrigin,null,null,null,null,null,null,null,null,null);

    }

    @Test
    public void testGetInvoiceByUserDateOk() throws LineNotExistsException, ParseException, RecordNotExistsException, UserNotExistException, PermissionDeniedException {

        List<Invoice> invoices=new ArrayList<>();
        invoices.add(invoice);

        when(userService.findByDni("123456")).thenReturn(user);
        when(lineService.getLineByNumber("123456")).thenReturn( lineOrigin);
        Date fromDate = new SimpleDateFormat("yyyy-MM-dd").parse("20-01-01");
        Date toDate = new SimpleDateFormat("yyyy-MM-dd").parse("20-07-07");

        when(invoiceService.getInvoicesByDate("123456", fromDate, toDate)).thenReturn(invoices);
        when(InvoiceDto.transferToInvoicesDto(invoices)).thenReturn(new ArrayList<InvoiceDto>());

        ResponseEntity re = invoiceBackofficeController.getInvoiceByUser("123456","123456","123456","20-01-01","20-07-07");

        assertEquals(ResponseEntity.ok(new ArrayList<InvoiceDto>()), re);
        verify(lineService, times(1)).getLineByNumber("123456");
        verify(userService, times(1)).findByDni("123456");
        verify(invoiceService, times(1)).getInvoicesByDate("123456",fromDate,toDate);

    }
    @Test
    public void testGetInvoiceByUserOk() throws LineNotExistsException, ParseException, RecordNotExistsException, UserNotExistException, PermissionDeniedException {

        List<Invoice> invoices=new ArrayList<>();
        invoices.add(invoice);

        when(userService.findByDni("123456")).thenReturn(user);
        when(lineService.getLineByNumber("123456")).thenReturn( lineOrigin);

        when(invoiceService.getInvoicesByLine(lineOrigin)).thenReturn(invoices);
        when(InvoiceDto.transferToInvoicesDto(invoices)).thenReturn(new ArrayList<InvoiceDto>());

        ResponseEntity re = invoiceBackofficeController.getInvoiceByUser("123456","123456","123456",null,null);

        assertEquals(ResponseEntity.ok(new ArrayList<InvoiceDto>()), re);
        verify(lineService, times(1)).getLineByNumber("123456");
        verify(userService, times(1)).findByDni("123456");
        verify(invoiceService, times(1)).getInvoicesByLine(lineOrigin);

    }
    @Test(expected = UserNotExistException.class)
    public void testGetInvoiceByUserDateUserNotExistException() throws PermissionDeniedException, UserNotExistException, ParseException, LineNotExistsException, RecordNotExistsException {

        when(userService.findByDni("123456")).thenReturn(null);
        invoiceBackofficeController.getInvoiceByUser("123456","123456","123456","20-01-01","20-07-07");
        verify(userService, times(1)).findByDni("123456");

    }
    @Test(expected = LineNotExistsException.class)
    public void testGetInvoiceByUserDateLineNotExistsException() throws PermissionDeniedException, UserNotExistException, ParseException, LineNotExistsException, RecordNotExistsException {

        when(userService.findByDni("123456")).thenReturn(user);
        Mockito.when(lineService.getLineByNumber("123456")).thenThrow(new LineNotExistsException());
        invoiceBackofficeController.getInvoiceByUser("123456","123456","123456","20-01-01","20-07-07");

        verify(lineService, times(1)).getLineByNumber("123456");
        verify(userService, times(1)).findByDni("123456");
    }
    @Test(expected = RecordNotExistsException.class)
    public void testGetInvoiceByUserDateRecordNotExistsException() throws LineNotExistsException, ParseException, RecordNotExistsException, UserNotExistException, PermissionDeniedException {

        List<Invoice> invoices=new ArrayList<>();
        invoices.add(invoice);

        when(userService.findByDni("123456")).thenReturn(user);
        when(lineService.getLineByNumber("123456")).thenReturn( lineOrigin);
        Date fromDate = new SimpleDateFormat("yyyy-MM-dd").parse("20-01-01");
        Date toDate = new SimpleDateFormat("yyyy-MM-dd").parse("20-07-07");

        when(invoiceService.getInvoicesByDate("123456", fromDate, toDate)).thenThrow(new RecordNotExistsException());
        when(InvoiceDto.transferToInvoicesDto(invoices)).thenReturn(new ArrayList<InvoiceDto>());

        invoiceBackofficeController.getInvoiceByUser("123456","123456","123456","20-01-01","20-07-07");

        verify(lineService, times(1)).getLineByNumber("123456");
        verify(userService, times(1)).findByDni("123456");
        verify(invoiceService, times(1)).getInvoicesByDate("123456",fromDate,toDate);

    }
}

package com.utn.UTN.Phone.service;

import com.utn.UTN.Phone.exceptions.RecordNotExistsException;
import com.utn.UTN.Phone.model.Invoice;
import com.utn.UTN.Phone.model.Line;
import com.utn.UTN.Phone.model.LineType;
import com.utn.UTN.Phone.model.User;
import com.utn.UTN.Phone.repository.InvoiceRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import java.util.ArrayList;
import java.util.List;

import static com.utn.UTN.Phone.model.User.Type.empleado;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class InvoiceServiceTest {
    InvoiceService invoiceService;
    @Mock
    InvoiceRepository invoiceRepository;
    User user ;
    LineType lineType;
    Line line;
    Invoice invoice;

    @Before
    public void setUp() {
        initMocks(this);
        invoiceService= new InvoiceService(invoiceRepository);
        user = new User(1,"mariano", "123456", "bbbb", "cccc","12345",empleado,null,null,null,null);
        lineType=new LineType(1,"celular",null,null);
        line=new Line(1,"123456",lineType,user,null,null,null);
        invoice = new Invoice(10,user,line,2,2.5f,2.5f,null,null,false,null,null,null);

    }

    @Test
    public void testGetInvoicesByDateOk() throws RecordNotExistsException, ParseException {

        List<Invoice> invoices=new ArrayList<>();
        Date fromDate = new SimpleDateFormat("yyyy-MM-dd").parse("2020-01-01");
        Date toDate = new SimpleDateFormat("yyyy-MM-dd").parse("2020-07-01");
        invoices.add(invoice);

        when(invoiceRepository.getInvoicesByDate("123456",fromDate,toDate)).thenReturn(invoices);
        List<Invoice> lReturn = invoiceService.getInvoicesByDate("123456",fromDate,toDate);
        assertEquals(invoices,lReturn);
        verify(invoiceRepository, times(1)).getInvoicesByDate("123456",fromDate,toDate);
    }
    @Test(expected = RecordNotExistsException.class)
    public void testGetInvoicesByDateRecordNotExistsException() throws RecordNotExistsException, ParseException {
        Date fromDate = new SimpleDateFormat("yyyy-MM-dd").parse("2020-01-01");
        Date toDate = new SimpleDateFormat("yyyy-MM-dd").parse("2020-07-01");
        when(invoiceRepository.getInvoicesByDate("123456",fromDate,toDate)).thenReturn(null);
        invoiceService.getInvoicesByDate("123456",fromDate,toDate);
    }

    @Test
    public void testGetInvoicesByLineOk() throws RecordNotExistsException {

        List<Invoice> invoices=new ArrayList<>();
        invoices.add(invoice);

        when(invoiceRepository.findAllByLine(line)).thenReturn(invoices);
        List<Invoice> lReturn = invoiceService.getInvoicesByLine(line);
        assertEquals(invoices,lReturn);
        verify(invoiceRepository, times(1)).findAllByLine(line);
    }
    @Test(expected = RecordNotExistsException.class)
    public void testGetInvoicesByLineRecordNotExistsException() throws RecordNotExistsException{

        when(invoiceRepository.findAllByLine(line)).thenReturn(null);
        invoiceService.getInvoicesByLine(line);
    }

}

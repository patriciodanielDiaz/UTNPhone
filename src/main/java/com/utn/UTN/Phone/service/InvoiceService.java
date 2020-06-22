package com.utn.UTN.Phone.service;

import com.utn.UTN.Phone.exceptions.RecordNotExistsException;
import com.utn.UTN.Phone.model.Call;
import com.utn.UTN.Phone.model.Invoice;
import com.utn.UTN.Phone.model.Line;
import com.utn.UTN.Phone.proyection.InvoiceProyection;
import com.utn.UTN.Phone.repository.InvoiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class InvoiceService {

    private InvoiceRepository invoiceRepository;

    @Autowired
    public InvoiceService(InvoiceRepository invoiceRepository) {
        this.invoiceRepository = invoiceRepository;
    }

    public List<Invoice> getInvoicesByDate(String lineNumber, Date fromDate, Date toDate) throws RecordNotExistsException {
        List<Invoice> invoices = invoiceRepository.getInvoicesByDate(lineNumber,fromDate,toDate);
        return Optional.ofNullable(invoices).orElseThrow(() -> new RecordNotExistsException());
    }

    public List<Invoice> getInvoicesByLine(Line line) throws RecordNotExistsException {
        List<Invoice> invoices = invoiceRepository.findAllByLine(line);
        return Optional.ofNullable(invoices).orElseThrow(() -> new RecordNotExistsException());}
}

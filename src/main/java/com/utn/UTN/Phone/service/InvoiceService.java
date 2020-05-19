package com.utn.UTN.Phone.service;

import com.utn.UTN.Phone.model.Invoice;
import com.utn.UTN.Phone.repository.InvoiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InvoiceService {

    private InvoiceRepository invoiceRepository;

    @Autowired
    public InvoiceService(InvoiceRepository invoiceRepository) {
        this.invoiceRepository = invoiceRepository;
    }



    //----------------------------------------------------------------------------
    public void addInvoice(Invoice invoice) {
        invoiceRepository.save(invoice);
    }

    public List<Invoice> getAll() {
        return  invoiceRepository.findAll();
    }
}

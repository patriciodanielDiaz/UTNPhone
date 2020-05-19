package com.utn.UTN.Phone.repository;

import com.utn.UTN.Phone.model.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InvoiceRepository extends JpaRepository<Invoice,Integer> {
}

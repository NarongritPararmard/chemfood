package com.cp.kku.demo.repository;

import com.cp.kku.demo.model.InvoiceProduct;
import com.cp.kku.demo.model.ReceiptProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface InvoiceProductRepository extends JpaRepository<InvoiceProduct, Long> {
    @Query("SELECT ip.id FROM InvoiceProduct ip WHERE ip.invoice.id = :id")
    List<String> findProductIdByInvoiceId(Long id);

}

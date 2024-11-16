package com.cp.kku.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cp.kku.demo.model.Invoice;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Long> {
    // คุณสามารถเพิ่ม custom query methods ได้ถ้าต้องการ
}

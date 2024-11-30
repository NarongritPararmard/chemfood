package com.cp.kku.demo.repository;

import com.cp.kku.demo.model.Product;
import com.cp.kku.demo.model.ReceiptProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

@Repository
public interface ReceiptProductRepository extends JpaRepository<ReceiptProduct, Long> {

    @Query("SELECT rp.id FROM ReceiptProduct rp WHERE rp.sampleReceipt.id = :id")
    List<String> findProductIdBySampleReceiptId(Long id);

    /**
     * ค้นหา ReceiptProduct ทั้งหมดที่เชื่อมโยงกับ Receipt ID
     * @param receiptId ID ของ Receipt
     * @return รายการ ReceiptProduct
     */
//    List<ReceiptProduct> findByReceiptId(Long receiptId);
}
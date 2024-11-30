//package com.cp.kku.demo.service;
//
//import com.cp.kku.demo.model.ReceiptProduct;
//import com.cp.kku.demo.repository.ReceiptProductRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//
//@Service
//public class ReceiptProductService {
//
//    @Autowired
//    private ReceiptProductRepository receiptProductRepository;
//
//    /**
//     * ดึงข้อมูล ReceiptProduct ทั้งหมดที่เชื่อมโยงกับ Receipt ID
//     * @param receiptId ID ของ Receipt
//     * @return รายการ ReceiptProduct
//     */
//    public List<ReceiptProduct> getProductsByReceiptId(Long receiptId) {
//        return receiptProductRepository.findByReceiptId(receiptId);
//    }
//}
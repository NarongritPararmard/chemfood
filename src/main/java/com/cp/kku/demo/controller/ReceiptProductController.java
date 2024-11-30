//package com.cp.kku.demo.controller;
//
//import com.cp.kku.demo.model.ReceiptProduct;
//import com.cp.kku.demo.service.ReceiptProductService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//
//import java.util.List;
//
//@Controller
//public class ReceiptProductController {
//
//    @Autowired
//    private ReceiptProductService receiptProductService;
//
//    /**
//     * แสดงรายละเอียดของ ReceiptProduct ที่เชื่อมโยงกับ Receipt
//     * @param receiptId ID ของ Receipt
//     * @param model Model สำหรับส่งข้อมูลไปยัง Thymeleaf
//     * @return ชื่อไฟล์ HTML สำหรับแสดงผล
//     */
//    @GetMapping("/receipt/{receiptId}/products")
//    public String getReceiptProducts(@PathVariable Long receiptId, Model model) {
//        // ดึงข้อมูล ReceiptProduct ทั้งหมดที่เกี่ยวข้องกับ Receipt ID
//        List<ReceiptProduct> receiptProducts = receiptProductService.getProductsByReceiptId(receiptId);
//
//        // ส่งข้อมูล ReceiptProduct ไปยัง View
//        model.addAttribute("receiptProducts", receiptProducts);
//
//        // ส่งข้อมูล Receipt ID ไปยัง View
//        model.addAttribute("receiptId", receiptId);
//
//        // คืนค่าไปยังหน้ารายละเอียด
//        return "receipt-details";
//    }
//}

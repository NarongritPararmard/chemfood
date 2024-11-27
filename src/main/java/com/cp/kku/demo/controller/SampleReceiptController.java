package com.cp.kku.demo.controller;



import java.beans.PropertyEditorSupport;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.cp.kku.demo.model.Company;
import com.cp.kku.demo.model.Product;
import com.cp.kku.demo.model.ReceiptProduct;
import com.cp.kku.demo.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import com.cp.kku.demo.model.SampleReceipt;
import com.cp.kku.demo.repository.CompanyRepository;
import com.cp.kku.demo.repository.SampleReceiptRepository;

@Controller
@RequestMapping("/receipts")
public class SampleReceiptController {

    private final SampleReceiptRepository sampleReceiptRepository;
    private final CompanyRepository companyRepository;
    private final ProductRepository productRepository;

    @Autowired
    public SampleReceiptController(SampleReceiptRepository sampleReceiptRepository, CompanyRepository companyRepository, ProductRepository productRepository) {
        this.sampleReceiptRepository = sampleReceiptRepository;
        this.companyRepository = companyRepository;
        this.productRepository = productRepository;
    }

    @GetMapping
    public String getAllReceipts(Model model) {
        List<SampleReceipt> receipts = sampleReceiptRepository.findAll();
        model.addAttribute("receipts", receipts);
        return "receipt-list";
    }

    @GetMapping("/new")
    public String createReceiptForm(Model model) {
        model.addAttribute("receipt", new SampleReceipt());
        model.addAttribute("companies", companyRepository.findAll());
        return "receipt-form";
    }

//    @PostMapping
//    public String saveReceipt(@ModelAttribute SampleReceipt sampleReceipt) {
//        sampleReceipt.setCompanyName(companyRepository.findById(sampleReceipt.getCompany().getId()).get().getCompanyName());
//        sampleReceiptRepository.save(sampleReceipt);
//        return "redirect:/receipts";
//    }
@PostMapping
public String saveReceipt(@ModelAttribute SampleReceipt sampleReceipt) {
    // พิมพ์ข้อมูล Company
    System.out.println("Company Name: " + companyRepository.findById(sampleReceipt.getCompany().getId()).get().getCompanyName());
//    for (ReceiptProduct receiptProduct : sampleReceipt.getReceiptProducts()) {
//        receiptProduct.setSampleReceipt(sampleReceipt); // ตั้งค่าความสัมพันธ์กับ SampleReceipt
//    }

    // พิมพ์ข้อมูลสินค้า
    if (sampleReceipt.getReceiptProducts() != null && !sampleReceipt.getReceiptProducts().isEmpty()) {
        System.out.println("Selected Products:");
        for (ReceiptProduct receiptProduct : sampleReceipt.getReceiptProducts()) {
            System.out.println("Product Name: " + receiptProduct.getProduct().getProductName() +
                    ", Product Code: " + receiptProduct.getProduct().getProductCode() +
                    ", Quantity: " + receiptProduct.getProduct().getQuantity() +
                    ", Price: " + receiptProduct.getProduct().getPrice());
        }
    } else {
        System.out.println("No products selected.");
    }

    // ตั้งค่า Company Name ก่อนบันทึก

    sampleReceipt.setCompanyName(companyRepository.findById(sampleReceipt.getCompany().getId()).get().getCompanyName());

    // บันทึกข้อมูล
    sampleReceiptRepository.save(sampleReceipt);

    return "redirect:/receipts";
}

//@PostMapping
//public String createReceipt(@ModelAttribute("receipt") SampleReceipt receipt) {
//    // Set company name from selected company
//    Company company = companyRepository.findById(receipt.getCompany().getId())
//            .orElseThrow(() -> new RuntimeException("Company not found"));
//    receipt.setCompanyName(company.getCompanyName());
//
//    // Prepare receipt products
//    List<ReceiptProduct> receiptProducts = new ArrayList<>();
//    for (ReceiptProduct receiptProduct : receipt.getReceiptProducts()) {
//        // Find original product
//        Product originalProduct = productRepository.findById(receiptProduct.getProduct().getId())
//                .orElseThrow(() -> new RuntimeException("Product not found"));
//
//        // Set real product details
//        receiptProduct.setRealProductCode(originalProduct.getProductCode());
//        receiptProduct.setRealProductName(originalProduct.getProductName());
//        receiptProduct.setRealDescription(originalProduct.getDescription());
//        receiptProduct.setRealPrice(originalProduct.getPrice());
//        receiptProduct.setRealUnit(originalProduct.getUnit());
//        receiptProduct.setRealImage(originalProduct.getImage());
//
//        // Set receipt reference
//        receiptProduct.setSampleReceipt(receipt);
//
//        receiptProducts.add(receiptProduct);
//    }
//
//    // Set receipt products
//    receipt.setReceiptProducts(receiptProducts);
//
//    // Save receipt
//    sampleReceiptRepository.save(receipt);
//
//    return "redirect:/receipts?success";
//}

    // ตัวแปลงค่าจาก String (ID) เป็น Company
    @ModelAttribute
    public void addAttributes(Model model) {
        model.addAttribute("companies", companyRepository.findAll());
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(Company.class, new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) throws IllegalArgumentException {
                if (text == null || text.isEmpty()) {
                    setValue(null);
                } else {
                    Optional<Company> company = companyRepository.findById(Long.parseLong(text));
                    setValue(company.orElse(null));
                }
            }
        });
    }

    // other CRUD methods
}

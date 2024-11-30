package com.cp.kku.demo.controller;



import java.beans.PropertyEditorSupport;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.cp.kku.demo.model.Company;
import com.cp.kku.demo.model.Product;
import com.cp.kku.demo.model.ReceiptProduct;
import com.cp.kku.demo.repository.ProductRepository;
import com.cp.kku.demo.repository.ReceiptProductRepository;
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
    private final ReceiptProductRepository receiptProductRepository;

    @Autowired
    public SampleReceiptController(SampleReceiptRepository sampleReceiptRepository, CompanyRepository companyRepository, ProductRepository productRepository, ReceiptProductRepository receiptProductRepository) {
        this.sampleReceiptRepository = sampleReceiptRepository;
        this.companyRepository = companyRepository;
        this.productRepository = productRepository;
        this.receiptProductRepository = receiptProductRepository;
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

@PostMapping
public String saveReceipt(@ModelAttribute SampleReceipt sampleReceipt) {
    // พิมพ์ข้อมูล Company
    System.out.println("Company Name: " + companyRepository.findById(sampleReceipt.getCompany().getId()).get().getCompanyName());
//    for (ReceiptProduct receiptProduct : sampleReceipt.getReceiptProducts()) {
//        receiptProduct.setSampleReceipt(sampleReceipt); // ตั้งค่าความสัมพันธ์กับ SampleReceipt
//    }
    System.out.println(sampleReceipt.getSupplierName());
    if(sampleReceipt.getSupplierName().isEmpty()) {
        String supplierName = companyRepository.findByCompanyName(companyRepository.findById(sampleReceipt.getCompany().getId()).get().getCompanyName()).getRepresentativeName();
        sampleReceipt.setSupplierName(supplierName);
    }

    // พิมพ์ข้อมูลสินค้า
    if (sampleReceipt.getReceiptProducts() != null && !sampleReceipt.getReceiptProducts().isEmpty()) {
        System.out.println("Selected Products:");
        for (ReceiptProduct receiptProduct : sampleReceipt.getReceiptProducts()) {

            // ตั้งค่า SampleReceipt ให้กับ ReceiptProduct แต่ละตัว
            receiptProduct.setSampleReceipt(sampleReceipt);

            System.out.println("Product Name: " + receiptProduct.getRealProductName() +
                    ", Product Code: " + receiptProduct.getRealProductCode() +
                    ", Quantity: " + receiptProduct.getRealQuantity()+
                    ", Price: " + receiptProduct.getRealPrice());
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

    @GetMapping("/edit/{id}")
    public String editReceiptForm(@PathVariable Long id, Model model) {
        SampleReceipt receipt = sampleReceiptRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Receipt not found"));
        model.addAttribute("receipt", receipt);
        model.addAttribute("companies", companyRepository.findAll());
        return "receipt-form";
    }

    @GetMapping("/delete/{id}")
    public String deleteReceipt(@PathVariable Long id) {
        sampleReceiptRepository.deleteById(id);
        return "redirect:/receipts";
    }

    @GetMapping("/view/{id}")
    public String viewReceipt(@PathVariable Long id, Model model) {
        System.out.println(receiptProductRepository.findProductIdBySampleReceiptId(id));
//        SampleReceipt receipt = sampleReceiptRepository.findById(id).get();

        // หาผลิตภัณฑ์ที่เกี่ยวข้องกับใบเสร็จ
        List<String> ids = receiptProductRepository.findProductIdBySampleReceiptId(id);
        List<ReceiptProduct> receiptProducts = new ArrayList<>();
        // แสดงข้อมูลใน Console
        for (String i : ids) {
            System.out.println(i);
            receiptProducts.add(receiptProductRepository.findById(Long.parseLong(i)).get());
//            products.add(productRepository.findById(Long.parseLong(i)).get());
//            System.out.println(productRepository.findById(Long.parseLong(i)).get().getProductName());
        }

        model.addAttribute("receiptProducts", receiptProducts);

        // สามารถดึงข้อมูล SampleReceipt ด้วย id ได้เช่นกันถ้าต้องการ
        SampleReceipt receipt = sampleReceiptRepository.findById(id).orElse(null);
        model.addAttribute("receipt", receipt);
        return "receipt-details";
    }
}

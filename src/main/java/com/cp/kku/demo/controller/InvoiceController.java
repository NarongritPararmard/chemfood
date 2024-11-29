package com.cp.kku.demo.controller;


import java.util.List;

import com.cp.kku.demo.model.InvoiceProduct;
import com.cp.kku.demo.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.cp.kku.demo.model.Invoice;
import com.cp.kku.demo.repository.CompanyRepository;
import com.cp.kku.demo.repository.InvoiceRepository;

@Controller
@RequestMapping("/invoices")
public class InvoiceController {

    private final InvoiceRepository invoiceRepository;
    private final CompanyRepository companyRepository;
    private final ProductRepository productRepository;

    @Autowired
    public InvoiceController(InvoiceRepository invoiceRepository, CompanyRepository companyRepository, ProductRepository productRepository) {
        this.invoiceRepository = invoiceRepository;
        this.companyRepository = companyRepository;
        this.productRepository = productRepository;
    }

    @GetMapping
    public String getAllInvoices(Model model) {
        List<Invoice> invoices = invoiceRepository.findAll();
        model.addAttribute("invoices", invoices);
        return "invoice-list";
    }

    @GetMapping("/new")
    public String createInvoiceForm(Model model) {
        model.addAttribute("invoice", new Invoice());
        model.addAttribute("companies", companyRepository.findAll());
        return "invoice-form";
    }

//    @PostMapping
//    public String saveInvoice(@ModelAttribute Invoice invoice) {
//        invoiceRepository.save(invoice);
//        return "redirect:/invoices";
//    }

    @PostMapping
    public String saveReceipt(@ModelAttribute Invoice invoice) {
        // พิมพ์ข้อมูล Company
        System.out.println("Company Name: " + companyRepository.findById(invoice.getCompany().getId()).get().getCompanyName());
//    for (ReceiptProduct receiptProduct : sampleReceipt.getReceiptProducts()) {
//        receiptProduct.setSampleReceipt(sampleReceipt); // ตั้งค่าความสัมพันธ์กับ SampleReceipt
//    }

        // พิมพ์ข้อมูลสินค้า
        if (invoice.getReceiptProducts() != null && !invoice.getReceiptProducts().isEmpty()) {
            System.out.println("Selected Products:");
            for (InvoiceProduct invoiceProduct : invoice.getReceiptProducts()) {

                // ตั้งค่า SampleReceipt ให้กับ ReceiptProduct แต่ละตัว
                invoiceProduct.setInvoice(invoice);

                System.out.println("Product Name: " + invoiceProduct.getRealProductName() +
                        ", Product Code: " + invoiceProduct.getRealProductCode() +
                        ", Quantity: " + invoiceProduct.getRealQuantity()+
                        ", Price: " + invoiceProduct.getRealPrice());
            }
        } else {
            System.out.println("No products selected.");
        }

        // ตั้งค่า Company Name ก่อนบันทึก
        invoice.setCompanyName(companyRepository.findById(invoice.getCompany().getId()).get().getCompanyName());

        // บันทึกข้อมูล
        invoiceRepository.save(invoice);

        return "redirect:/invoices";
    }

    // other CRUD methods
}

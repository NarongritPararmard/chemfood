package com.cp.kku.demo.controller;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import com.cp.kku.demo.model.*;
import com.cp.kku.demo.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/invoices")
public class InvoiceController {

    private final InvoiceRepository invoiceRepository;
    private final CompanyRepository companyRepository;
    private final ProductRepository productRepository;
    private final InvoiceProductRepository invoiceProductRepository;

    @Autowired
    public InvoiceController(InvoiceRepository invoiceRepository, CompanyRepository companyRepository, ProductRepository productRepository, InvoiceProductRepository invoiceProductRepository, InvoiceProductRepository invoiceProductRepository1) {
        this.invoiceRepository = invoiceRepository;
        this.companyRepository = companyRepository;
        this.productRepository = productRepository;
        this.invoiceProductRepository = invoiceProductRepository1;
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
        List<Company> companies = companyRepository.findAll().stream()
                .filter(company -> "INVOICE".equals(company.getStatus()))
                .collect(Collectors.toList());
        model.addAttribute("companies", companies);
        return "invoice-form";
    }

    @PostMapping
    public String saveReceipt(@ModelAttribute Invoice invoice) {
        if(invoice.getCustomerName().isEmpty()) {
            String customerName = companyRepository.findByCompanyName(companyRepository.findById(invoice.getCompany().getId()).get().getCompanyName()).getRepresentativeName();
            invoice.setCustomerName(customerName);
        }

        if (invoice.getContact().isEmpty()) {
            String contact = companyRepository.findByCompanyName(companyRepository.findById(invoice.getCompany().getId()).get().getCompanyName()).getContact();
            invoice.setContact(contact);
        }

        if (invoice.getDate() == null) {
            invoice.setDate(new Date());
        }
        // พิมพ์ข้อมูลสินค้า
        if (invoice.getInvoiceProducts() != null && !invoice.getInvoiceProducts().isEmpty()) {
            System.out.println("Selected Products:");
            for (InvoiceProduct invoiceProduct : invoice.getInvoiceProducts()) {

                // ตั้งค่า Invoice ให้กับ InvoiceProduct แต่ละตัว
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

    @GetMapping("/delete/{id}")
    public String deleteInvoice(@PathVariable Long id) {
        invoiceRepository.deleteById(id);
        return "redirect:/invoices";
    }

    @GetMapping("/view/{id}")
    public String viewInvoice(@PathVariable Long id, Model model) {

        System.out.println(invoiceProductRepository.findProductIdByInvoiceId(id));

        // หาผลิตภัณฑ์ที่เกี่ยวข้องกับใบเสร็จ
        List<String> ids = invoiceProductRepository.findProductIdByInvoiceId(id);
        List<InvoiceProduct> invoiceProducts = new ArrayList<>();
        // แสดงข้อมูลใน Console
        for (String i : ids) {
            System.out.println(i);
            invoiceProducts.add(invoiceProductRepository.findById(Long.parseLong(i)).get());
        }

        model.addAttribute("invoiceProducts", invoiceProducts);


        Invoice invoice = invoiceRepository.findById(id).orElse(null);
        model.addAttribute("invoice", invoice);
        return "invoice-details";
    }

    // other CRUD methods
}

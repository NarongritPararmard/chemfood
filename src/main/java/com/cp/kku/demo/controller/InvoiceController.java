package com.cp.kku.demo.controller;


import java.util.List;

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

    @Autowired
    public InvoiceController(InvoiceRepository invoiceRepository, CompanyRepository companyRepository) {
        this.invoiceRepository = invoiceRepository;
        this.companyRepository = companyRepository;
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

    @PostMapping
    public String saveInvoice(@ModelAttribute Invoice invoice) {
        invoiceRepository.save(invoice);
        return "redirect:/invoices";
    }

    // other CRUD methods
}

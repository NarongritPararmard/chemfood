package com.cp.kku.demo.controller;



import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.cp.kku.demo.model.SampleReceipt;
import com.cp.kku.demo.repository.CompanyRepository;
import com.cp.kku.demo.repository.SampleReceiptRepository;

@Controller
@RequestMapping("/receipts")
public class SampleReceiptController {

    private final SampleReceiptRepository sampleReceiptRepository;
    private final CompanyRepository companyRepository;

    @Autowired
    public SampleReceiptController(SampleReceiptRepository sampleReceiptRepository, CompanyRepository companyRepository) {
        this.sampleReceiptRepository = sampleReceiptRepository;
        this.companyRepository = companyRepository;
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
        sampleReceiptRepository.save(sampleReceipt);
        return "redirect:/receipts";
    }

    // other CRUD methods
}

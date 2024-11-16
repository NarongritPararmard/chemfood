package com.cp.kku.demo.controller;



import java.beans.PropertyEditorSupport;
import java.util.List;
import java.util.Optional;

import com.cp.kku.demo.model.Company;
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

    @PostMapping
    public String saveReceipt(@ModelAttribute SampleReceipt sampleReceipt) {
        sampleReceipt.setCompanyName(companyRepository.findById(sampleReceipt.getCompany().getId()).get().getCompanyName());
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
}

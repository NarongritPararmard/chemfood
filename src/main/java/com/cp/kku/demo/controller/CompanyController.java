package com.cp.kku.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.cp.kku.demo.model.Company;
import com.cp.kku.demo.service.CompanyService;

@Controller
@RequestMapping("/companies")
public class CompanyController {

    private final CompanyService companyService;

    @Autowired
    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    @GetMapping
    public String getAllCompanies(Model model) {
        List<Company> companies = companyService.getAllCompanies();
        model.addAttribute("companies", companies);
        return "company-list";
    }

    @GetMapping("/new")
    public String createCompanyForm(Model model) {
        model.addAttribute("company", new Company());
        return "company-form";
    }

    @PostMapping
    public String saveCompany(@ModelAttribute Company company) {
        companyService.saveCompany(company);
        return "redirect:/companies";
    }

    // Example: Custom method to show active companies
//    @GetMapping("/active")
//    public String getActiveCompanies(Model model) {
//        List<Company> activeCompanies = companyService.findCompaniesByStatus("ACTIVE");
//        model.addAttribute("companies", activeCompanies);
//        return "company-list";
//    }

    // Update form
    @GetMapping("/edit/{id}")
    public String editCompanyForm(@PathVariable Long id, Model model) {
        Company company = companyService.getCompanyById(id).orElseThrow(() -> new IllegalArgumentException("Company with ID " + id + " not found."));
        model.addAttribute("company", company);
        return "company-edit";
    }

    // Update logic
    @PostMapping("/update/{id}")
    public String updateCompany(@PathVariable Long id, @ModelAttribute Company company) {
        companyService.updateCompany(id, company);
        return "redirect:/companies";
    }

    // Delete logic
    @GetMapping("/delete/{id}")
    public String deleteCompany(@PathVariable Long id) {
        companyService.deleteCompany(id);
        return "redirect:/companies";
    }

}

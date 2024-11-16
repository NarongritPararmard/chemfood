package com.cp.kku.demo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cp.kku.demo.model.Company;
import com.cp.kku.demo.repository.CompanyRepository;

@Service
public class CompanyService {

    private final CompanyRepository companyRepository;

    @Autowired
    public CompanyService(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    // Fetch all companies
    public List<Company> getAllCompanies() {
        return companyRepository.findAll();
    }

    // Save or update a company
    public Company saveCompany(Company company) {
        return companyRepository.save(company);
    }

    // Update a company
//    public Company updateCompany(Long id, Company updatedCompany) {
//        updatedCompany.setId(id);
//        return companyRepository.save(updatedCompany);
//    }
    public Company updateCompany(Long id, Company updatedCompany) {
        return companyRepository.findById(id).map(existingCompany -> {
            // อัปเดตฟิลด์ทั่วไป
            existingCompany.setCompanyName(updatedCompany.getCompanyName());
            existingCompany.setRepresentativeName(updatedCompany.getRepresentativeName());
            existingCompany.setAddress(updatedCompany.getAddress());
            existingCompany.setContact(updatedCompany.getContact());
            existingCompany.setTaxId(updatedCompany.getTaxId());
            existingCompany.setWebsite(updatedCompany.getWebsite());
            existingCompany.setStatus(updatedCompany.getStatus());

            // จัดการคอลเลกชัน invoices
            existingCompany.getInvoices().clear(); // ลบรายการเก่า
            if (updatedCompany.getInvoices() != null) {
                existingCompany.getInvoices().addAll(updatedCompany.getInvoices());
                // เชื่อมโยง Invoice กลับมาที่ Company
                updatedCompany.getInvoices().forEach(invoice -> invoice.setCompany(existingCompany));
            }

            return companyRepository.save(existingCompany);
        }).orElseThrow(() -> new IllegalArgumentException("ไม่พบ Company ที่มี ID " + id));
    }

    // Fetch a company by ID
    public Optional<Company> getCompanyById(Long id) {
        return companyRepository.findById(id);
    }

    // Delete a company by ID
    public void deleteCompany(Long id) {
        companyRepository.deleteById(id);
    }

    // Custom method: Find companies by status
    public List<Company> findCompaniesByStatus(String status) {
        return companyRepository.findByStatus(status);
    }
}

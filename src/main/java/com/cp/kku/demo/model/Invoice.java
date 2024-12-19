package com.cp.kku.demo.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import jakarta.persistence.*;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "invoice")
public class Invoice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String companyName;
    private String customerName;
    private String contact;

    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date date;

    private double totalPrice;
    private int totalAmount;

    @ManyToOne
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    @OneToMany(mappedBy = "invoice", cascade = CascadeType.ALL)
    private List<InvoiceProduct> invoiceProducts;

    // Getters and Setters


    public Company getCompany() {
        return company;
    }

    public Date getDate() {
        return date;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public int getTotalAmount() {
        return totalAmount;
    }

    public Long getId() {
        return id;
    }

    public String getCompanyName() {
        return companyName;
    }

    public String getContact() {
        return contact;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setCustomerName(String supplierName) {
        this.customerName = supplierName;
    }

    public void setTotalAmount(int totalAmount) {
        this.totalAmount = totalAmount;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public List<InvoiceProduct> getInvoiceProducts() {
        return invoiceProducts;
    }

    public void setInvoiceProducts(List<InvoiceProduct> invoiceProducts) {
        this.invoiceProducts = invoiceProducts;
    }
}


package com.cp.kku.demo.model;

import jakarta.persistence.*;

@Entity
public class InvoiceProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @ManyToOne
    @JoinColumn(name = "invoice_id", nullable = false)
    private Invoice invoice;

    @Column(name = "real_product_code")
    private String realProductCode;

    @Column(name = "real_product_name")
    private String realProductName;

    @Column(name = "real_description")
    private String realDescription;

    @Column(name = "real_price")
    private double realPrice;

    @Column(name = "real_quantity")
    private int realQuantity;

    @Column(name = "real_unit")
    private String realUnit;

    @Column(name = "real_image")
    private String realImage;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public Invoice getInvoice() {
        return invoice;
    }

    public Product getProduct() {
        return product;
    }

    public double getRealPrice() {
        return realPrice;
    }

    public int getRealQuantity() {
        return realQuantity;
    }

    public String getRealDescription() {
        return realDescription;
    }

    public String getRealImage() {
        return realImage;
    }

    public String getRealProductCode() {
        return realProductCode;
    }

    public String getRealProductName() {
        return realProductName;
    }

    public void setInvoice(Invoice invoice) {
        this.invoice = invoice;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public String getRealUnit() {
        return realUnit;
    }

    public void setRealDescription(String realDescription) {
        this.realDescription = realDescription;
    }

    public void setRealImage(String realImage) {
        this.realImage = realImage;
    }

    public void setRealPrice(double realPrice) {
        this.realPrice = realPrice;
    }

    public void setRealProductCode(String realProductCode) {
        this.realProductCode = realProductCode;
    }

    public void setRealProductName(String realProductName) {
        this.realProductName = realProductName;
    }

    public void setRealQuantity(int realQuantity) {
        this.realQuantity = realQuantity;
    }

    public void setRealUnit(String realUnit) {
        this.realUnit = realUnit;
    }
}

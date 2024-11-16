package com.cp.kku.demo.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class ReceiptProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @ManyToOne
    @JoinColumn(name = "sample_receipt_id", nullable = false)
    private SampleReceipt sampleReceipt;

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

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public SampleReceipt getSampleReceipt() {
        return sampleReceipt;
    }

    public void setSampleReceipt(SampleReceipt sampleReceipt) {
        this.sampleReceipt = sampleReceipt;
    }

    public String getRealProductCode() {
        return realProductCode;
    }

    public void setRealProductCode(String realProductCode) {
        this.realProductCode = realProductCode;
    }

    public String getRealProductName() {
        return realProductName;
    }

    public void setRealProductName(String realProductName) {
        this.realProductName = realProductName;
    }

    public String getRealDescription() {
        return realDescription;
    }

    public void setRealDescription(String realDescription) {
        this.realDescription = realDescription;
    }

    public double getRealPrice() {
        return realPrice;
    }

    public void setRealPrice(double realPrice) {
        this.realPrice = realPrice;
    }

    public int getRealQuantity() {
        return realQuantity;
    }

    public void setRealQuantity(int realQuantity) {
        this.realQuantity = realQuantity;
    }

    public String getRealUnit() {
        return realUnit;
    }

    public void setRealUnit(String realUnit) {
        this.realUnit = realUnit;
    }

    public String getRealImage() {
        return realImage;
    }

    public void setRealImage(String realImage) {
        this.realImage = realImage;
    }
}

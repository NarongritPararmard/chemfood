package com.cp.kku.demo.service;

import com.cp.kku.demo.model.Product;
import com.cp.kku.demo.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    // Save the product to the database
    public void saveProduct(Product product) {
        productRepository.save(product);
    }
}
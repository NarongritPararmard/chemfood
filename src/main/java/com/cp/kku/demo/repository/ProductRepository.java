package com.cp.kku.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cp.kku.demo.model.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {
}

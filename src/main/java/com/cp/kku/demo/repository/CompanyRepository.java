package com.cp.kku.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cp.kku.demo.model.Company;

import java.util.List;

public interface CompanyRepository extends JpaRepository<Company, Long> {
    List<Company> findByStatus(String status);
}
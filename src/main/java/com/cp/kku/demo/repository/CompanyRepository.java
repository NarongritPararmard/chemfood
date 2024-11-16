package com.cp.kku.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cp.kku.demo.model.Company;

public interface CompanyRepository extends JpaRepository<Company, Long> {
}
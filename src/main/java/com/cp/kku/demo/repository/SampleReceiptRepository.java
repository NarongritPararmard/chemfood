package com.cp.kku.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cp.kku.demo.model.SampleReceipt;

public interface SampleReceiptRepository extends JpaRepository<SampleReceipt, Long> {

}
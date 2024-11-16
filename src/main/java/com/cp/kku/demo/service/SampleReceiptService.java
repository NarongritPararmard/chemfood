package com.cp.kku.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cp.kku.demo.model.SampleReceipt;
import com.cp.kku.demo.repository.SampleReceiptRepository;

@Service
public class SampleReceiptService {

    private final SampleReceiptRepository sampleReceiptRepository;

    @Autowired
    public SampleReceiptService(SampleReceiptRepository sampleReceiptRepository) {
        this.sampleReceiptRepository = sampleReceiptRepository;
    }

    public List<SampleReceipt> getAllReceipts() {
        return sampleReceiptRepository.findAll();
    }

    public SampleReceipt saveReceipt(SampleReceipt sampleReceipt) {
        return sampleReceiptRepository.save(sampleReceipt);
    }

    // other CRUD methods
}

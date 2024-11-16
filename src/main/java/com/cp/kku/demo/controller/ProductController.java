package com.cp.kku.demo.controller;



import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import com.cp.kku.demo.model.Product;
import com.cp.kku.demo.repository.ProductRepository;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/products")
public class ProductController {

    private final ProductRepository productRepository;

    // ตัวแปรสำหรับเก็บที่อยู่ของไดเร็กทอรีที่ใช้เก็บไฟล์
    private final String uploadDir = "src/main/resources/static/uploads/";

    @Autowired
    public ProductController(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @GetMapping
    public String getAllProducts(Model model) {
        List<Product> products = productRepository.findAll();
        model.addAttribute("products", products);
        return "product-list";
    }

    @GetMapping("/new")
    public String createProductForm(Model model) {
        model.addAttribute("product", new Product());
        return "product-form";
    }

//    @PostMapping
//    public String saveProduct(@ModelAttribute Product product) {
//        productRepository.save(product);
//        return "redirect:/products";
//    }

    @PostMapping
    public String saveProduct(@ModelAttribute Product product) throws IOException {
        // ตรวจสอบว่าไฟล์ที่อัปโหลดมีอยู่
        if (product.getImageFile() != null && !product.getImageFile().isEmpty()) {
            // บันทึกไฟล์ไปยังที่เก็บ
            String fileName = product.getImageFile().getOriginalFilename();
            Path path = Paths.get(uploadDir, fileName);
            Files.copy(product.getImageFile().getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
            // ตั้งค่าภาพที่เก็บในฐานข้อมูลเป็นชื่อไฟล์
            product.setImage(fileName);
        }

        productRepository.save(product);
        return "redirect:/products";
    }

    // other CRUD methods

}

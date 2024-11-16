package com.cp.kku.demo.controller;



import java.io.File;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import com.cp.kku.demo.model.Product;
import com.cp.kku.demo.repository.ProductRepository;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/products")
public class ProductController {

    private final ProductRepository productRepository;

    // ตัวแปรสำหรับเก็บที่อยู่ของไดเร็กทอรีที่ใช้เก็บไฟล์
//    private final String uploadDir = "src/main/resources/static/uploads/";

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

    @Value("${upload.dir}") // รับค่าจาก application.properties
    private String uploadDir;

    @PostMapping
    public String saveProduct(@ModelAttribute Product product, @RequestParam("imageFile") MultipartFile imageFile, RedirectAttributes redirectAttributes) throws IOException {
        File dir = new File(uploadDir);
        if (!dir.exists()) {
            dir.mkdirs(); // สร้างโฟลเดอร์ถ้ายังไม่มี
        }

        // กำหนดชื่อไฟล์และที่เก็บไฟล์
        String originalFilename = StringUtils.cleanPath(imageFile.getOriginalFilename());
        Path targetLocation = Paths.get(uploadDir + "/" + originalFilename);
        Files.copy(imageFile.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

        // กำหนดค่าภาพให้กับ Product
        product.setImage(originalFilename);
        productRepository.save(product);

        return "redirect:/products";
    }


    // other CRUD methods

}

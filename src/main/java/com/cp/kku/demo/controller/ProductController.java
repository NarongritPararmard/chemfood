package com.cp.kku.demo.controller;



import java.io.File;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;
import jakarta.validation.Valid;


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
    public String saveProduct(@ModelAttribute Product product,
                              @RequestParam(value = "imageFile", required = false) MultipartFile imageFile,
                              RedirectAttributes redirectAttributes) throws IOException {
        // ตรวจสอบว่า imageFile มีค่าเป็นไฟล์หรือไม่
        if (imageFile != null && !imageFile.isEmpty()) {
            // ถ้ามีการเลือกไฟล์ ให้ทำการอัปโหลดไฟล์
            File dir = new File(uploadDir);
            if (!dir.exists()) {
                dir.mkdirs();  // สร้างโฟลเดอร์ถ้ายังไม่มี
            }

            // กำหนดชื่อไฟล์และที่เก็บไฟล์
            String originalFilename = StringUtils.cleanPath(imageFile.getOriginalFilename());
            Path targetLocation = Paths.get(uploadDir + "/" + originalFilename);
            Files.copy(imageFile.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            // กำหนดค่าภาพให้กับ Product
            product.setImage(originalFilename);
        }

        // บันทึกข้อมูลสินค้าในฐานข้อมูล
        productRepository.save(product);

        // รีไดเรคไปยังหน้ารายการสินค้า
        return "redirect:/products";
    }


    @RequestMapping("/delete/{id}")
    public String deleteProduct(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            productRepository.deleteById(id);  // ลบสินค้าจากฐานข้อมูล
            redirectAttributes.addFlashAttribute("message", "Product deleted successfully");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error deleting product");
        }
        return "redirect:/products";  // หลังจากลบแล้วให้ redirect กลับไปที่หน้าสินค้า
    }

    // แสดงฟอร์มแก้ไขข้อมูลสินค้า
    @GetMapping("/edit/{id}")
    public String editProduct(@PathVariable("id") Long id, Model model) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid product Id:" + id));
        model.addAttribute("product", product);
        return "product-edit";
    }

    // บันทึกการแก้ไขข้อมูลสินค้า
    @PostMapping("/update/{id}")
    public String saveProduct(@PathVariable("id") Long id,@ModelAttribute Product product,
                              @RequestParam(value = "imageFile", required = false) MultipartFile imageFile,
                              RedirectAttributes redirectAttributes) throws IOException {
        // ตรวจสอบว่า imageFile มีค่าเป็นไฟล์หรือไม่
        if (imageFile != null && !imageFile.isEmpty()) {
            // ถ้ามีการเลือกไฟล์ ให้ทำการอัปโหลดไฟล์
            File dir = new File(uploadDir);
            if (!dir.exists()) {
                dir.mkdirs();  // สร้างโฟลเดอร์ถ้ายังไม่มี
            }

            // กำหนดชื่อไฟล์และที่เก็บไฟล์
            String originalFilename = StringUtils.cleanPath(imageFile.getOriginalFilename());
            Path targetLocation = Paths.get(uploadDir + "/" + originalFilename);
            Files.copy(imageFile.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            // กำหนดค่าภาพให้กับ Product
            product.setImage(originalFilename);
            product.setId(id);
        }

        // บันทึกข้อมูลสินค้าในฐานข้อมูล
        productRepository.save(product);

        // รีไดเรคไปยังหน้ารายการสินค้า
        return "redirect:/products";
    }

    // ฟังก์ชั่นสำหรับบันทึกภาพที่อัพโหลด
    private String saveImage(MultipartFile imageFile) throws IOException {
        String uploadDir = "src/main/resources/static/uploads/";
        File uploadDirFile = new File(uploadDir);
        if (!uploadDirFile.exists()) {
            uploadDirFile.mkdir();
        }

        String fileName = UUID.randomUUID().toString() + "_" + imageFile.getOriginalFilename();
        Path path = uploadDirFile.toPath().resolve(fileName);
        Files.copy(imageFile.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
        return fileName;
    }




    // other CRUD methods

}

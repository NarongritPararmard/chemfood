package com.cp.kku.demo.controller;



import java.beans.PropertyEditorSupport;
import java.time.LocalDate;
import java.util.*;

import com.cp.kku.demo.model.*;
import com.cp.kku.demo.repository.ProductRepository;
import com.cp.kku.demo.repository.ReceiptProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import com.cp.kku.demo.repository.CompanyRepository;
import com.cp.kku.demo.repository.SampleReceiptRepository;

@Controller
@RequestMapping("/receipts")
public class SampleReceiptController {

    private final SampleReceiptRepository sampleReceiptRepository;
    private final CompanyRepository companyRepository;
    private final ProductRepository productRepository;
    private final ReceiptProductRepository receiptProductRepository;

    @Autowired
    public SampleReceiptController(SampleReceiptRepository sampleReceiptRepository, CompanyRepository companyRepository, ProductRepository productRepository, ReceiptProductRepository receiptProductRepository) {
        this.sampleReceiptRepository = sampleReceiptRepository;
        this.companyRepository = companyRepository;
        this.productRepository = productRepository;
        this.receiptProductRepository = receiptProductRepository;
    }

    @GetMapping
    public String getAllReceipts(Model model) {
        List<SampleReceipt> receipts = sampleReceiptRepository.findAll();
        model.addAttribute("receipts", receipts);
        return "receipt-list";
    }

    @GetMapping("/new")
    public String createReceiptForm(Model model) {
        model.addAttribute("receipt", new SampleReceipt());
        model.addAttribute("companies", companyRepository.findAll());
        return "receipt-form";
    }

@PostMapping
public String saveReceipt(@ModelAttribute SampleReceipt sampleReceipt) {
    // พิมพ์ข้อมูล Company
    System.out.println("Company Name: " + companyRepository.findById(sampleReceipt.getCompany().getId()).get().getCompanyName());
//    for (ReceiptProduct receiptProduct : sampleReceipt.getReceiptProducts()) {
//        receiptProduct.setSampleReceipt(sampleReceipt); // ตั้งค่าความสัมพันธ์กับ SampleReceipt
//    }
//    System.out.println(sampleReceipt.getSupplierName());
    if(sampleReceipt.getSupplierName().isEmpty()) {
        String supplierName = companyRepository.findByCompanyName(companyRepository.findById(sampleReceipt.getCompany().getId()).get().getCompanyName()).getRepresentativeName();
        sampleReceipt.setSupplierName(supplierName);
    }

    if (sampleReceipt.getContact().isEmpty()) {
        String contact = companyRepository.findByCompanyName(companyRepository.findById(sampleReceipt.getCompany().getId()).get().getCompanyName()).getContact();
        sampleReceipt.setContact(contact);
    }

    if (sampleReceipt.getDate() == null) {
        sampleReceipt.setDate(new Date());
    }

    // พิมพ์ข้อมูลสินค้า
    if (sampleReceipt.getReceiptProducts() != null && !sampleReceipt.getReceiptProducts().isEmpty()) {
        System.out.println("Selected Products:");
        for (ReceiptProduct receiptProduct : sampleReceipt.getReceiptProducts()) {

            // ตั้งค่า SampleReceipt ให้กับ ReceiptProduct แต่ละตัว
            receiptProduct.setSampleReceipt(sampleReceipt);

            System.out.println("Product Name: " + receiptProduct.getRealProductName() +
                    ", Product Code: " + receiptProduct.getRealProductCode() +
                    ", Quantity: " + receiptProduct.getRealQuantity()+
                    ", Price: " + receiptProduct.getRealPrice());
        }
    } else {
        System.out.println("No products selected.");
    }

    // ตั้งค่า Company Name ก่อนบันทึก
    sampleReceipt.setCompanyName(companyRepository.findById(sampleReceipt.getCompany().getId()).get().getCompanyName());

    // บันทึกข้อมูล
    sampleReceiptRepository.save(sampleReceipt);

    return "redirect:/receipts";
}

    // ตัวแปลงค่าจาก String (ID) เป็น Company
    @ModelAttribute
    public void addAttributes(Model model) {
        model.addAttribute("companies", companyRepository.findAll());
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(Company.class, new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) throws IllegalArgumentException {
                if (text == null || text.isEmpty()) {
                    setValue(null);
                } else {
                    Optional<Company> company = companyRepository.findById(Long.parseLong(text));
                    setValue(company.orElse(null));
                }
            }
        });
    }

    // other CRUD methods
    @GetMapping("/edit/{id}")
    public String editReceiptForm(@PathVariable Long id, Model model) {

        // ดึงข้อมูลใบเสร็จที่ต้องการแก้ไข
        SampleReceipt receipt = sampleReceiptRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Receipt not found"));

        // ตรวจสอบว่า receiptProducts เป็น null หรือไม่ ถ้าเป็น null จะสร้างใหม่เป็น ArrayList
        if (receipt.getReceiptProducts() == null) {
            receipt.setReceiptProducts(new ArrayList<>());
        }

        // ตรวจสอบว่ามีสินค้าผลิตภัณฑ์ในใบเสร็จหรือไม่
        if (receipt.getReceiptProducts().size() > 1) {
            System.out.println(receipt.getReceiptProducts().get(1).getRealProductCode());
        } else {
            System.out.println("No second product available");
        }

        // เพิ่มข้อมูลลงในโมเดล
        model.addAttribute("receipt", receipt);
        model.addAttribute("companies", companyRepository.findAll());
        model.addAttribute("receiptProducts", receipt.getReceiptProducts());  // เพิ่มสินค้าของใบเสร็จลงในโมเดล

        return "receipt-edit";
    }


    @PostMapping("/edit/{id}")
    public String updateReceipt(
            @PathVariable Long id,
            @RequestParam String companyName,
            @RequestParam String supplierName,
            @RequestParam String contact,
            @RequestParam String date,
            @RequestParam double totalPrice,
            @RequestParam double totalAmount,
            @RequestParam List<String> realProductNames,
            @RequestParam List<String> realDescriptions,
            @RequestParam List<Double> realPrices,
            @RequestParam List<Integer> realQuantities,
            @RequestParam List<String> realUnits,
            @RequestParam List<String> realImages) {

        // Print the received data for debugging
        System.out.println("Updating Receipt ID: " + id);
        System.out.println("Company Name: " + companyName);
        System.out.println("Supplier Name: " + supplierName);
        System.out.println("Contact: " + contact);
        System.out.println("Date: " + date);
        System.out.println("Total Price: " + totalPrice);
        System.out.println("Total Amount: " + totalAmount);
        System.out.println("Product Names: " + realProductNames);
        System.out.println("Descriptions: " + realDescriptions);
        System.out.println("Prices: " + realPrices);
        System.out.println("Quantities: " + realQuantities);
        System.out.println("Units: " + realUnits);
        System.out.println("Images: " + realImages);

        // Update the receipt data
//        Receipt updatedReceipt = receiptService.updateReceipt(id, companyName, supplierName, contact, date, totalPrice, totalAmount);

        // Update the receipt products
        for (int i = 0; i < realProductNames.size(); i++) {
            ReceiptProduct product = new ReceiptProduct();
            product.setRealProductName(realProductNames.get(i));
            product.setRealDescription(realDescriptions.get(i));
            product.setRealPrice(realPrices.get(i));
            product.setRealQuantity(realQuantities.get(i));
            product.setRealUnit(realUnits.get(i));
            product.setRealImage(realImages.get(i));  // Assuming images are stored by path
//            product.setReceipt(updatedReceipt);

            // Save the product information
//            receiptService.saveProduct(product);
        }

        // Redirect to the receipt list page after update
        return "redirect:/receipts";
    }


//    @PostMapping("/edit/{id}")
//    public String saveReceipt(@ModelAttribute ReceiptForm receiptForm) {
//        // Handle saving the receipt and products data
//        // Save receipt and products to database
//        return "redirect:/receipts"; // Redirect to receipts list or success page
//    }


    @GetMapping("/delete/{id}")
    public String deleteReceipt(@PathVariable Long id) {
        sampleReceiptRepository.deleteById(id);
        return "redirect:/receipts";
    }

    @GetMapping("/view/{id}")
    public String viewReceipt(@PathVariable Long id, Model model) {
        System.out.println(receiptProductRepository.findProductIdBySampleReceiptId(id));
//        SampleReceipt receipt = sampleReceiptRepository.findById(id).get();

        // หาผลิตภัณฑ์ที่เกี่ยวข้องกับใบเสร็จ
        List<String> ids = receiptProductRepository.findProductIdBySampleReceiptId(id);
        List<ReceiptProduct> receiptProducts = new ArrayList<>();
        // แสดงข้อมูลใน Console
        for (String i : ids) {
            System.out.println(i);
            receiptProducts.add(receiptProductRepository.findById(Long.parseLong(i)).get());

//            products.add(productRepository.findById(Long.parseLong(i)).get());
//            System.out.println(productRepository.findById(Long.parseLong(i)).get().getProductName());
        }

        model.addAttribute("receiptProducts", receiptProducts);

        // สามารถดึงข้อมูล SampleReceipt ด้วย id ได้เช่นกันถ้าต้องการ
        SampleReceipt receipt = sampleReceiptRepository.findById(id).orElse(null);
        model.addAttribute("receipt", receipt);
        return "receipt-details";
    }
}

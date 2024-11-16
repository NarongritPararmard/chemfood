package com.cp.kku.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    @GetMapping("/")
    public String index(Model model) {
        // สามารถส่งข้อมูลไปยังหน้า index.html ได้ (ตัวอย่างส่งชื่อผู้ใช้)
        model.addAttribute("username", "ผู้ใช้งาน");
        return "index";  // ชื่อไฟล์ที่ใช้เป็นชื่อหน้า HTML (index.html)
    }
}

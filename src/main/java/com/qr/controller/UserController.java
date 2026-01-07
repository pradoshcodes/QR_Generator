package com.qr.controller;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.qr.entity.User;
import com.qr.repository.UserRepository;
import com.qr.service.QrService;

@Controller
public class UserController {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private QrService qrService;

    // Home → Create User Page
    @GetMapping("/")
    public String home() {
        return "create-user";
    }

    // Create User
    @PostMapping("/create-user")
    public String createUser(@RequestParam String name, Model model) {
        User user = new User();
        user.setName(name);
        user.setQrToken(UUID.randomUUID().toString());

        userRepo.save(user);

        model.addAttribute("user", user);
        return "show-qr";
    }

    // QR Image
    @GetMapping("/qr/{id}")
    public ResponseEntity<byte[]> getQr(@PathVariable Long id) throws Exception {
        User user = userRepo.findById(id).orElseThrow();

        String url = "http://localhost:8080/scan?token=" + user.getQrToken();
        byte[] qr = qrService.generateQr(url);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, "image/png")
                .body(qr);
    }

    // Scan Result
    @GetMapping("/scan")
    public String scanQr(@RequestParam String token, Model model) {
        User user = userRepo.findByQrToken(token);
        model.addAttribute("user", user);
        return "scan-result";
    }
}

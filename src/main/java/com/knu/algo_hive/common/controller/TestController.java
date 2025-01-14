package com.knu.algo_hive.common.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("/api")
    public ResponseEntity<String> mainPage() {
        return ResponseEntity.ok().body("test13");
    }

    @GetMapping("/api/oh")
    public ResponseEntity<String> sidePage() {
        return ResponseEntity.ok().body("new test13");
    }
}

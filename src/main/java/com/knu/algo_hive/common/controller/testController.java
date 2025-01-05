package com.knu.algo_hive.common.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class testController {

    @GetMapping("/api")
    public ResponseEntity<String> mainPage() {
        return ResponseEntity.ok().body("test7");
    }

    @GetMapping("/api/oh")
    public ResponseEntity<String> sidePage() {
        return ResponseEntity.ok().body("new test8");
    }
}

package com.knu.algo_hive.common.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class testController {

    @GetMapping("/")
    public ResponseEntity<String> mainPage() {
        return ResponseEntity.ok().body("test");
    }
}

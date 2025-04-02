package com.nbnp.trip_to_go.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CheckController {

    @GetMapping("/check")
    public String check() {
        return "OK!";
    }
}

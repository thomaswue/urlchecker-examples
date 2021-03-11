package com.example.springurlchecker;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("/")
public class MyController {
    @GetMapping(value = "/check", params = "url")
    public String check(String url) {
        return "Hello world " + url;
    }
}

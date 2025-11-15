package com.devsuperior.catalogo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping
@RestController
public class TesteString {

    @GetMapping
    public String demostracao() {
        return "Ol´´a";
    }
}

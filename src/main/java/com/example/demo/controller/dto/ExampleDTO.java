package com.example.demo.controller.dto;

import java.time.LocalDateTime;

public class ExampleDTO {

    private String name;
    private LocalDateTime createdDate = LocalDateTime.now();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }
}
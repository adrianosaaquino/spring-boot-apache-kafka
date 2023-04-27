package com.example.demo.dto;

import java.io.Serializable;
import java.time.LocalDateTime;

public class ExampleDTO implements Serializable {

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
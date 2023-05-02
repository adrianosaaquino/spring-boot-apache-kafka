package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProdutoDTO implements Serializable {

  private String id;
  private String name;
  @Max(10)
  private BigDecimal preco;
  private Date creationDate;
  @Max(10)
  private int preco2;
}

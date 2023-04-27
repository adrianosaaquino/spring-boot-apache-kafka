package com.example.demo.form;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProdutoForm {

  @NotBlank(message = "Name is required")
  private String name;

  @NotNull(message = "Preco is mandatory")
  private BigDecimal preco;

  @Builder.Default
  private Date creationDate = new Date();

}
package com.example.demo.controller;

import com.example.demo.domain.Produto;
import com.example.demo.dto.ProdutoDTO;
import com.example.demo.form.ProdutoForm;
import com.example.demo.service.ProdutoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/produtos")
public class ProdutoController {

  private final Logger logger = LoggerFactory.getLogger(getClass());
  private final ProdutoService produtoService;
  private final ObjectMapper mapper;

  ProdutoController(ProdutoService produtoService, ObjectMapper mapper) {
    this.produtoService = produtoService;
    this.mapper = mapper;
  }

  @GetMapping("")
  public List<ProdutoDTO> list() {

    logger.info("Getting all Produtos");

    List<Produto> produtos = produtoService.findProdutos();

    List<ProdutoDTO> produtosDTO = new ArrayList<>();

    for (Produto produto : produtos) {
      produtosDTO.add(
              mapper.convertValue(produto, ProdutoDTO.class)
      );
    }

    return produtosDTO;
  }

  @GetMapping("/{id}")
  public ProdutoDTO show(@PathVariable("id") String id) {

    Produto produto = produtoService.findById(id);

    return mapper.convertValue(produto, ProdutoDTO.class);
  }

  @PostMapping
  public ProdutoDTO save(@Valid @RequestBody ProdutoForm produtoForm) {

    Produto produto = produtoService.save(
            mapper.convertValue(produtoForm, Produto.class)
    );

    return mapper.convertValue(produto, ProdutoDTO.class);
  }
}

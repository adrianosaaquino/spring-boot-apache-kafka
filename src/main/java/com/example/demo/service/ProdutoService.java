package com.example.demo.service;

import com.example.demo.domain.Produto;
import com.example.demo.producer.ProdutoProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProdutoService {

  private final Logger logger = LoggerFactory.getLogger(getClass());

  private final MongoTemplate mongoTemplate;
  private final ProdutoProducer produtoProducer;

  public ProdutoService(MongoTemplate mongoTemplate, ProdutoProducer produtoProducer) {
    this.mongoTemplate = mongoTemplate;
    this.produtoProducer = produtoProducer;
  }

  public List<Produto> findProdutos() {

    logger.info("Find Produtos");

    return mongoTemplate.findAll(Produto.class);
  }

  public Produto findById(String id) {
    return mongoTemplate.findById(id, Produto.class);
  }

  public Produto save(Produto produto) {

    logger.info("Salvar produto [{}]", produto);

    Produto produtoEntity = mongoTemplate.save(produto);

    logger.info("Produto salvo com sucesso [{}]", produtoEntity);

    produtoProducer.notifyProdutoCreated(produtoEntity);



    return produtoEntity;
  }
}

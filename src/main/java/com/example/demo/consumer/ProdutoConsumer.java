package com.example.demo.consumer;

import com.example.demo.domain.Produto;
import com.example.demo.form.ProdutoForm;
import com.example.demo.service.ProdutoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.Set;

@Log4j2
@Component
public class ProdutoConsumer {

  private final ProdutoService produtoService;
  private final ObjectMapper mapper;
  private final Validator validator;

  ProdutoConsumer(ProdutoService produtoService, ObjectMapper mapper, Validator validator) {
    this.produtoService = produtoService;
    this.mapper = mapper;
    this.validator = validator;
  }

  @RabbitListener(queues = "produto.create")
  public void produtoCreate(ProdutoForm produtoForm) {

    log.info("Recebido produtoForm [{}]", produtoForm);

    Set<ConstraintViolation<ProdutoForm>> violations = validator.validate(produtoForm);

    if (!violations.isEmpty()) {

      StringBuilder sb = new StringBuilder();

      for (ConstraintViolation<ProdutoForm> constraintViolation : violations) {
        sb.append(constraintViolation.getMessage());
      }

      log.error("Erro ao savar o produto [{}], erros [{}]", produtoForm, sb);

    } else {

      produtoService.save(
              mapper.convertValue(produtoForm, Produto.class)
      );
    }
  }

  @RabbitListener(queues = "produto.created")
  public void produtoCreated(ProdutoForm produtoForm) {

    log.info("Recebido notificação de criação de produto [{}]", produtoForm);
  }
}

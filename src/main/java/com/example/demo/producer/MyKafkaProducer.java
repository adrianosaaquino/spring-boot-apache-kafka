package com.example.demo.producer;

import com.example.demo.dto.ProdutoDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class MyKafkaProducer {

  private final Logger logger = LoggerFactory.getLogger(this.getClass());

  private final KafkaTemplate<String, ProdutoDTO> producerProdutoDTO;

  // @Value("")
  private String topic = "demo-produtos";

  public MyKafkaProducer(KafkaTemplate<String, ProdutoDTO> producerCreated) {

    this.producerProdutoDTO = producerCreated;
  }

  public void produceProdutoDTODLQ(ProdutoDTO produtoDTO) {

    logger.info("[PRODUCE] - INFO: Enviando msg [{}]", produtoDTO.toString());

    producerProdutoDTO.send(topic, produtoDTO);
  }
}

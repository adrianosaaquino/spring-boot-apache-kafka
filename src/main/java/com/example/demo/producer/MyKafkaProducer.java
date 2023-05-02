package com.example.demo.producer;

import com.example.demo.dto.ProdutoDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class MyKafkaProducer {

  private final Logger logger = LoggerFactory.getLogger(this.getClass());

  private final KafkaTemplate<String, Object> kafkaTemplate;

  // @Value("")
  private String topic = "demo-produtos";

  public MyKafkaProducer(KafkaTemplate<String, Object> producerCreated) {

    this.kafkaTemplate = producerCreated;
  }

  public void produceProdutoDTODLQ(ProdutoDTO produtoDTO) {

    logger.info("[PRODUCE] - INFO: Enviando msg [{}]", produtoDTO.toString());

    kafkaTemplate.send(topic, produtoDTO);
  }
}

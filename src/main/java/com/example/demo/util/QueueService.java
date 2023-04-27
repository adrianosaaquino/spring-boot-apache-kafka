package com.example.demo.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class QueueService {

  private final Logger logger = LoggerFactory.getLogger(getClass());

  final private RabbitTemplate rabbitTemplate;

  public QueueService(RabbitTemplate rabbitTemplate) {
    this.rabbitTemplate = rabbitTemplate;
  }

  public void sendAsJSON(String exchange, String routingKey, Object payload) {
    try {

      logger.info(
              "Notify exchange [{}], routingKey [{}], payload [{}]",
              exchange,
              routingKey,
              payload
      );

      String json = new ObjectMapper().writeValueAsString(payload);

      Message messageToSend = MessageBuilder.withBody(json.getBytes())
              .setContentType(MessageProperties.CONTENT_TYPE_JSON)
              .build();

      rabbitTemplate.convertAndSend(exchange, routingKey, messageToSend);

    } catch (Exception e) {

      logger.error("Notify exchange ERROR [{}]", e.getMessage());

      e.printStackTrace();
    }
  }
}

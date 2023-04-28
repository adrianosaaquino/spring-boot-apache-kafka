package com.example.demo.consumer;

import com.example.demo.dto.ProdutoDTO;
import com.example.demo.producer.MyKafkaProducer;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalTime;

@Component
public class KafkaConsumidor {

  @Value("${app.tempo-nack-ms}")
  private long tempoNackMs;

  private final Logger logger = LoggerFactory.getLogger(this.getClass());

  private final ObjectMapper objectMapper;
  private final MyKafkaProducer myKafkaProducer;

  public KafkaConsumidor(ObjectMapper objectMapper,
                         MyKafkaProducer myKafkaProducer) {

    this.objectMapper = objectMapper;
    this.myKafkaProducer = myKafkaProducer;
  }

  @KafkaListener(topics = "${app.topico-demo-produtos}")
  public void consumirProdutoDTO(@Payload String payload,
                                 @Header(value = KafkaHeaders.RECEIVED_MESSAGE_KEY, required = false) String key,
                                 @Header(KafkaHeaders.RECEIVED_TOPIC) String topico,
                                 @Header(KafkaHeaders.RECEIVED_TIMESTAMP) long ts,
                                 Acknowledgment ack) throws JsonProcessingException {

    logger.info("[key][topico][ts][payload][{}][{}][{}][{}]", key, topico, ts, payload);

    ProdutoDTO produtoDTO = objectMapper.readValue(payload, ProdutoDTO.class);

    try {

      logger.info("ProdutoDTO: [{}]", produtoDTO);

      if (produtoDTO.getPreco().compareTo(new BigDecimal("10.00")) > 0) {

        throw new IllegalArgumentException("Valor inválido, preço maior que [10.00]");
      }

      ack.acknowledge();
      logger.info("Commit realizado");

    } catch (IllegalArgumentException e) {

      logger.error(e.getMessage());

      int second = LocalTime.now().getSecond();

      if ((second % 2) == 1) {

        ack.acknowledge();

        logger.info("Preço maior que [10.00] mas produto chegou em segundo ímpar, é válido, second [{}]", second);
        logger.info("Commit realizado");

      } else {

        logger.info("Preço maior que [10.00] e produto chegou em segundo par, second [{}]", second);

        int random = (int) ((Math.random() * (15 - 5)) + 5);

        if (random <= 7) {

          ack.acknowledge();
          logger.info("random [{}] <= 7, commit....", random);

        } else {

          logger.info("random [{}] > 7, DLQ", random);
          myKafkaProducer.produceProdutoDTODLQ(produtoDTO);

        }
      }

    } catch (Exception e) {

      logger.error("Erro desconhecido ao tentar salvar", e);
      ack.acknowledge();
    }

  }

  @KafkaListener(topics = "${app.topico-demo-produtos.DLQ}", errorHandler = "errorHandler....")
  public void consumirProdutoDTODLQ(@Payload String payload,
                                    @Header(value = KafkaHeaders.RECEIVED_MESSAGE_KEY, required = false) String key,
                                    @Header(KafkaHeaders.RECEIVED_TOPIC) String topico,
                                    @Header(KafkaHeaders.RECEIVED_TIMESTAMP) long ts,
                                    Acknowledgment ack) {

    logger.info("[key][topico][ts][payload][{}][{}][{}][{}]", key, topico, ts, payload);

    try {

      ProdutoDTO produtoDTO = objectMapper.readValue(payload, ProdutoDTO.class);
      logger.info("ProdutoDTO: name  [{}]", produtoDTO.getName());
      logger.info("ProdutoDTO: preço [{}]", produtoDTO.getPreco());
      logger.info("ProdutoDTO: [{}]", produtoDTO);

      int second = LocalTime.now().getSecond();

      if ((second % 2) == 1) {

        int tempoNackMsCustom = (int) ((Math.random() * (15 - 5)) + 5);

        logger.error("Produto chegou em segundo ínpar, não será processado agora, second [{}], processar daqui a [{}] segundos", second, tempoNackMsCustom);

        ack.nack(tempoNackMsCustom * 1000L);

      } else {

        logger.error("Produto pode ser processado, second [{}]", second);
        ack.acknowledge();
        logger.info("Commit realizado");
      }

    } catch (Exception e) {

      logger.error("Erro desconhecido ao tentar salvar", e);
      ack.acknowledge();
    }

  }
}

package com.example.demo.consumer;

import com.example.demo.dto.ProdutoDTO;
import com.example.demo.dto.SampleRecord;
import com.example.demo.producer.MyKafkaProducer;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.retrytopic.FixedDelayStrategy;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.retry.annotation.Backoff;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalTime;

@Validated
@Component
public class KafkaConsumidor {

  @Value("${app.tempo-nack-ms}")
  private long tempoNackMs;

  private final Logger logger = LoggerFactory.getLogger(this.getClass());

  private final ObjectMapper objectMapper;

  private final MyKafkaProducer sampleRecordKafkaTemplate;

  public KafkaConsumidor(ObjectMapper objectMapper,
                         MyKafkaProducer sampleRecordKafkaTemplate,
                         KafkaTemplate<String, SampleRecord> sampleRecordTemplate) {

    this.objectMapper = objectMapper;
    this.sampleRecordKafkaTemplate = sampleRecordKafkaTemplate;
    this.sampleRecordTemplate = sampleRecordTemplate;
  }

  private KafkaTemplate<String, SampleRecord> sampleRecordTemplate;

  @EventListener(ApplicationReadyEvent.class)
  public void runAfterStartup() throws IOException {

    logger.info("runAfterStartup");

    ProdutoDTO produtoDTO = ProdutoDTO.builder()
            .name("Produto DELL 102")
            .preco(new BigDecimal("10.00"))
            .preco2(10)
            .build();

    sampleRecordKafkaTemplate.produceProdutoDTODLQ(produtoDTO);

    logger.info("runAfterStartup");

    SampleRecord sampleRecord = new SampleRecord();
    sampleRecord.setName("Outro nome 123");
    sampleRecord.setValor(10);

    sampleRecordTemplate.send("demo-produtos-avro", sampleRecord);

    logger.info("runAfterStartup foi");
  }

  @RetryableTopic(
          attempts = "5",
          fixedDelayTopicStrategy = FixedDelayStrategy.SINGLE_TOPIC,
          backoff = @Backoff(10000)
  )
  @KafkaListener(
          topics = "${app.topico-demo-produtos}",
          properties = "value.deserializer:org.springframework.kafka.support.serializer.JsonDeserializer"
  )
  public void consumirProdutoDTO(@Payload @Valid ProdutoDTO payload,
                                 @Header(KafkaHeaders.RECEIVED_TOPIC) String topico) {

    logger.info("[key][topico][ts][S][payload][{}][{}][{}]", topico, LocalTime.now().getSecond(), payload);

    logger.info("------------------");


    // throw new IllegalArgumentException("Valor inválido, preço maior que [10.00]");

    /*logger.info("[key][topico][ts][payload][{}][{}][{}][{}]", key, topico, ts, payload);

    ProdutoDTO produtoDTO = payload.value();

    logger.info("ProdutoDTO: [{}]", produtoDTO);

    if (produtoDTO.getPreco().compareTo(new BigDecimal("10.00")) > 0) {

      throw new IllegalArgumentException("Valor inválido, preço maior que [10.00]");
    }

    ack.acknowledge();
    logger.info("Commit realizado");*/
  }

  @KafkaListener(
          topics = "demo-produtos-avro",
          properties = "value.deserializer:io.confluent.kafka.serializers.KafkaAvroDeserializer"
  )
  public void consumirProdutoDTO(ConsumerRecord<String, SampleRecord> payload,
                                 @Header(KafkaHeaders.RECEIVED_TOPIC) String topico) {

    logger.info("[key][topico][ts][S][payload][{}][{}][{}]", topico, LocalTime.now().getSecond(), payload.value());

    logger.info("------------------");

    // throw new IllegalArgumentException("Valor inválido, preço maior que [10.00]");

    /*logger.info("[key][topico][ts][payload][{}][{}][{}][{}]", key, topico, ts, payload);

    ProdutoDTO produtoDTO = payload.value();

    logger.info("ProdutoDTO: [{}]", produtoDTO);

    if (produtoDTO.getPreco().compareTo(new BigDecimal("10.00")) > 0) {

      throw new IllegalArgumentException("Valor inválido, preço maior que [10.00]");
    }

    ack.acknowledge();
    logger.info("Commit realizado");*/
  }

  /*@DltHandler
  public void handleDlt(ConsumerRecord<String, ProdutoDTO> consumerRecord,
                        @Header(KafkaHeaders.RECEIVED_TOPIC) String topico) {

    logger.info("[key][topico][ts][S][payload][{}][{}][{}]", topico, LocalTime.now().getSecond(), consumerRecord.value());

    *//*logger.info("[key][topico][ts][payload][{}][{}][{}][{}]", key, topico, ts, consumerRecord.value());

    ProdutoDTO produtoDTO = consumerRecord.value();

    logger.info("Send to DLQ");
    // myKafkaProducer.produceProdutoDTODLQ(produtoDTO);

    ack.acknowledge();
    logger.info("Commit realizado");*//*
  }*/

  /*@KafkaListener(topics = "${app.topico-demo-produtos.DLQ}")
  public void consumirProdutoDTODLQ(ConsumerRecord<String, ProdutoDTO> consumerRecord,
                                    @Header(value = KafkaHeaders.RECEIVED_MESSAGE_KEY, required = false) String key,
                                    @Header(KafkaHeaders.RECEIVED_TOPIC) String topico,
                                    @Header(KafkaHeaders.RECEIVED_TIMESTAMP) long ts,
                                    Acknowledgment ack) {

    logger.info("[key][topico][ts][payload][{}][{}][{}][{}]", key, topico, ts, consumerRecord);

    try {

      ProdutoDTO produtoDTO = consumerRecord.value();

      logger.info("ProdutoDTO: name  [{}]", produtoDTO.getName());
      logger.info("ProdutoDTO: preço [{}]", produtoDTO.getPreco());
      logger.info("ProdutoDTO: [{}]", produtoDTO);

      ack.acknowledge();
      logger.info("Commit realizado");

      *//*int second = LocalTime.now().getSecond();

      if ((second % 2) == 1) {

        int tempoNackMsCustom = (int) ((Math.random() * (15 - 5)) + 5);

        logger.error("Produto chegou em segundo ínpar, não será processado agora, second [{}], processar daqui a [{}] segundos", second, tempoNackMsCustom);

        ack.nack(tempoNackMsCustom * 1000L);

      } else {

        logger.error("Produto pode ser processado, second [{}]", second);
        ack.acknowledge();
        logger.info("Commit realizado");
      }*//*

    } catch (Exception e) {

      logger.error("Erro desconhecido ao tentar salvar", e);
      ack.acknowledge();
    }

  }*/
}

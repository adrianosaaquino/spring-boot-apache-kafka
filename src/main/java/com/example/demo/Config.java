package com.example.demo;

import com.example.demo.dto.ProdutoDTO;
import com.example.demo.dto.SampleRecord;
import io.confluent.kafka.serializers.KafkaAvroSerializer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListenerConfigurer;
import org.springframework.kafka.config.KafkaListenerEndpointRegistrar;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableKafka
public class Config implements KafkaListenerConfigurer {

  final private KafkaProperties kafkaProperties;

  final private LocalValidatorFactoryBean validator;

  public Config(KafkaProperties kafkaProperties, LocalValidatorFactoryBean validator) {
    this.kafkaProperties = kafkaProperties;
    this.validator = validator;
  }

  @Override
  public void configureKafkaListeners(KafkaListenerEndpointRegistrar registrar) {
    registrar.setValidator(this.validator);
  }

  @Bean
  public ProducerFactory<String, Object> kafkaProducerFactory() {
    Map<String, Object> configProps = new HashMap<>(kafkaProperties.getProperties());
    configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaProperties.getBootstrapServers());
    configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, kafkaProperties.getProducer().getKeySerializer());
    configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, kafkaProperties.getProducer().getValueSerializer());
    return new DefaultKafkaProducerFactory<>(configProps);
  }

  @Bean
  public KafkaTemplate<String, Object> kafkaTemplate() {
    return new KafkaTemplate<>(kafkaProducerFactory());
  }

  @Bean
  public ProducerFactory<String, SampleRecord> sampleRecordProducerFactory() {
    Map<String, Object> configProps = new HashMap<>(kafkaProperties.getProperties());
    configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaProperties.getBootstrapServers());
    configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, kafkaProperties.getProducer().getKeySerializer());
    configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, KafkaAvroSerializer.class);
    return new DefaultKafkaProducerFactory<>(configProps);
  }

  @Bean
  public KafkaTemplate<String, SampleRecord> sampleRecordKafkaTemplate() {
    return new KafkaTemplate<>(sampleRecordProducerFactory());
  }
}
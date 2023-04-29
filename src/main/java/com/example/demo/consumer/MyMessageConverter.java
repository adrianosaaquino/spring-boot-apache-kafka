package com.example.demo.consumer;

import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.converter.MessageConversionException;
import org.springframework.messaging.converter.MessageConverter;
import org.springframework.messaging.support.MessageBuilder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import org.springframework.stereotype.Component;

@Component
public class MyMessageConverter implements MessageConverter {

  final private ObjectMapper objectMapper;

  public MyMessageConverter(ObjectMapper objectMapper) {
    this.objectMapper = objectMapper;
  }

  @Override
  public Message<?> toMessage(Object payload, MessageHeaders headers) {
    try {
      byte[] data = objectMapper.writeValueAsBytes(payload);
      return MessageBuilder.withPayload(data).copyHeaders(headers).build();
    } catch (JsonProcessingException e) {
      throw new MessageConversionException("Failed to convert object to JSON", e);
    }
  }

  @Override
  public Object fromMessage(Message<?> message, Class<?> targetClass) {
    try {
      byte[] data = (byte[]) message.getPayload();
      return objectMapper.readValue(data, targetClass);
    } catch (IOException e) {
      throw new MessageConversionException("Failed to convert JSON to object", e);
    }
  }
}
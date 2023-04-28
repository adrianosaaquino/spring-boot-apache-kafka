package com.example.demo;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalTime;

// @SpringBootTest
class DemoApplicationTests {

  private final Logger logger = LoggerFactory.getLogger(this.getClass());

  @Test
  void contextLoads() {

    int second = LocalTime.now().getSecond();

    logger.info("Second: [{}]", second);
    logger.info("second % 3: [{}]", second % 3);

    System.out.println((second % 3) == 0);

    int i = (int) ((Math.random() * (20 - 10)) + 10) * 1000;
    System.out.println(i);
  }

}

package com.example.demo.producer;

import com.example.demo.domain.Produto;
import com.example.demo.util.QueueService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class ProdutoProducer {

  private final Logger logger = LoggerFactory.getLogger(getClass());

  final private String EXCHANGE = "spring-boot-demo-by-example";
  final private String ROUTING_KEY_PRODUTO_CREATED = "produto.created";

  private final QueueService queueService;

  public ProdutoProducer(QueueService queueService) {
    this.queueService = queueService;
  }

  public void notifyProdutoCreated(Produto produtoEntity) {

    logger.info("Notify Produto Created - Produto [{}]", produtoEntity.toString());

    queueService.sendAsJSON(EXCHANGE, ROUTING_KEY_PRODUTO_CREATED, produtoEntity);
  }
}

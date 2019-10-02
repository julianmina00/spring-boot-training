package com.trainingsession.invoiceconsumer.route;

import static org.apache.camel.component.kafka.KafkaConstants.LAST_RECORD_BEFORE_COMMIT;
import static org.apache.camel.component.kafka.KafkaConstants.MANUAL_COMMIT;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.kafka.KafkaManualCommit;
import org.apache.camel.model.RouteDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class InvoiceConsumerRoute extends RouteBuilder {

  private static final Logger logger = LoggerFactory.getLogger(InvoiceConsumerRoute.class);
  private static final String CONSUMER_GROUP_ID = "invoice-consumer";


  @Value("${kafka.bootstrap.servers}")
  private String bootstrapServer;

  @Value("${kafka.invoices.topic}")
  private String topicName;

  @Value("${create.list.endpoint.url}")
  private String createListEndpointUrl;

  @Autowired
  private Processor invoiceToListProcessor;

  @Autowired
  private Processor prepareListPostProcessor;

  @Override
  public void configure() throws Exception {

    RouteDefinition definition = new RouteDefinition();
    String kafkaConfiguration = getKafkaConfiguration();
    definition.from(kafkaConfiguration)
        .routeId("InvoiceConsumerRoute")
        .log("Transforming the Invoice into a List")
        .process(invoiceToListProcessor)
        .log("Preparing the Post to the ListService")
        .process(prepareListPostProcessor)
        .toD("https4:" + createListEndpointUrl)
        .log("List created!")
        .process(this::performManualCommit);
    addExceptionHandler(definition);
  }

  private void addExceptionHandler(RouteDefinition route) {
    route.onException(Exception.class).handled(true)
        .process(exchange -> {
          Exception ex = (Exception) exchange.getProperty(Exchange.EXCEPTION_CAUGHT);
          logger.error(ex.getLocalizedMessage(), ex);
        });
  }

  private String getKafkaConfiguration(){
    return "kafka:".concat(topicName)
        .concat("?brokers=").concat(bootstrapServer)
        .concat("&groupId=").concat(CONSUMER_GROUP_ID)
        .concat("&maxPollRecords=1")
        .concat("&consumersCount=1")
        .concat("&autoOffsetReset=earliest")
        .concat("&autoCommitEnable=false")
        .concat("&allowManualCommit=true")
        .concat("&breakOnFirstError=false");
  }

  private void performManualCommit(Exchange exchange) {
    Boolean lastOne = exchange.getIn().getHeader(LAST_RECORD_BEFORE_COMMIT, Boolean.class);
    if (lastOne) {
      KafkaManualCommit manual = exchange.getIn().getHeader(MANUAL_COMMIT, KafkaManualCommit.class);
      if (manual != null) {
        if (logger.isInfoEnabled()) {
          logger.info("manually committing the offset for batch");
        }
        manual.commitSync();
      }
    } else {
      if (logger.isInfoEnabled()) {
        logger.info("Not time to commit the offset yet");
      }
    }
  }

}


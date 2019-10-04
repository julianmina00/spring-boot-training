package com.trainingsession.invoiceconsumer.route;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.ShutdownRoute;
import org.apache.camel.model.RouteDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


@Component
public class ConsumerRouteDefinition {

  private static final Logger logger = LoggerFactory.getLogger(ConsumerRouteDefinition.class);
  private static final String CONSUMER_GROUP_ID = "invoice-consumer-3";
  private static final String ROUTE_ID = "InvoiceConsumerRoute";

  private final String bootstrapServer;
  private final String topicName;
  private final String createListEndpointUrl;
  private final Processor invoiceToListProcessor;

  @Autowired
  public ConsumerRouteDefinition(
      @Value("${kafka.bootstrap.servers}") String bootstrapServer,
      @Value("${kafka.invoices.topic}") String topicName,
      @Value("${create.list.endpoint.url}") String createListEndpointUrl,
      @Qualifier("invoiceToListProcessor") Processor invoiceToListProcessor) {
    this.bootstrapServer = bootstrapServer;
    this.topicName = topicName;
    this.createListEndpointUrl = createListEndpointUrl;
    this.invoiceToListProcessor = invoiceToListProcessor;
  }

  public String getRouteId(){
    return ROUTE_ID;
  }

  public RouteDefinition createRouteDefinition() {
    RouteDefinition definition = new RouteDefinition();
    String kafkaConfiguration = getKafkaConfiguration();
    definition.from(kafkaConfiguration)
        .streamCaching()
        .routeId(getRouteId())
        .shutdownRoute(ShutdownRoute.Defer)
        .log("Transforming the Invoice ${body} into a List")
        .process(invoiceToListProcessor)
        .toD("http4:" + createListEndpointUrl)
        .log("List created: ${body}");
    addExceptionHandler(definition);
    return definition;
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
        .concat("&maxPollIntervalMs=").concat(Integer.toString(Integer.MAX_VALUE))
        .concat("&autoOffsetReset=earliest");
  }

}

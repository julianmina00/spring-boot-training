package com.trainingsession.invoicepublisher.route;

import static org.apache.camel.model.rest.RestParamType.body;

import javax.annotation.Resource;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.RouteDefinition;
import org.apache.camel.model.rest.RestBindingMode;
import org.apache.camel.model.rest.RestDefinition;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class PublishingRoute extends RouteBuilder {

  private static final Logger logger = LoggerFactory.getLogger(PublishingRoute.class);

  @Value("${server.port}")
  private int port;

  @Value("${kafka.bootstrap.servers}")
  private String boostrapServer;

  @Value("${kafka.cohesion.post.topic}")
  private String topicName;

  @Resource(name = "publishingRouteProcessor")
  private Processor processor;

  @Override
  public void configure() throws Exception {

    // Configureation camel
    restConfiguration()
        .component("netty4-http")
        .port(port)
        .bindingMode(RestBindingMode.json)
        .dataFormatProperty("prettyPrint", "true")
        .contextPath("/")
        .apiContextPath("/api-doc")
        .apiProperty("api.title", "Invoice Publisher API")
        .apiProperty("api.version", "1.0.0")
        .apiProperty("cors", "true");

    // Configure the route entry
    RestDefinition restDef = rest("/api")
        .post("/invoice")
        .consumes("text/plain")
        .bindingMode(RestBindingMode.off)
        .description("Post an invoice")
        .produces("application/json")
        .outType(String.class);
    addFilters(restDef);

    // configure the route to process the Cohesion Post
    RouteDefinition routeDefinition = restDef.route()
        .routeId("InvoicePublisherRoute")
        .log("Preparing the invoice to be published")
        .process(processor)
        .to(getKafkaConfig())
        .log("Invoice was successfully published to Kafka");
    addExceptionHandler(routeDefinition);

  }

  private void addFilters(RestDefinition restDef){
    restDef.param()
        .name("body")
        .type(body)
        .description("A string containing the information of the invoice")
        .dataType("string")
        .required(true)
        .endParam();
  }

  private String getKafkaConfig(){
    return "kafka:" + topicName
        + "?brokers=" + boostrapServer
        + "&serializerClass=" + StringSerializer.class.getName();
  }

  private void addExceptionHandler(RouteDefinition route) {
    route.onException(Exception.class).handled(true)
        .process(exchange -> {
          Exception ex = (Exception)exchange.getProperty(Exchange.EXCEPTION_CAUGHT);
          logger.error(ex.getLocalizedMessage(), ex);
        });
  }

}

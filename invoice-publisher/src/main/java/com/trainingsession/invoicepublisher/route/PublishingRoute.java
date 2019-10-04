package com.trainingsession.invoicepublisher.route;

import static org.apache.camel.model.rest.RestParamType.body;

import javax.annotation.Resource;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.ProcessorDefinition;
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
  private static final String BULK_ENDPOINT_PATH = "/invoice/bulk";
  private static final String BULK_ENDPOINT_DESCRIPTION = "Post a bunch of invoices at one time";
  private static final String BULK_BODY_DESCRIPTION = "A string containing a list of invoices";
  private static final String INVOICE_ENDPOINT_PATH = "/invoice";
  private static final String INVOICE_ENDPOINT_DESCRIPTION = "Post an invoice";
  private static final String INVOICE_BODY_DESCRIPTION = "A string containing the information of the invoice";

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
    addInvoiceRoute();
    addInvoiceBulkRoute();
  }

  private void addInvoiceRoute() {
    RestDefinition restDef = createRestDefinition(
        INVOICE_ENDPOINT_PATH,
        INVOICE_ENDPOINT_DESCRIPTION,
        INVOICE_BODY_DESCRIPTION);
    // configure the route to process a single invoice
    RouteDefinition routeDefinition = restDef.route()
        .routeId("InvoicePublisherRoute")
        .log("Preparing the invoice to be published")
        .process(processor)
        .to(getKafkaConfig())
        .log("Invoice was successfully published to Kafka");
    addExceptionHandler(routeDefinition);
  }

  private void addInvoiceBulkRoute() {
   RestDefinition restDef = createRestDefinition(
       BULK_ENDPOINT_PATH,
       BULK_ENDPOINT_DESCRIPTION,
       BULK_BODY_DESCRIPTION);
    // configure the route to process a bunch of invoices
    ProcessorDefinition<?> definition = restDef.route()
        .routeId("InvoiceBulkPublisherRoute")
        .split(body().tokenize("\n")).streaming()
        .log("Preparing the invoices to be published")
        .process(processor)
        .to(getKafkaConfig())
        .log("Invoice was successfully published to Kafka")
        .end();
    addExceptionHandler(definition);
  }

  private RestDefinition createRestDefinition(String endpointPath, String endpointDescription, String bodyDescription) {
    RestDefinition restDef = rest("/api")
        .post(endpointPath)
        .consumes("text/plain")
        .bindingMode(RestBindingMode.off)
        .description(endpointDescription)
        .produces("application/json")
        .outType(String.class);

    // Swagger configuration
    restDef.param()
        .name("body").type(body).description(bodyDescription)
        .dataType("string").required(true).endParam();
    return restDef;
  }

  private String getKafkaConfig(){
    return "kafka:" + topicName
        + "?brokers=" + boostrapServer
        + "&serializerClass=" + StringSerializer.class.getName();
  }

  private void addExceptionHandler(ProcessorDefinition<?> route) {
    route.onException(Exception.class).handled(true)
        .process(exchange -> {
          Exception ex = (Exception)exchange.getProperty(Exchange.EXCEPTION_CAUGHT);
          logger.error(ex.getLocalizedMessage(), ex);
        });
  }

}

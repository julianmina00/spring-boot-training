package com.trainingsession.invoicepublisher.route;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.trainingsession.invoicepublisher.model.Invoice;
import java.util.Arrays;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.stereotype.Component;

@Component("publishingRouteProcessor")
public class PublishingRouteProcessor implements Processor {

  private static final String FIELDS_SEPARATOR = ":";
  private static final String ITEMS_SEPARATOR = ",";

  private final ObjectMapper mapper = new ObjectMapper();

  @Override
  public void process(Exchange exchange) throws Exception {
    String body = exchange.getIn().getBody(String.class);
    String[] fields = body.split(FIELDS_SEPARATOR);
    String[] items = fields[2].split(ITEMS_SEPARATOR);

    Invoice invoice = new Invoice();
    invoice.setNumber(fields[0]);
    invoice.setAmount(fields[1]);
    invoice.setItems(Arrays.asList(items));

    String json = mapper.writeValueAsString(invoice);
    exchange.getIn().setBody(json, String.class);
  }

}

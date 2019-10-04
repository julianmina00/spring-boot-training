package com.trainingsession.invoiceconsumer.route.processor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.trainingsession.invoiceconsumer.model.Invoice;
import com.trainingsession.invoiceconsumer.model.List;
import com.trainingsession.invoiceconsumer.model.ListItem;
import java.util.HashMap;
import java.util.Map;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.component.http4.HttpMethods;
import org.springframework.stereotype.Component;

@Component("invoiceToListProcessor")
public class InvoiceToListProcessor implements Processor {

  private final ObjectMapper mapper = new ObjectMapper();

  @Override
  public void process(Exchange exchange) throws Exception {
    Invoice invoice = mapper.readValue(exchange.getIn().getBody(String.class), Invoice.class);
    List list = new List();
    list.setName("Invoice number: "+invoice.getNumber());
    list.setDescription("Shopping list for invoice "+invoice.getNumber()+". Total amount: "+invoice.getTotal());
    // Count the items in the invoice and add them as a single element to the List
    Map<String,Integer> itemsCounter = new HashMap<>();
    invoice.getItems().forEach(item -> {
      itemsCounter.putIfAbsent(item, 0);
      itemsCounter.computeIfPresent(item, (listItem, count) -> count + 1);
    });
    itemsCounter.forEach((item, count) -> list.getItems().add(new ListItem(item," x "+count)));

    // Prepare the POST to the ListService
    exchange.getIn().setBody(mapper.writeValueAsString(list), String.class);
    exchange.getIn().removeHeaders("*");
    exchange.getIn().setHeader(Exchange.CONTENT_TYPE, "application/json");
    exchange.getIn().setHeader(Exchange.HTTP_METHOD, HttpMethods.POST);
  }
}

package com.trainingsession.invoiceconsumer.route;

import org.apache.camel.CamelContext;
import org.apache.camel.spring.SpringCamelContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class InvoiceConsumerRoute {

  private static final Logger logger = LoggerFactory.getLogger(InvoiceConsumerRoute.class);

  private final CamelContext camelContext;

  @Autowired
  public InvoiceConsumerRoute(ApplicationContext applicationContext, ConsumerRouteDefinition routeDefinition){
    camelContext = new SpringCamelContext(applicationContext);
    try {
      camelContext.setAutoStartup(false);
      camelContext.start();
      camelContext.addRouteDefinition(routeDefinition.createRouteDefinition());
      camelContext.startRoute(routeDefinition.getRouteId());
    } catch (Exception ex){
      logger.error("Error starting Camel Context: "+ex.getLocalizedMessage());
    }
  }

}


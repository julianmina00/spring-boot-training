package com.trainingsession.invoiceconsumer.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Invoice {

  private String number;
  private String total;
  private List<String> items;

}

package com.trainingsession.invoicepublisher.model;

import java.util.List;
import lombok.Data;

@Data
public class Invoice {

  private String number;
  private String total;
  private List<String> items;

}

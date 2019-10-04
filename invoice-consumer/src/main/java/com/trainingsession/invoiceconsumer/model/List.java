package com.trainingsession.invoiceconsumer.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.ArrayList;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class List {

  private String name;
  private String description;
  private java.util.List<ListItem> items;

  public java.util.List<ListItem> getItems(){
    if(items == null){
      items = new ArrayList<>();
    }
    return items;
  }

}

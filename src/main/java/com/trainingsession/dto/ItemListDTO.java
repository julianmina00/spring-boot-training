package com.trainingsession.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ItemListDTO extends ListDTO {

  private List<ItemDTO> items;

  public List<ItemDTO> getItems() {
    return items;
  }

  public void setItems(List<ItemDTO> items) {
    this.items = items;
  }
}

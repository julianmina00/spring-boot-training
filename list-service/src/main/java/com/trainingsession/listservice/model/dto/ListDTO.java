package com.trainingsession.listservice.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ListDTO {

  private Long id;
  private String name;
  private String description;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
  private List<ItemDTO> items = new ArrayList<>();

  public List<ItemDTO> getItems() {
    if(items == null){
      items = new ArrayList<>();
    }
    return items;
  }

}

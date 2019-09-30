package com.trainingsession.service.impl;

import com.trainingsession.model.dto.ItemDTO;
import com.trainingsession.model.dto.ListDTO;
import com.trainingsession.model.entity.Item;
import com.trainingsession.model.entity.List;
import com.trainingsession.repository.ItemRepository;
import com.trainingsession.service.Converter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("listConverter")
public class ListConverterImpl implements Converter<List, ListDTO> {

  @Autowired
  private ItemRepository itemRepository;

  @Autowired
  private Converter<Item, ItemDTO> itemConverter;

  @Override
  public ListDTO convert(List entity) {
    if(entity == null){
      return null;
    }
    ListDTO dto = new ListDTO();
    dto.setId(entity.getId());
    dto.setName(entity.getName());
    dto.setDescription(entity.getDescription());
    java.util.List<Item> items = itemRepository.findByListId(entity.getId());
    dto.setItems(itemConverter.convertAll(items));
    return dto;
  }

  @Override
  public List reverseConvert(ListDTO dto) {
    List entity = new List();
    entity.setId(dto.getId());
    entity.setName(dto.getName());
    entity.setDescription(dto.getDescription());
    return entity;
  }

}

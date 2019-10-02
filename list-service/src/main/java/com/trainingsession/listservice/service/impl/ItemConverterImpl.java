package com.trainingsession.listservice.service.impl;

import com.trainingsession.listservice.model.dto.ItemDTO;
import com.trainingsession.listservice.model.entity.Item;
import com.trainingsession.listservice.service.Converter;
import org.springframework.stereotype.Component;

@Component("itemConverter")
public class ItemConverterImpl implements Converter<Item, ItemDTO> {

  @Override
  public ItemDTO convert(Item entity) {
    if(entity == null){
      return null;
    }
    ItemDTO dto = new ItemDTO();
    dto.setId(entity.getId());
    dto.setName(entity.getName());
    dto.setDescription(entity.getDescription());
    return dto;
  }

  @Override
  public Item reverseConvert(ItemDTO dto) {
    Item entity = new Item();
    entity.setId(dto.getId());
    entity.setName(dto.getName());
    entity.setDescription(dto.getDescription());
    return entity;
  }
}

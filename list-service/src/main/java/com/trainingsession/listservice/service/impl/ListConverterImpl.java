package com.trainingsession.listservice.service.impl;

import com.trainingsession.listservice.model.dto.ItemDTO;
import com.trainingsession.listservice.model.dto.ListDTO;
import com.trainingsession.listservice.model.entity.Item;
import com.trainingsession.listservice.model.entity.List;
import com.trainingsession.listservice.repository.ItemRepository;
import com.trainingsession.listservice.service.Converter;
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
    dto.setCreatedAt(entity.getCreatedAt());
    dto.setUpdatedAt(entity.getUpdatedAt());
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

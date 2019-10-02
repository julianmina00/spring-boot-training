package com.trainingsession.listservice.service.impl;

import com.trainingsession.listservice.model.dto.ItemDTO;
import com.trainingsession.listservice.model.dto.ListDTO;
import com.trainingsession.listservice.model.entity.Item;
import com.trainingsession.listservice.repository.ItemRepository;
import com.trainingsession.listservice.repository.ListRepository;
import com.trainingsession.listservice.service.Converter;
import com.trainingsession.listservice.service.ListService;
import java.util.List;
import java.util.Optional;
import javax.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("listService")
public class ListServiceImpl implements ListService {

  @Autowired
  private ListRepository listRepository;

  @Autowired
  private ItemRepository itemRepository;

  @Autowired
  private Converter<com.trainingsession.listservice.model.entity.List, ListDTO> listConverter;

  @Autowired
  private Converter<Item, ItemDTO> itemConverter;

  @Override
  public List<ListDTO> getLists() {
    return listConverter.convertAll(listRepository.findAll());
  }

  @Override
  public ListDTO getList(long id) {
    Optional<com.trainingsession.listservice.model.entity.List> optionalList = listRepository.findById(id);
    return optionalList.map(list -> listConverter.convert(list)).orElse(null);
  }

  @Override
  public ListDTO createList(@NotNull ListDTO listDTO) {
    // persist data
    com.trainingsession.listservice.model.entity.List createdList =
        listRepository.save(listConverter.reverseConvert(listDTO));
    List<Item> items = itemConverter.reverseConvertAll(listDTO.getItems());
    items.forEach(item -> item.setListId(createdList.getId()));
    List<Item> createdItems = itemRepository.saveAll(items);

    // prepare response DTO
    ListDTO responseDTO = listConverter.convert(createdList);
    responseDTO.setItems(itemConverter.convertAll(createdItems));
    return responseDTO;
  }

  @Override
  public ListDTO createList(@NotNull String name, String description) {
    com.trainingsession.listservice.model.entity.List entity = new com.trainingsession.listservice.model.entity.List();
    entity.setName(name);
    entity.setDescription(description);
    com.trainingsession.listservice.model.entity.List createdList = listRepository.save(entity);
    return createList(listConverter.convert(createdList));
  }

  @Override
  public ListDTO updateList(long id, @NotNull String name, String description) {
    Optional<com.trainingsession.listservice.model.entity.List> record = listRepository.findById(id);
    if (record.isPresent()) {
      com.trainingsession.listservice.model.entity.List entity = record.get();
      entity.setName(name);
      entity.setDescription(description);
      com.trainingsession.listservice.model.entity.List updated = listRepository.save(entity);
      return listConverter.convert(updated);
    }
    return null;
  }

  @Override
  public ListDTO deleteList(long id) {
    Optional<com.trainingsession.listservice.model.entity.List> record = listRepository.findById(id);
    record.ifPresent(list -> listRepository.deleteById(id));
    return record.map(list -> listConverter.convert(list)).orElse(null);
  }


  @Override
  public List<ItemDTO> getItems(long listId) {
    List<Item> items = itemRepository.findByListId(listId);
    return itemConverter.convertAll(items);
  }

  @Override
  public ListDTO addItem(long listId, @NotNull ItemDTO item) {
    Optional<com.trainingsession.listservice.model.entity.List> optionalList = listRepository.findById(listId);
    optionalList.ifPresent(list -> {
      Item entity = itemConverter.reverseConvert(item);
      entity.setListId(listId);
      itemRepository.save(entity);
    });
    return buildListDTO(listId, optionalList);
  }

  @Override
  public ListDTO addItems(long listId, @NotNull List<ItemDTO> items) {
    Optional<com.trainingsession.listservice.model.entity.List> optionalList = listRepository.findById(listId);
    optionalList.ifPresent(list -> {
      List<Item> entities = itemConverter.reverseConvertAll(items);
      entities.forEach(item -> item.setListId(listId));
      itemRepository.saveAll(entities);
    });
    return buildListDTO(listId, optionalList);
  }

  @Override
  public ListDTO updateItem(long listId, long itemId, @NotNull String name, String description) {
    Optional<Item> optionalItem = itemRepository.findOneByListIdAndId(listId, itemId);
    return optionalItem.map(item -> {
      item.setName(name);
      item.setDescription(description);
      itemRepository.save(item);
      return buildListDTO(item.getListId(), listRepository.findById(item.getListId()));
    }).orElse(null);
  }

  @Override
  public ListDTO removeItem(long listId, long itemId) {
    Optional<Item> optionalItem = itemRepository.findOneByListIdAndId(listId, itemId);
    return optionalItem.map(item -> {
      itemRepository.deleteById(item.getId());
      return buildListDTO(item.getListId(), listRepository.findById(item.getListId()));
    }).orElse(null);
  }

  @Override
  public ListDTO removeAllItems(long listId) {
    Optional<com.trainingsession.listservice.model.entity.List> optionalList = listRepository.findById(listId);
    return optionalList.map(list -> {
      List<Item> items = itemRepository.findByListId(list.getId());
      items.forEach(item -> itemRepository.deleteById(item.getId()));
      return buildListDTO(listId, optionalList);
    }).orElse(null);
  }

  private ListDTO buildListDTO(long listId,
      @NotNull Optional<com.trainingsession.listservice.model.entity.List> optionalList) {
    return optionalList.map(list -> {
      ListDTO listDTO = listConverter.convert(optionalList.get());
      listDTO.setItems(itemConverter.convertAll(itemRepository.findByListId(listId)));
      return listDTO;
    }).orElse(null);
  }
}

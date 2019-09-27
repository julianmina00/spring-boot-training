package com.trainingsession.service.impl;

import com.trainingsession.dto.ItemDTO;
import com.trainingsession.dto.ListDTO;
import com.trainingsession.service.ListService;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.validation.constraints.NotNull;
import org.springframework.stereotype.Service;

@Service("listService")
public class ListServiceImpl implements ListService {

  private Map<Long,ListDTO> listRecords = new ConcurrentHashMap<>();
  private static long LIST_ID = 0L;
  private static long ITEM_ID = 0L;

  @Override
  public List<ListDTO> getLists() {
    return new ArrayList<>(listRecords.values());
  }

  @Override
  public ListDTO getList(long id) {
    return listRecords.get(id);
  }

  @Override
  public ListDTO createList(@NotNull ListDTO listDTO) {
    long id = getListId();
    listDTO.setId(id);
    List<ItemDTO> items = listDTO.getItems();
    items.forEach(item -> item.setId(getItemId()));
    listRecords.put(id, listDTO);
    return listRecords.get(id);
  }

  @Override
  public ListDTO createList(@NotNull String name, String description) {
    ListDTO list = new ListDTO();
    list.setName(name);
    list.setDescription(description);
    return createList(list);
  }


  @Override
  public ListDTO updateList(long id, @NotNull String name, String description) {
    ListDTO record = listRecords.get(id);
    if(record != null) {
      record.setName(name);
      record.setDescription(description);
    }
    return record;
  }

  @Override
  public ListDTO deleteList(long id) {
    ListDTO record = listRecords.get(id);
    if(record != null){
      listRecords.remove(id);
    }
    return record;
  }
  @Override
  public List<ItemDTO> getItems(long listId) {
    ListDTO list = listRecords.get(listId);
    return list != null ? list.getItems() : null;
  }

  @Override
  public ListDTO addItem(long listId, @NotNull ItemDTO item) {
    ListDTO list = listRecords.get(listId);
    if(list != null){
      item.setId(getItemId());
      list.getItems().add(item);
    }
    return list;
  }

  @Override
  public ListDTO addItems(long listId, @NotNull List<ItemDTO> items) {
    ListDTO list = listRecords.get(listId);
    if(list != null){
      items.forEach(itemDTO -> itemDTO.setId(getItemId()));
      list.getItems().addAll(items);
    }
    return list;
  }

  @Override
  public ListDTO updateItem(long listId, long itemId, @NotNull String name, String description) {
    ListDTO list = listRecords.get(listId);
    if(list != null){
      List<ItemDTO> items = list.getItems();
      ItemDTO toUpdate = items.stream().filter(itemDTO -> itemDTO.getId().equals(itemId))
          .findFirst().orElse(null);
      if(toUpdate == null){
        return null;
      }
      toUpdate.setName(name);
      toUpdate.setDescription(description);
    }
    return list;
  }

  @Override
  public ListDTO removeItem(long listId, long itemId) {
    ListDTO list = listRecords.get(listId);
    if(list != null){
      list.getItems().removeIf(item -> item.getId().equals(itemId));
    }
    return list;
  }

  @Override
  public ListDTO removeAllItems(long listId) {
    ListDTO list = listRecords.get(listId);
    if(list != null){
      list.getItems().clear();
    }
    return list;
  }

  private static synchronized long getListId(){
    return ++LIST_ID;
  }

  private static synchronized long getItemId(){
    return ++ITEM_ID;
  }
}

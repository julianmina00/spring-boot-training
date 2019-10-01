package com.trainingsession.service;

import com.trainingsession.model.dto.ItemDTO;
import com.trainingsession.model.dto.ListDTO;
import java.util.List;

public interface ListService {

  List<ListDTO> getLists();
  ListDTO getList(long id);
  ListDTO createList(ListDTO listDTO);
  ListDTO createList(String name, String description);
  ListDTO updateList(long id, String name, String description);
  ListDTO deleteList(long id);

  List<ItemDTO> getItems(long listId);
  ListDTO addItem(long listId, ItemDTO item);
  ListDTO addItems(long listId, List<ItemDTO> items);
  ListDTO updateItem(long listId, long itemId, String name, String description);
  ListDTO removeItem(long listId, long itemId);
  ListDTO removeAllItems(long listId);

}

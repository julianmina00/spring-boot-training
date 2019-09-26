package com.trainingsession.service;

import com.trainingsession.dto.ListDTO;
import java.util.List;

public interface ListService {

  List<ListDTO> getLists();
  ListDTO getList(long id);
  ListDTO createList(ListDTO listDTO);
  ListDTO updateList(long id, ListDTO listDTO);
  ListDTO deleteList(long id);

}

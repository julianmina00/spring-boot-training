package com.trainingsession.service.impl;

import com.trainingsession.dto.ListDTO;
import com.trainingsession.service.ListService;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Service;

@Service("listService")
public class ListServiceImpl implements ListService {

  private Map<Long,ListDTO> listRecords = new ConcurrentHashMap<>();
  private static long ID = 0L;

  @Override
  public List<ListDTO> getLists() {
    return new ArrayList<>(listRecords.values());
  }

  @Override
  public ListDTO getList(long id) {
    return listRecords.get(id);
  }

  @Override
  public ListDTO createList(ListDTO listDTO) {
    long id = getId();
    listDTO.setId(id);
    listRecords.put(id, listDTO);
    return listRecords.get(id);
  }

  @Override
  public ListDTO updateList(long id, ListDTO listDTO) {
    ListDTO record = listRecords.get(id);
    if(record != null) {
      record.setName(listDTO.getName());
      record.setDescription(listDTO.getDescription());
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

  private static synchronized long getId(){
    return ++ID;
  }
}

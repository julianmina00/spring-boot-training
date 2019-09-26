package com.trainingsession.rest;


import com.trainingsession.dto.ListDTO;
import com.trainingsession.service.ListService;
import java.util.Collections;
import java.util.List;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rest/list")
public class ListRestController {

  @Autowired
  private ListService listService;

  @GetMapping({"/", ""})
  public ResponseEntity<List<ListDTO>> getLists() {
    List<ListDTO> lists = listService.getLists();
    if(lists == null || lists.isEmpty()){
      return ResponseEntity.noContent().build();
    }
    return ResponseEntity.ok(lists);
  }

  @GetMapping(value = "/{id}")
  public ResponseEntity<ListDTO> getList(@PathVariable Long id) {
    ListDTO list = listService.getList(id);
    if(list == null){
      return ResponseEntity.notFound().build();
    }
    return ResponseEntity.ok(list);
  }

  @PostMapping
  public ResponseEntity<ListDTO> createList(@Valid @RequestBody ListDTO list) {
    if(list == null || list.getId() != null){
      return ResponseEntity.badRequest().build();
    }
    ListDTO createdList = listService.createList(list);
    return ResponseEntity.ok(createdList);
  }

  @PutMapping(value = "/{id}")
  public ResponseEntity<ListDTO> updateList(@PathVariable Long id,
      @Valid @RequestBody ListDTO list) {
    ListDTO updatedList = listService.updateList(id, list);
    if(updatedList == null){
      return ResponseEntity.notFound().build();
    }
    return ResponseEntity.ok(list);
  }

  @DeleteMapping(value = "/{id}")
  public ResponseEntity<ListDTO> deleteList(@PathVariable Long id) {
    ListDTO deletedList = listService.deleteList(id);
    if(deletedList == null){
      return ResponseEntity.notFound().build();
    }
    return ResponseEntity.ok(deletedList);
  }

}

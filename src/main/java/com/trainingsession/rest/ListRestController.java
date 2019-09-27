package com.trainingsession.rest;


import com.trainingsession.dto.ItemDTO;
import com.trainingsession.dto.ListDTO;
import com.trainingsession.service.ListService;
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

  @GetMapping
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

  @PostMapping("/{name}/{description}")
  public ResponseEntity<ListDTO> createList(@PathVariable("name") String name,
      @PathVariable("description") String description) {
    ListDTO createdList = listService.createList(name, description);
    return ResponseEntity.ok(createdList);
  }

  @PostMapping
  public ResponseEntity<ListDTO> createList(@Valid @RequestBody ListDTO list) {
    if(list == null || list.getId() != null){
      return ResponseEntity.badRequest().build();
    }
    ListDTO createdList = listService.createList(list);
    return ResponseEntity.ok(createdList);
  }

  @PutMapping(value = "/{id}/{name}/{description}")
  public ResponseEntity<ListDTO> updateList(@PathVariable("id") Long id, @PathVariable("name") String name,
      @PathVariable("description") String description) {
    ListDTO updatedList = listService.updateList(id, name, description);
    if(updatedList == null){
      return ResponseEntity.notFound().build();
    }
    return ResponseEntity.ok(updatedList);
  }

  @DeleteMapping(value = "/{id}")
  public ResponseEntity<ListDTO> deleteList(@PathVariable Long id) {
    ListDTO deletedList = listService.deleteList(id);
    if(deletedList == null){
      return ResponseEntity.notFound().build();
    }
    return ResponseEntity.ok(deletedList);
  }

  @GetMapping(value = "/{listId}/items")
  public ResponseEntity<List<ItemDTO>> getItems(@PathVariable Long listId) {
    List<ItemDTO> items = listService.getItems(listId);
    if(items == null){
      return ResponseEntity.notFound().build();
    }
    return ResponseEntity.ok(items);
  }

  @PostMapping(value = "/{listId}/item")
  public ResponseEntity<ListDTO> addItem(@PathVariable Long listId, @Valid @RequestBody ItemDTO item) {
    ListDTO listDTO = listService.addItem(listId, item);
    return ResponseEntity.ok(listDTO);
  }

  @PostMapping(value = "/{listId}/items")
  public ResponseEntity<ListDTO> addItems(@PathVariable Long listId, @Valid @RequestBody List<ItemDTO> items) {
    if(items.isEmpty()){
      return ResponseEntity.badRequest().build();
    }
    ListDTO listDTO = listService.addItems(listId, items);
    return ResponseEntity.ok(listDTO);
  }

  @PutMapping(value = "/{listId}/item/{itemId}/{name}/{description}")
  public ResponseEntity<ListDTO> updateItem(@PathVariable("listId") Long listId, @PathVariable("itemId") Long itemId,
      @PathVariable("name") String name, @PathVariable("description") String description) {
    ListDTO list = listService.updateItem(listId, itemId, name, description);
    if(list == null){
      return ResponseEntity.notFound().build();
    }
    return ResponseEntity.ok(list);
  }

  @DeleteMapping(value = "/{listId}/item/{itemId}")
  public ResponseEntity<ListDTO> removeItem(@PathVariable("listId") Long listId, @PathVariable("itemId") Long itemId) {
    ListDTO list = listService.removeItem(listId, itemId);
    if(list == null){
      return ResponseEntity.notFound().build();
    }
    return ResponseEntity.ok(list);
  }

  @DeleteMapping(value = "/{listId}/items")
  public ResponseEntity<ListDTO> removeItems(@PathVariable("listId") Long listId) {
    ListDTO list = listService.removeAllItems(listId);
    if(list == null){
      return ResponseEntity.notFound().build();
    }
    return ResponseEntity.ok(list);
  }

}

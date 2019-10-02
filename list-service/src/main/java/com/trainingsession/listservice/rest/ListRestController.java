package com.trainingsession.listservice.rest;


import com.trainingsession.listservice.model.dto.ItemDTO;
import com.trainingsession.listservice.model.dto.ListDTO;
import com.trainingsession.listservice.service.ListService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
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
  @ApiOperation(value = "Obtains all lists", produces = "application/json", tags = "List Endpoints")
  public ResponseEntity<List<ListDTO>> getLists() {
    List<ListDTO> lists = listService.getLists();
    if(lists == null || lists.isEmpty()){
      return ResponseEntity.noContent().build();
    }
    return ResponseEntity.ok(lists);
  }

  @GetMapping(value = "/{id}")
  @ApiOperation(value = "Obtains the List for the given id", produces = "application/json", tags = "List Endpoints")
  public ResponseEntity<ListDTO> getList(
      @PathVariable("id") @ApiParam(required = true, value = "Id of the List to be displayed", type = "path") Long id) {
    ListDTO list = listService.getList(id);
    if(list == null){
      return ResponseEntity.notFound().build();
    }
    return ResponseEntity.ok(list);
  }

  @PostMapping("/{name}/{description}")
  @ApiOperation(value = "Creates a new List without items", produces = "application/json", tags = "List Endpoints")
  public ResponseEntity<ListDTO> createList(
      @PathVariable("name") @ApiParam(required = true, value = "Name of the List that will be created", type = "path") String name,
      @PathVariable("description") @ApiParam(required = true, value = "A description for the List that will be created", type = "path") String description) {
    ListDTO createdList = listService.createList(name, description);
    return ResponseEntity.ok(createdList);
  }

  @PostMapping
  @ApiOperation(value = "Creates a new List with items", produces = "application/json", tags = "List Endpoints", consumes = "application/json")
  public ResponseEntity<ListDTO> createList(
      @Valid @RequestBody @ApiParam(required = true, value = "A JSON object containing the data of the List and Items to be created", type = "body") ListDTO list) {
    if(list == null || list.getId() != null){
      return ResponseEntity.badRequest().build();
    }
    ListDTO createdList = listService.createList(list);
    return ResponseEntity.ok(createdList);
  }

  @PutMapping(value = "/{id}/{name}/{description}")
  @ApiOperation(value = "Updates the List for the given id", produces = "application/json", tags = "List Endpoints")
  public ResponseEntity<ListDTO> updateList(
      @PathVariable("id") @ApiParam(required = true, value = "Id of the List to be updated", type = "path") Long id,
      @PathVariable("name") @ApiParam(required = true, value = "New name of the List", type = "path") String name,
      @PathVariable("description") @ApiParam(required = true, value = "New description of the List", type = "path") String description) {
    ListDTO updatedList = listService.updateList(id, name, description);
    if(updatedList == null){
      return ResponseEntity.notFound().build();
    }
    return ResponseEntity.ok(updatedList);
  }

  @DeleteMapping(value = "/{id}")
  @ApiOperation(value = "Deletes the List for the given id with all its items", produces = "application/json", tags = "List Endpoints")
  public ResponseEntity<ListDTO> deleteList(
      @PathVariable("id") @ApiParam(required = true, value = "Id of the List to be removed", type = "path") Long id) {
    ListDTO deletedList = listService.deleteList(id);
    if(deletedList == null){
      return ResponseEntity.notFound().build();
    }
    return ResponseEntity.ok(deletedList);
  }

  @GetMapping(value = "/{listId}/items")
  @ApiOperation(value = "Obtains the items of a List", produces = "application/json", tags = "Item Endpoints")
  public ResponseEntity<List<ItemDTO>> getItems(
      @PathVariable("listId") @ApiParam(required = true, value = "Id of the List whose items will be listed", type = "path") Long listId) {
    List<ItemDTO> items = listService.getItems(listId);
    if(items == null){
      return ResponseEntity.notFound().build();
    }
    return ResponseEntity.ok(items);
  }

  @PostMapping(value = "/{listId}/item")
  @ApiOperation(value = "Adds a new item to a List", produces = "application/json", tags = "Item Endpoints")
  public ResponseEntity<ListDTO> addItem(
      @PathVariable("listId") @ApiParam(required = true, value = "Id of the List where the new Item will be added", type = "path") Long listId,
      @Valid @RequestBody @ApiParam(required = true, value = "A JSON object with the structure of the Item to be added", type = "path") ItemDTO item) {
    ListDTO listDTO = listService.addItem(listId, item);
    return ResponseEntity.ok(listDTO);
  }

  @PostMapping(value = "/{listId}/items")
  @ApiOperation(value = "Adds a set of items to a List", produces = "application/json", tags = "Item Endpoints")
  public ResponseEntity<ListDTO> addItems(
      @PathVariable("listId") @ApiParam(required = true, value = "Id of the List where the new Items will be added", type = "path") Long listId,
      @Valid @RequestBody @ApiParam(required = true, value = "A JSON object with set of items to be added", type = "path") List<ItemDTO> items) {
    if(items.isEmpty()){
      return ResponseEntity.badRequest().build();
    }
    ListDTO listDTO = listService.addItems(listId, items);
    return ResponseEntity.ok(listDTO);
  }

  @PutMapping(value = "/{listId}/item/{itemId}/{name}/{description}")
  @ApiOperation(value = "Updates an Item of a List", produces = "application/json", tags = "Item Endpoints")
  public ResponseEntity<ListDTO> updateItem(
      @PathVariable("listId") @ApiParam(required = true, value = "Id of the List containing the Item to be updated", type = "path") Long listId,
      @PathVariable("itemId") @ApiParam(required = true, value = "Id of the Item that will be updated", type = "path") Long itemId,
      @PathVariable("name") @ApiParam(required = true, value = "New name of the Item", type = "path") String name,
      @PathVariable("description") @ApiParam(required = true, value = "New description of the Item", type = "path") String description) {
    ListDTO list = listService.updateItem(listId, itemId, name, description);
    if(list == null){
      return ResponseEntity.notFound().build();
    }
    return ResponseEntity.ok(list);
  }

  @DeleteMapping(value = "/{listId}/item/{itemId}")
  @ApiOperation(value = "Removes an Item of a List", produces = "application/json", tags = "Item Endpoints")
  public ResponseEntity<ListDTO> removeItem(
      @PathVariable("listId") @ApiParam(required = true, value = "Id of the List containing the Item to be removed", type = "path") Long listId,
      @PathVariable("itemId") @ApiParam(required = true, value = "Id of the Item that will be removed", type = "path") Long itemId) {
    ListDTO list = listService.removeItem(listId, itemId);
    if(list == null){
      return ResponseEntity.notFound().build();
    }
    return ResponseEntity.ok(list);
  }

  @DeleteMapping(value = "/{listId}/items")
  @ApiOperation(value = "Removes all items from a List", produces = "application/json", tags = "Item Endpoints")
  public ResponseEntity<ListDTO> removeItems(
      @PathVariable("listId") @ApiParam(required = true, value = "Id of the List whose items will be removed", type = "path") Long listId) {
    ListDTO list = listService.removeAllItems(listId);
    if(list == null){
      return ResponseEntity.notFound().build();
    }
    return ResponseEntity.ok(list);
  }

}

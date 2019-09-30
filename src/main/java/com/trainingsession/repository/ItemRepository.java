package com.trainingsession.repository;

import com.trainingsession.model.entity.Item;
import java.util.List;
import org.springframework.data.repository.CrudRepository;

public interface ItemRepository extends CrudRepository<Item, Long> {

  List<Item> findByListId(Long listId);

}

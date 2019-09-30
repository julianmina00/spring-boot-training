package com.trainingsession.repository;

import com.trainingsession.model.entity.Item;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {

  List<Item> findByListId(Long listId);

  //Optional<Item> findOneByListIdAndId(Long listId, Long id);
  Item findOneByListIdAndId(Long listId, Long id);

  void deleteByListId(Long listId);

}

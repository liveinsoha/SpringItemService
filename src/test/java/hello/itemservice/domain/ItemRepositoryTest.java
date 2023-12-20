package hello.itemservice.domain;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class ItemRepositoryTest {
    ItemRepository itemRepository = ItemRepository.getInstance();

    @AfterEach
    void afterEach() {
        itemRepository.clear();
    }

    @Test
    void save() {
        Item item = new Item("asd", 123, 23);
        itemRepository.save(item);
        Item findItem = itemRepository.findById(item.getId());
        assertThat(findItem).isEqualTo(item);
    }

    @Test
    void delete() {

        Item item = new Item("asd", 123, 23);
        itemRepository.save(item);
        Item removedItem = itemRepository.delete(item.getId());
        assertThat(removedItem).isEqualTo(item);

    }

    @Test
    void update() {
        Item item = new Item("asd", 123, 23);
        itemRepository.save(item);
        itemRepository.update(item.getId(), "qwe", 100, 10);
        Item updatedItem = itemRepository.findById(item.getId());

        assertThat(updatedItem.getName()).isEqualTo("qwe");
        assertThat(updatedItem.getPrice()).isEqualTo(100);
        assertThat(updatedItem.getquantity()).isEqualTo(10);
    }
}
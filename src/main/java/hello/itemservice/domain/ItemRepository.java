package hello.itemservice.domain;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class ItemRepository {

    private static ItemRepository instance = new ItemRepository();
    private static Map<Long, Item> store = new HashMap<>();
    private static Long sequence = 0L;

    public static ItemRepository getInstance() {
        return instance;
    }

    public Long save(Item item) {
        log.info("info log : save");
        store.put(++sequence, item);
        item.setId(sequence);
        return item.getId();
    }

    public Item findById(Long itemId) {
        validateHasItem(itemId);
        return store.get(itemId);
    }

    public Item delete(Long itemId) {
        validateHasItem(itemId);
        return store.remove(itemId);
    }

    public Item update(Long itemId, String name, int price, int quantity) {
        Item item = store.get(itemId);
        item.updateItem(name, price, quantity);
        return item;
    }

    private static void validateHasItem(Long itemId) {
        if (!store.containsKey(itemId)) {
            throw new IllegalArgumentException("[ERROR] 해당 아이템 없음");
        }
    }

    public List<Item> findAll() {
        return new ArrayList<>(store.values());
    }

    public void clear() {
        store.clear();
    }
}

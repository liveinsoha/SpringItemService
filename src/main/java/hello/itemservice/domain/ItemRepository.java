package hello.itemservice.domain;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Repository //컴포넌트 스캔의 대상이 된다
public class ItemRepository {

    //스프링을 쓰면 싱글톤을 보장해주기 떄문에 static을 안써도 되지만, 다른데서 new해서 사용하는 경우를 대비해 static을 사용한다.
    /**
     * 실제 서비스에서는 여러 스레드에서 접근하기 때문에 atomic Long, ConcurrentHashMAp을 사용한다.
     */
    private static final Map<Long, Item> store = new HashMap<>();
    private static Long sequence = 0L;


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

    //update할 파라미터를 넘기는 DTO를 사용하거나..
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

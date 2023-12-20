package hello.itemservice.domain;

import hello.itemservice.ItemForm;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@Slf4j
public class ItemController {

    ItemRepository itemRepository = ItemRepository.getInstance();

    @GetMapping("/basic/items") //상품 목록
    public String items(Model model) {
        log.info("log info : items , method : get");
        List<Item> items = itemRepository.findAll();
        model.addAttribute("items", items);
        return "basic/items";
    }

    @GetMapping("/basic/items/add") //상품 등록
    public String createItemForm() {
        log.info("log info : create items method = get");
        return "basic/addForm";
    }

    @PostMapping("/basic/items/add") //등록완료
    public String createItem(@ModelAttribute ItemForm itemForm) {
        log.info("log info : create items method = post");
        Item item = new Item(itemForm.getItemName(), itemForm.getPrice(), itemForm.getQuantity());
        Long savedId = itemRepository.save(item);
        return "redirect:/basic/items/" + savedId;
    }

    @GetMapping("/basic/items/{itemId}")//상품 상세
    public String itemDetail(@PathVariable Long itemId, Model model) {
        log.info("log info : Detail items method = get");
        Item findItem = itemRepository.findById(itemId); //DTO
        model.addAttribute("item", findItem);
        return "basic/item";
    }

    @GetMapping("/basic/items/{itemId}/edit")//상품 수정 폼
    public String updateItemForm(@PathVariable Long itemId, Model model) {
        log.info("log info : updateItemForm method = get");
        Item findItem = itemRepository.findById(itemId);
        model.addAttribute("item", findItem);
        return "basic/editForm";
    }

    @PostMapping("/basic/items/{itemId}/edit")
    public String updateItem(@PathVariable Long itemId, @ModelAttribute ItemForm itemForm) {
        log.info("log info : updateItem method = post");
        itemRepository.update(itemId, itemForm.getItemName(), itemForm.getPrice(), itemForm.getQuantity());
        return "redirect:/basic/items/" + itemId;
    }
}

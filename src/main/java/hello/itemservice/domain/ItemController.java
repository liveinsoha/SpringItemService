package hello.itemservice.domain;

import hello.itemservice.ItemForm;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/basic/items")
public class ItemController {

    /**
     * public BasicItemController(ItemRepository itemRepository) {
     * this.itemRepository = itemRepository;
     * 이렇게 생성자가 딱 1개만 있으면 스프링이 해당 생성자에 `@Autowired` 로 의존관계를 주입해준다.
     * 따라서 **final 키워드를 빼면 안된다!**, 그러면 `ItemRepository` 의존관계 주입이 안된다.
     */
    private final ItemRepository itemRepository;

    @GetMapping //상품 목록
    public String items(Model model) {
        log.info("log info : items , method : get");
        List<Item> items = itemRepository.findAll();
        model.addAttribute("items", items);
        return "basic/items";
    }

    /**
     * 테스트용 데이터가 없으면 회원 목록 기능이 정상 동작하는지 확인하기 어렵다.
     * `@PostConstruct` : 해당 빈의 의존관계가 모두 주입되고 나면 초기화 용도로 호출된다.
     * 여기서는 간단히 테스트용 테이터를 넣기 위해서 사용했다.
     */
    @PostConstruct
    public void init() {
        itemRepository.save(new Item("ItemA", 1000, 10));
        itemRepository.save(new Item("ItemB", 2000, 10));
    }

    @GetMapping("/add") //상품 등록 폼 보여만 주기.
    public String addForm() {
        log.info("log info : create items method = get");
        return "basic/addForm";
    }

    //@PostMapping("/add") //등록하기
    public String addItemV1(@RequestParam String itemName,
                            @RequestParam Integer price,
                            @RequestParam Integer quantity, Model model) {
        log.info("log info : addItemV1 method = post");
        Item item = new Item(itemName, price, quantity);
        itemRepository.save(item);
        model.addAttribute("item", item);
        return "basic/item"; //저장하고 저장 객체의 정보를 모델에 담아 상품상세 뷰로 이동
    }

    /**
     * **@ModelAttribute - 요청 파라미터 처리**
     * `@ModelAttribute` 는 `Item` 객체를 생성하고, 요청 파라미터의 값을 프로퍼티 접근법(setXxx)으로 입력해준다.
     * **@ModelAttribute - Model 추가**
     * `@ModelAttribute` 는 중요한 한가지 기능이 더 있는데, 바로 모델(Model)에 `@ModelAttribute` 로 지정한 객체
     * 를 자동으로 넣어준다. 지금 코드를 보면 `model.addAttribute("item", item)` 가 주석처리 되어 있어도 잘 동
     * 작하는 것을 확인할 수 있다.
     * 모델에 데이터를 담을 때는 이름이 필요하다. 이름은 `@ModelAttribute` 에 지정한 `name(value)` 속성을 사용한
     * 다. 만약 다음과 같이 `@ModelAttribute` 의 이름을 다르게 지정하면 다른 이름으로 모델에 포함된다.
     * `@ModelAttribute("hello") Item item` 이름을 `hello` 로 지정
     * `model.addAttribute("hello", item);` 모델에 `hello` 이름으로 저장
     * **
     */
    @PostMapping("/add") //등록하기
    public String addItemV2(@ModelAttribute("item") Item item, Model model) {
        log.info("log info : addItemV2 method = post");
        //Item item = new Item(itemForm.getItemName(), itemForm.getPrice(), itemForm.getQuantity());
        itemRepository.save(item);
        //model.addAttribute("item", item);
        return "basic/item"; //저장하고 저장 객체의 정보를 모델에 담아 상품상세 뷰로 이동
    }

    @GetMapping("/{itemId}")//상품 상세 Gradle로 빌드해야 PathVariable value속성 생략가능핮다.
    public String itemDetail(@PathVariable Long itemId, Model model) {
        log.info("log info : Detail items method = get");
        Item findItem = itemRepository.findById(itemId); //DTO 사용해보기
        model.addAttribute("item", findItem);
        return "basic/item";
    }

    @GetMapping("/{itemId}/edit")//상품 수정 폼
    public String updateItemForm(@PathVariable Long itemId, Model model) {
        log.info("log info : updateItemForm method = get");
        Item findItem = itemRepository.findById(itemId);
        model.addAttribute("item", findItem);
        return "basic/editForm";
    }

    @PostMapping("/{itemId}/edit")
    public String updateItem(@PathVariable Long itemId, @ModelAttribute ItemForm itemForm) {
        log.info("log info : updateItem method = post");
        itemRepository.update(itemId, itemForm.getItemName(), itemForm.getPrice(), itemForm.getQuantity());
        return "redirect:/basic/items/" + itemId;
    }
}

package hello.itemservice;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
public class ItemForm {

    private String itemName;
    private Integer price;
    private Integer quantity;

}

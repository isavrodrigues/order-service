package store.order;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

@Builder @Data @Accessors(fluent = true, chain = true)
public class OrderItem {

    private String productId;
    private Integer quantity;
    private Double price;

}

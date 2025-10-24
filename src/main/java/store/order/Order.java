package store.order;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

@Builder
@Data
@Accessors(fluent = true, chain = true)
public class Order {

    private String id;
    private String accountId;
    private LocalDateTime date;
    private List<OrderItem> items;
    private Double total;

}


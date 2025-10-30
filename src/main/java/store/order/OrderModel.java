package store.order;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Entity
@Table(name = "orders")
@Setter @Accessors(chain = true, fluent = true)
@NoArgsConstructor @AllArgsConstructor
public class OrderModel {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "id_user")
    private String accountId;

    @Column(name = "date")
    private LocalDateTime date;

    @Column(name = "items_json", length = 4000)
    private String itemsJson;

    @Column(name = "total")
    private Double total;

    public OrderModel(Order o) {
        this.accountId = o.accountId();
        this.date = o.date();
        this.itemsJson = OrderParser.writeItemsJson(o.items());
        this.total = o.total();
    }

    public Order to() {
        return Order.builder()
            .id(this.id)
            .accountId(this.accountId)
            .date(this.date)
            .items(OrderParser.parseItemsJson(this.itemsJson))
            .total(this.total)
            .build();
    }
}

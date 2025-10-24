package store.order;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class OrderService {

    private final OrderRepository repository;

    public OrderService(OrderRepository repository) {
        this.repository = repository;
    }

    public Order create(Order in, String currentAccountId) {
        var total = in.items() == null ? 0.0 :
            in.items().stream()
                .mapToDouble(i -> i.price() * i.quantity())
                .sum();

        var order = Order.builder()
            .id(UUID.randomUUID().toString())
            .accountId(currentAccountId)
            .date(LocalDateTime.now())
            .items(in.items())
            .total(total)
            .build();

        repository.save(new OrderModel(order));
        return order;
    }

    public Optional<Order> findById(String id) {
        return repository.findById(id).map(OrderModel::to);
    }
}

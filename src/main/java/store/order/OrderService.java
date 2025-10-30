package store.order;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import store.product.ProductController;
import store.product.ProductOut;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class OrderService {

    private final OrderRepository repository;
    private final ProductController productController;

    public OrderService(OrderRepository repository, ProductController productController) {
        this.repository = repository;
        this.productController = productController;
    }

    public OrderOut create(OrderIn in, String currentAccountId) {
        if (in.items() == null || in.items().isEmpty()) {
            throw new IllegalArgumentException("Order must have at least one item");
        }

        List<OrderItem> orderItems = new ArrayList<>();
        List<OrderItemOut> orderItemsOut = new ArrayList<>();
        BigDecimal total = BigDecimal.ZERO;

        for (OrderItemIn itemIn : in.items()) {
            // Fetch product from product-service
            ResponseEntity<ProductOut> response = productController.findById(itemIn.idProduct());

            if (response == null || !response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
                throw new IllegalArgumentException("Product does not exist: " + itemIn.idProduct());
            }

            ProductOut product = response.getBody();
            if (product.price() == null) {
                throw new IllegalArgumentException("Product price is missing: " + itemIn.idProduct());
            }

            // Calculate item total
            BigDecimal price = new BigDecimal(product.price());
            BigDecimal quantity = new BigDecimal(itemIn.quantity());
            BigDecimal itemTotal = price.multiply(quantity).setScale(2, RoundingMode.HALF_UP);
            total = total.add(itemTotal);

            // Create OrderItem for persistence
            OrderItem orderItem = OrderItem.builder()
                .productId(itemIn.idProduct())
                .quantity(itemIn.quantity())
                .price(product.price())
                .build();

            orderItems.add(orderItem);

            // Build OrderItemOut for response
            OrderItemOut itemOut = OrderItemOut.builder()
                .id(null) // Order items don't have separate IDs in this schema
                .product(product)
                .quantity(itemIn.quantity())
                .total(itemTotal.doubleValue())
                .build();

            orderItemsOut.add(itemOut);
        }

        var order = Order.builder()
            .accountId(currentAccountId)
            .date(LocalDateTime.now())
            .items(orderItems)
            .total(total.doubleValue())
            .build();

        var saved = repository.save(new OrderModel(order));

        return OrderOut.builder()
            .id(saved.to().id())
            .date(saved.to().date().toString())
            .items(orderItemsOut)
            .total(saved.to().total())
            .build();
    }

    public List<OrderOut> findAll(String accountId) {
        // TODO: Implementar filtro por accountId quando tiver autenticação
        List<OrderOut> orders = new ArrayList<>();
        for (OrderModel model : repository.findAll()) {
            Order order = model.to();
            OrderOut out = OrderOut.builder()
                .id(order.id())
                .date(order.date().toString())
                .items(null) // Não retorna items no findAll
                .total(order.total())
                .build();
            orders.add(out);
        }
        return orders;
    }

    public Optional<OrderOut> findById(String id) {
        return repository.findById(id).map(model -> {
            Order order = model.to();

            List<OrderItemOut> itemsOut = new ArrayList<>();
            for (OrderItem item : order.items()) {
                ResponseEntity<ProductOut> productResponse = productController.findById(item.productId());
                ProductOut product = productResponse != null && productResponse.getBody() != null
                    ? productResponse.getBody()
                    : null;

                BigDecimal itemTotal = new BigDecimal(item.price())
                    .multiply(new BigDecimal(item.quantity()))
                    .setScale(2, RoundingMode.HALF_UP);

                OrderItemOut itemOut = OrderItemOut.builder()
                    .id(null)
                    .product(product)
                    .quantity(item.quantity())
                    .total(itemTotal.doubleValue())
                    .build();

                itemsOut.add(itemOut);
            }

            return OrderOut.builder()
                .id(order.id())
                .date(order.date().toString())
                .items(itemsOut)
                .total(order.total())
                .build();
        });
    }
}

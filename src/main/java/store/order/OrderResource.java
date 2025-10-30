package store.order;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/order")
@Validated
public class OrderResource {

    private final OrderService service;

    public OrderResource(OrderService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<OrderOut> create(@RequestBody OrderIn in) {
        var created = service.create(in, "account-1"); // sem autenticação real
        return ResponseEntity.created(URI.create("/order/" + created.id())).body(created);
    }

    @GetMapping
    public ResponseEntity<java.util.List<OrderOut>> findAll() {
        var orders = service.findAll("account-1"); // sem autenticação real
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderOut> findById(@PathVariable("id") String id) {
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}

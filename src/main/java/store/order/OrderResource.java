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
    public ResponseEntity<Order> create(@RequestBody Order in) {
        var created = service.create(in, "account-1"); // sem autenticação real
        return ResponseEntity.created(URI.create("/order/" + created.id())).body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Order> findById(@PathVariable("id") String id) {
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}

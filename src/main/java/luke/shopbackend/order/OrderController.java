package luke.shopbackend.order;

import luke.shopbackend.order.model.dto.CustomerOrderRequest;
import luke.shopbackend.order.model.entity.Customer;
import luke.shopbackend.order.model.entity.CustomerOrder;
import luke.shopbackend.order.service.OrderService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.net.URI;

@RestController
@RequestMapping("/api/order")
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    /**
     * Only for ROLE_ADMIN.
     */
    @GetMapping
    public ResponseEntity<Page<CustomerOrder>> getAll(
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "size", defaultValue = "20") int size){
        Pageable pageable = PageRequest.of(page, size);
        Page<CustomerOrder> customerOrderPage = orderService.getAllPageable(pageable);
        return ResponseEntity.ok().body(customerOrderPage);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerOrder> getOrderById(@PathVariable Long id){
        CustomerOrder order = orderService.getOrder(id);
        order.setCustomer(orderService.getFakeCustomerData());
        return ResponseEntity.ok().body(order);
    }

    @PostMapping
    public ResponseEntity<CustomerOrder> saveOrder(@Valid @RequestBody CustomerOrderRequest orderRequest){
        CustomerOrder order = orderService.addOrder(orderRequest);

        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("{id}")
                .buildAndExpand(order.getOrderId())
                .toUri();

        return ResponseEntity.created(uri).body(order);
    }
}

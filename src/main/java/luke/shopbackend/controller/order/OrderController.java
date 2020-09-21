package luke.shopbackend.controller.order;

import luke.shopbackend.controller.order.service.OrderService;
import luke.shopbackend.model.data_transfer.CustomerOrderRequest;
import luke.shopbackend.model.entity.CustomerOrder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/order")
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<?> saveOrder(@RequestBody CustomerOrderRequest orderRequest){
        orderService.addOrder(orderRequest);
        return ResponseEntity.ok(orderRequest);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerOrder> getOrderById(@PathVariable Long id){
        CustomerOrder order = orderService.getOrder(id);
        return ResponseEntity.ok().body(order);
    }
}

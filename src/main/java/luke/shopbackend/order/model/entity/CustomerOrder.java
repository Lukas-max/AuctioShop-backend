package luke.shopbackend.order.model.entity;
import luke.shopbackend.order.model.dto.CustomerOrderRequest;
import luke.shopbackend.order.model.embeddable.CartItem;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;

@Entity
@Table(name = "customer_order")
public class CustomerOrder implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long orderId;

    @ElementCollection
    @CollectionTable(name = "cart_items", joinColumns = @JoinColumn(name = "order_id"))
    private List<CartItem> cartItems;

    @Column(name = "total_price")
    private BigDecimal totalPrice;

    @Column(name = "total_quantity")
    private Integer totalQuantity;

    @ManyToOne(cascade = CascadeType.ALL)
//    @JsonBackReference
    @JoinColumn(name = "customer_id")
    private Customer customer;

    public CustomerOrder() {
    }

    public CustomerOrder(List<CartItem> items , CustomerOrderRequest request) {
        this.cartItems = new LinkedList<>(items);
        this.totalPrice = request.getTotalPrice();
        this.totalQuantity = request.getTotalQuantity();
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public List<CartItem> getCartItems() {
        return cartItems;
    }

    public void setCartItems(List<CartItem> cartItems) {
        this.cartItems = cartItems;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Integer getTotalQuantity() {
        return totalQuantity;
    }

    public void setTotalQuantity(Integer totalQuantity) {
        this.totalQuantity = totalQuantity;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    @Override
    public String toString() {
        return "CustomerOrder{" +
                "orderId=" + orderId +
                ", cartItems=" + cartItems +
                ", totalPrice=" + totalPrice +
                ", totalQuantity=" + totalQuantity +
                ", customerId=" + customer.getCustomerId() +
                '}';
    }
}

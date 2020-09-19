package luke.shopbackend.model.embeddable;

import luke.shopbackend.model.data_transfer.CartItemValidateDto;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class CartItem {

    @Column(name = "product_id")
    private Long productId;

    @Column(name = "product_name")
    private String name;

    @Column(name = "quantity")
    private int quantity;

    public CartItem() {
    }

    public CartItem(CartItemValidateDto dto) {
        this.productId = dto.getProductId();
        this.name = dto.getName();
        this.quantity = dto.getQuantity();
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "CartItemDto{" +
                "productId=" + productId +
                ", name='" + name + '\'' +
                ", quantity=" + quantity +
                '}';
    }
}

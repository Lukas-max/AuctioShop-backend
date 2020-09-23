package luke.shopbackend.model.data_transfer;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Arrays;

public class CustomerOrderRequest {
    private Long clientId;
    @NotNull(message = "Brak danych klienta.")
    private CustomerDto customer;
    @NotNull(message = "Brak koszyka z zakupami.")
    private CartItemValidateDto[] items;
    @Min(value = 1, message = "Niepoprawna cena")
    private BigDecimal totalPrice;
    @Min(value = 1, message = "Niepoprawna ilość przedmiotów w koszyku.")
    private int totalQuantity;

    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    public CustomerDto getCustomer() {
        return customer;
    }

    public void setCustomer(CustomerDto customer) {
        this.customer = customer;
    }

    public CartItemValidateDto[] getItems() {
        return items;
    }

    public void setItems(CartItemValidateDto[] items) {
        this.items = items;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public int getTotalQuantity() {
        return totalQuantity;
    }

    public void setTotalQuantity(int totalQuantity) {
        this.totalQuantity = totalQuantity;
    }

    @Override
    public String toString() {
        return "ClientOrderRequest{" +
                "clientId=" + clientId +
                ", customer=" + customer +
                ", items=" + Arrays.toString(items) +
                ", totalPrice=" + totalPrice +
                ", totalQuantity=" + totalQuantity +
                '}';
    }
}

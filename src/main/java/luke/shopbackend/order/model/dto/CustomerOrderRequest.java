package luke.shopbackend.order.model.dto;


import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.Arrays;

public class CustomerOrderRequest {
    @NotNull(message = "Brak danych klienta.")
    private CustomerDto customer;
    @NotNull(message = "Brak koszyka z zakupami.")
    private CartItemValidateDto[] items;
    @Min(value = 1, message = "Niepoprawna cena")
    private BigDecimal totalPrice;
    @Min(value = 1, message = "Niepoprawna ilość przedmiotów w koszyku.")
    private int totalQuantity;
    @Size(min = 3, max = 45, message = "Nazwa użytkownika nie może mieć mniej niż 3 znaki i więcej niż 45 znaków")
    private String username;


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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        return "CustomerOrderRequest{" +
                "customer=" + customer +
                ", items=" + Arrays.toString(items) +
                ", totalPrice=" + totalPrice +
                ", totalQuantity=" + totalQuantity +
                ", username='" + username + '\'' +
                '}';
    }
}

package luke.shopbackend.model;


import java.math.BigDecimal;
import java.sql.Timestamp;

public class ProductRequest {

    private String sku;
    private String name;
    private String description;
    private BigDecimal unitPrice;
    private String productImage;
    private boolean active;
    private int unitsInStock;
    private Timestamp dateTimeCreated;
    private Timestamp dateTimeUpdated;
    private Long productCategoryId;

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public int getUnitsInStock() {
        return unitsInStock;
    }

    public void setUnitsInStock(int unitsInStock) {
        this.unitsInStock = unitsInStock;
    }

    public Timestamp getDateTimeCreated() {
        return dateTimeCreated;
    }

    public void setDateTimeCreated(Timestamp dateTimeCreated) {
        this.dateTimeCreated = dateTimeCreated;
    }

    public Timestamp getDateTimeUpdated() {
        return dateTimeUpdated;
    }

    public void setDateTimeUpdated(Timestamp dateTimeUpdated) {
        this.dateTimeUpdated = dateTimeUpdated;
    }

    public Long getProductCategoryId() {
        return productCategoryId;
    }

    public void setProductCategoryId(Long productCategoryId) {
        this.productCategoryId = productCategoryId;
    }


    @Override
    public String toString() {
        return "ProductRequest{" +
                "sku='" + sku + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", unitPrice=" + unitPrice +
                ", productImage='" + productImage + '\'' +
                ", active=" + active +
                ", unitsInStock=" + unitsInStock +
                ", dateTimeCreated=" + dateTimeCreated +
                ", dateTimeUpdated=" + dateTimeUpdated +
                ", productCategoryId=" + productCategoryId +
                '}';
    }
}

package luke.shopbackend.exception.model;

import java.sql.Timestamp;

public class ProductNotFoundResponse {
    private int status;
    private String message;
    private Timestamp timestamp;

    public ProductNotFoundResponse() {
    }

    public ProductNotFoundResponse(int status, String message, Timestamp timestamp) {
        this.status = status;
        this.message = message;
        this.timestamp = timestamp;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }
}

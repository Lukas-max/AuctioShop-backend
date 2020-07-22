package luke.shopbackend.exception;

import javassist.NotFoundException;
import luke.shopbackend.exception.model.ProductNotFoundResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.sql.Timestamp;

@ControllerAdvice
public class ProductRestExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ProductNotFoundResponse> handleNotFoundException(
            NotFoundException exception
    ) {
        ProductNotFoundResponse response = new ProductNotFoundResponse();
        response.setStatus(HttpStatus.NOT_FOUND.value());
        response.setMessage(exception.getMessage());
        response.setTimestamp(new Timestamp(System.currentTimeMillis()));

        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }
}

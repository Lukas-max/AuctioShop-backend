package luke.shopbackend.exception;

import luke.shopbackend.exception.model.ErrorValidationResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
public class CustomResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {


    /**
     *
     * @return global exception response for wrong validation
     * Message is sent to null intentionally. The user gets the error from validationErrors
     * field.
     */
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest request) {

        List<String> errors = new ArrayList<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()){
            errors.add(error.getDefaultMessage());
        }

        ErrorValidationResponse response = new ErrorValidationResponse();
        response.setStatus(HttpStatus.BAD_REQUEST);
        response.setTimestamp(new Timestamp(System.currentTimeMillis()));
        response.setValidationErrors(errors);

        return handleExceptionInternal(ex, response, headers, response.getStatus(), request);
    }

    /**
     * Second implementation of handleMethodArgumentNotValid. Done and left here for training
     * purpose.
     */
//    @Override
//    protected ResponseEntity<Object> handleMethodArgumentNotValid(
//            MethodArgumentNotValidException ex,
//            HttpHeaders headers,
//            HttpStatus status,
//            WebRequest request) {
//
//        List<String> errors = ex.getBindingResult()
//                .getAllErrors()
//                .stream()
//                .map(e -> e.getDefaultMessage())
//                .collect(Collectors.toList());
//
//        Map<String, Object> body = new HashMap<>();
//        body.put("timestamp", new Timestamp(System.currentTimeMillis()));
//        body.put("status", status.value());
//        body.put("validationErrors", errors);
//
//        return new ResponseEntity<>(body, headers, status);
//    }
}

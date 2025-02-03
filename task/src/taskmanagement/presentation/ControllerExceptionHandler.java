package taskmanagement.presentation;

import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ControllerExceptionHandler{

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Object> handleConstraintException(Exception e) {
        return new ResponseEntity<>(null, HttpStatusCode.valueOf(400));
    }

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<Object> handleNullException(Exception e){
        return new ResponseEntity<>(null, HttpStatusCode.valueOf(400));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Object> handleIllegalArgument(Exception e){
        return new ResponseEntity<>(null, HttpStatusCode.valueOf(400));
    }
}
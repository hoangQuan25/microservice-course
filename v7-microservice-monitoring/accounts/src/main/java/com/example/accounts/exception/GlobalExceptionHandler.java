package com.example.accounts.exception;

import com.example.accounts.dto.ErrorResponseDto;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    
    
    /**
     * Overrides the default handling of `MethodArgumentNotValidException` to provide a custom response for validation errors.
     * 
     * This method extracts all validation errors from the exception, collects them in a map where the keys are the
     * field names and the values are the corresponding error messages, and returns this map as the response body.
     * 
     * @param ex the exception containing information about the validation errors
     * @param headers the HTTP headers to be sent in the response
     * @param status the HTTP status code
     * @param request the web request during which the exception was raised
     * @return a ResponseEntity containing a map of validation errors and an HTTP 400 Bad Request status
     */
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatusCode status,
                                                                  WebRequest request) {
        Map<String, String> validationErrors = new HashMap<>();
        List<ObjectError> validationErrorsList = ex.getBindingResult().getAllErrors();
    
        validationErrorsList.forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            validationErrors.put(fieldName, errorMessage);
        });
        
        return new ResponseEntity<>(validationErrors, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles the exception when a customer already exists in the system.
     *
     * @param ex the exception thrown when attempting to create a duplicate customer
     * @param webRequest the current web request from which the exception was triggered
     * @return a ResponseEntity containing an ErrorResponseDto with details
     *         about the error and an HTTP 400 Bad Request status
     */
    @ExceptionHandler(CustomerAlreadyExistsException.class)
    public ResponseEntity<ErrorResponseDto> handleCustomerAlreadyExistsException(
            CustomerAlreadyExistsException ex,
            WebRequest webRequest
    ) {
        ErrorResponseDto errorResponseDto = new ErrorResponseDto(
                webRequest.getDescription(false),
                HttpStatus.BAD_REQUEST,
                ex.getMessage(),
                LocalDateTime.now()
        );
    
        return new ResponseEntity<>(errorResponseDto, HttpStatus.BAD_REQUEST);
    }
    
    
    /**
     * Handles the exception when a requested resource is not found in the system.
     *
     * @param ex the exception thrown when the requested resource cannot be found
     * @param webRequest the current web request from which the exception was triggered
     * @return a ResponseEntity containing an ErrorResponseDto with details
     *         about the error and an HTTP 404 Not Found status
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handleResourceNotFoundException(
            ResourceNotFoundException ex,
            WebRequest webRequest
    ) {
        ErrorResponseDto errorResponseDto = new ErrorResponseDto(
                webRequest.getDescription(false),
                HttpStatus.NOT_FOUND,
                ex.getMessage(),
                LocalDateTime.now()
        );
    
        return new ResponseEntity<>(errorResponseDto, HttpStatus.NOT_FOUND);
    }
    
    /**
     * Handles any unspecified exceptions that occur in the system.
     *
     * @param ex the exception that was thrown
     * @param webRequest the current web request from which the exception was triggered
     * @return a ResponseEntity containing an ErrorResponseDto with details
     *         about the error and an HTTP 500 Internal Server Error status
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDto> handleGlobalException(
            Exception ex,
            WebRequest webRequest
    ) {
        ErrorResponseDto errorResponseDto = new ErrorResponseDto(
                webRequest.getDescription(false),
                HttpStatus.INTERNAL_SERVER_ERROR,
                ex.getMessage(),
                LocalDateTime.now()
        );

        return new ResponseEntity<>(errorResponseDto, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    
}

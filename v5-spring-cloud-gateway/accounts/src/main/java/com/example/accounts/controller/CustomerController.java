package com.example.accounts.controller;

import com.example.accounts.dto.CustomerDetailsDto;
import com.example.accounts.dto.ErrorResponseDto;
import com.example.accounts.service.CustomersService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api", produces = {MediaType.APPLICATION_JSON_VALUE})
@RequiredArgsConstructor
@Validated
@Tag(name = "Customer API", description = "API for performing CRUD on customer detail")
public class CustomerController {

    private final CustomersService customersService;
    public static final Logger logger = LoggerFactory.getLogger(CustomerController.class);

    @Operation(summary = "Fetch customer details",
            description = "Fetch customer details for an existing customer.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Details fetched successfully."),
            @ApiResponse(responseCode = "400", description = "Invalid input provided."),
            @ApiResponse(
                    responseCode = "500",
                    description = "Unexpected error occurred.",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponseDto.class)
                    )
            )
    })
    @GetMapping("/fetchCustomerDetails")
    public ResponseEntity<CustomerDetailsDto> fetchCustomerDetails(
            @RequestHeader("mybank-correlation-id") String correlationId,
            @RequestParam @Pattern(regexp = "\\d{10}",
            message = "Mobile number must be exactly 10 digits") String mobileNumber) {
        logger.debug("MyBank correlation id found: {}", correlationId);
        CustomerDetailsDto customerDetailsDto = customersService.fetchCustomerDetails(mobileNumber, correlationId);
        return ResponseEntity.status(HttpStatus.OK).body(customerDetailsDto);
    }
}

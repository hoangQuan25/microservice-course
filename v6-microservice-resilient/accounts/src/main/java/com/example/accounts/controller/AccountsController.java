package com.example.accounts.controller;

import com.example.accounts.constants.AccountsConstants;
import com.example.accounts.dto.AccountsContactInfoDto;
import com.example.accounts.dto.CustomerDto;
import com.example.accounts.dto.ErrorResponseDto;
import com.example.accounts.dto.ResponseDto;
import com.example.accounts.service.AccountsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.annotation.Validated;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import io.swagger.v3.oas.annotations.tags.Tag;


@RestController
@RequestMapping(path = "/api", produces = {MediaType.APPLICATION_JSON_VALUE})
@RequiredArgsConstructor
@Validated
@Tag(name = "Accounts API", description = "API for performing CRUD on customer accounts")
public class AccountsController {

    private final AccountsService accountsService;
    private final Environment environment;
    private final AccountsContactInfoDto accountsContactInfoDto;

    @Value("${build.version}")
    private String buildVersion;

    
    @Operation(summary = "Create a new customer account",
            description = "Endpoint to create a new account for a customer.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Account successfully created."),
            @ApiResponse(responseCode = "400", description = "Invalid input provided."),
            @ApiResponse(
                    responseCode = "500",
                    description = "Unexpected error occurred.",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponseDto.class)
                    )
            )
    })
    @PostMapping("/create")
    public ResponseEntity<ResponseDto> createAccount(@Valid @RequestBody CustomerDto customerDto) {

        accountsService.createAccount(customerDto);
        return ResponseEntity
                .status(HttpStatus.CREATED) //201 resource created successfully
                .body(new ResponseDto(AccountsConstants.STATUS_201, AccountsConstants.MESSAGE_201));
    }


    @Operation(summary = "Fetch customer account by mobile number",
            description = "Retrieve customer account details using a 10-digit mobile number.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Customer account retrieved successfully."),
            @ApiResponse(responseCode = "400", description = "Invalid input provided."),
            @ApiResponse(
                    responseCode = "500",
                    description = "Unexpected error occurred.",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponseDto.class)
                    )
            )
    })
    @GetMapping("/fetch")
    public ResponseEntity<CustomerDto> fetchAccount(@RequestParam @Pattern(regexp = "\\d{10}",
            message = "Mobile number must be exactly 10 digits") String mobileNumber) {
        CustomerDto customerDto = accountsService.fetchAccount(mobileNumber);
        return ResponseEntity.status(HttpStatus.OK).body(customerDto);
    }


    @Operation(summary = "Update customer account details",
            description = "Update account details for an existing customer.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Account updated successfully."),
            @ApiResponse(responseCode = "400", description = "Invalid input provided."),
            @ApiResponse(
                    responseCode = "500",
                    description = "Unexpected error occurred.",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponseDto.class)
                    )
            )
    })
    @PutMapping("/update")
    public ResponseEntity<ResponseDto> updateAccount(@Valid @RequestBody CustomerDto customerDto) {
        boolean updated = accountsService.updateAccount(customerDto);
        if (updated) {
            return ResponseEntity
                .status(HttpStatus.OK) // 200 resource updated successfully
                .body(new ResponseDto(AccountsConstants.STATUS_200, AccountsConstants.MESSAGE_200));
        } else {
            return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR) // 500 resource update failed
                .body(new ResponseDto(AccountsConstants.STATUS_500, AccountsConstants.MESSAGE_500));
        }
    }


    @Operation(summary = "Delete customer account by mobile number", 
            description = "Delete a customer account using a 10-digit mobile number.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Account deleted successfully."),
            @ApiResponse(responseCode = "400", description = "Invalid input provided."),
            @ApiResponse(
                    responseCode = "500",
                    description = "Unexpected error occurred.",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponseDto.class)
                    )
            )
    })
    @DeleteMapping("/delete")
    public ResponseEntity<ResponseDto> deleteAccount(@RequestParam @Pattern(regexp = "\\d{10}",
            message = "Mobile number must be exactly 10 digits") String mobileNumber) {
        boolean deleted = accountsService.deleteAccount(mobileNumber);
        if (deleted) {
            return ResponseEntity
                    .status(HttpStatus.OK) // 200 resource deleted successfully
                    .body(new ResponseDto(AccountsConstants.STATUS_200, AccountsConstants.MESSAGE_200));
        } else {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR) // 404 resource not found
                    .body(new ResponseDto(AccountsConstants.STATUS_500, AccountsConstants.MESSAGE_500));
        }
    }

    @GetMapping("/build-info")
    public ResponseEntity<String> getBuildVersion() {
        return ResponseEntity.status(HttpStatus.OK).body(buildVersion);
    }

    @GetMapping("/java-version")
    public ResponseEntity<String> getJavaVersion() {
        return ResponseEntity.status(HttpStatus.OK).body(environment.getProperty("MAVEN_HOME"));
    }

    @GetMapping("/contact-info")
    public ResponseEntity<AccountsContactInfoDto> getContactInfo() {
        return ResponseEntity.status(HttpStatus.OK).body(accountsContactInfoDto);
    }

}

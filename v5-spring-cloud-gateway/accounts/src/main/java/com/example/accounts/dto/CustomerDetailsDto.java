package com.example.accounts.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(
        name = "CustomerDetails",
        description = "Schema to hold customer, account, cards and loans information"
)
public class CustomerDetailsDto {

    @Schema(description = "Customer's full name", example = "John Doe")
    @NotEmpty(message = "Name is mandatory")
    @Size(min = 3, max = 50, message = "Name should be between 3 and 50 characters")
    private String name;

    @Schema(description = "Customer's valid email address", example = "john.doe@example.com")
    @NotEmpty(message = "Email is mandatory")
    @Email(message = "Please provide a valid email address")
    private String email;

    @Schema(description = "Customer's 10-digit mobile number", example = "9876543210")
    @NotEmpty(message = "Mobile number is mandatory")
    @Pattern(regexp = "\\d{10}", message = "Mobile number must be exactly 10 digits")
    private String mobileNumber;

    @Schema(
            description = "account detail of customer"
    )
    private AccountsDto accountsDto;

    @Schema(
            description = "account detail of customer's card"
    )
    private CardsDto cardsDto;

    @Schema(
            description = "account detail of customer's loan"
    )
    private LoansDto loansDto;
}

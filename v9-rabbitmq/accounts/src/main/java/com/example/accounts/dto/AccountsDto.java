package com.example.accounts.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import jakarta.validation.constraints.Pattern;

@Data
@Schema(
        name = "Accounts",
        description = "Schema to hold accounts information"
)
public class AccountsDto {
    
    @NotEmpty(message = "Account number cannot be null or empty.")
    @Pattern(regexp = "\\d{10}", message = "Account number must be exactly 10 digits.")
    @Schema(description = "The unique account number consisting of exactly 10 digits", example = "1234567890")
    private String accountNumber;

    
    @NotEmpty(message = "Account type cannot be null or empty.")
    @Schema(description = "The type of account, such as 'Savings' or 'Checking'", example = "Savings")
    private String accountType;
    
    
    @NotEmpty(message = "Branch address cannot be null or empty.")
    @Schema(description = "The address of the branch associated with the account", example = "123 Main Street, Springfield")
    private String branchAddress;
}

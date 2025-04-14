package com.example.accounts.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@Schema(name = "Error Response", description = "Represents the structure of an error response.")
public class ErrorResponseDto {

    @Schema(description = "The path of the API where the error occurred.", example = "/v1/accounts")
    private String apiPath;

    @Schema(description = "The HTTP status code of the error.", example = "404")
    private HttpStatus errorCode;

    @Schema(description = "A detailed error message describing the issue.", example = "Account not found.")
    private String errorMessage;

    @Schema(description = "The timestamp when the error occurred.", example = "2023-10-01T10:15:30")
    private LocalDateTime errorTimestamp;
}

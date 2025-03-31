package com.example.accounts.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;

@Data
@AllArgsConstructor
@Schema(
        name = "Response",
        description = "Response Data Transfer Object representing API status information.")
public class ResponseDto {

    @Schema(description = "The HTTP status code returned by the API.", example = "200")
    private String statusCode;

    @Schema(description = "The HTTP status message providing additional information.", example = "Success")
    private String statusMessage;
}

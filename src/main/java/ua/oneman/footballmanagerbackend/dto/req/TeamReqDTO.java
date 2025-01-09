package ua.oneman.footballmanagerbackend.dto.req;

import jakarta.validation.constraints.*;

import lombok.Data;

@Data
public class TeamReqDTO {

    @NotBlank(message = "Team name must not be blank")
    @Size(min = 3, max = 50, message = "Team name must be between 3 and 50 characters")
    private String name;

    @NotNull(message = "Budget must not be null")
    @Positive(message = "Budget must be a positive number")
    private Double budget;

    @NotNull(message = "Commission percentage must not be null")
    @DecimalMin(value = "0.0", inclusive = true, message = "Commission percentage must be at least 0%")
    @DecimalMax(value = "100.0", inclusive = true, message = "Commission percentage must be at most 100%")
    private Double commissionPercentage;
}
package ua.oneman.footballmanagerbackend.dto.req;

import jakarta.validation.constraints.*;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class TeamReqDTO {

    @NotBlank(message = "Team name must not be blank")
    @Size(min = 3, max = 50, message = "Team name must be between 3 and 50 characters")
    private String name;

    @NotNull(message = "Commission percentage must not be null")
    @DecimalMin(value = "0.0", inclusive = true, message = "Commission percentage must be at least 0%")
    @DecimalMax(value = "10.0", inclusive = true, message = "Commission percentage cannot exceed 10%")
    private Double commissionPercentage;

    @NotNull(message = "Balance must not be null")
    @PositiveOrZero(message = "Balance must be a non-negative value")
    private BigDecimal balance;
}
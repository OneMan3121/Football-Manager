package ua.oneman.footballmanagerbackend.dto.req;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class UserBalanceUpdateReqDTO {

    @NotNull
    @DecimalMin(value = "0.01", message = "The top-up amount must be greater than 0")
    private BigDecimal amount;
}
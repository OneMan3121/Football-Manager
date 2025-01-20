package ua.oneman.footballmanagerbackend.dto.req;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class TransferRequestReqDTO {

    @NotNull(message = "Player ID cannot be null.")
    private Long playerId;

    @NotNull(message = "From team ID cannot be null.")
    private Long fromTeamId;

    @NotNull(message = "To team ID cannot be null.")
    private Long toTeamId;

    @NotNull(message = "Transfer fee cannot be null.")
    @DecimalMin(value = "0.01", message = "Transfer fee must be greater than 0.")
    private BigDecimal transferFee;
}
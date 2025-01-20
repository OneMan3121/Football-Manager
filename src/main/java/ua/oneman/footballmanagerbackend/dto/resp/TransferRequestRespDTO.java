package ua.oneman.footballmanagerbackend.dto.resp;

import lombok.Data;
import ua.oneman.footballmanagerbackend.model.TransferStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class TransferRequestRespDTO {

    private Long id;

    private Long playerId;

    private Long fromTeamId;

    private Long toTeamId;

    private BigDecimal transferFee;

    private String buyerUsername;

    private String sellerUsername;

    private TransferStatus status;

    private LocalDateTime requestedAt;

    private LocalDateTime decisionAt;
}
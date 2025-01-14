package ua.oneman.footballmanagerbackend.model;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
public class TransferRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Player player;

    @ManyToOne
    private Team fromTeam;

    @ManyToOne
    private Team toTeam;

    private BigDecimal transferFee;

    private TransferStatus status;

    @ManyToOne
    private User buyer;

    @ManyToOne
    private User seller;

    private LocalDateTime requestedAt;

    private LocalDateTime decisionAt;
}
    enum TransferStatus {
        PENDING,
        APPROVED,
        REJECTED
    }

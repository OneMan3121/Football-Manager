package ua.oneman.footballmanagerbackend.dto.resp.admin;

import jakarta.persistence.*;
import ua.oneman.footballmanagerbackend.model.Player;
import ua.oneman.footballmanagerbackend.model.User;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class AdminTeamRespDTO {

    private Long id;

    private String name;

    String logoUrl;

    private BigDecimal balance;

    private Double commissionPercentage;

    private List<Player> players;

    private User owner;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private Boolean isDeleted;

    private LocalDateTime deletedAt;
}

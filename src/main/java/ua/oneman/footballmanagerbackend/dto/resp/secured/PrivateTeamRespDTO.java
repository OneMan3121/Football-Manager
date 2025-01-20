package ua.oneman.footballmanagerbackend.dto.resp.secured;

import ua.oneman.footballmanagerbackend.model.Player;
import ua.oneman.footballmanagerbackend.model.User;

import java.math.BigDecimal;
import java.util.List;

public class PrivateTeamRespDTO {
    private String name;
    String logoUrl;
    private BigDecimal balance;
    private Double commissionPercentage;
    private List<Player> players;
    private User owner;

}

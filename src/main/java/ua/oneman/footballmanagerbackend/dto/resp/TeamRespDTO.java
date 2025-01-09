package ua.oneman.footballmanagerbackend.dto.resp;

import lombok.Data;

@Data
public class TeamRespDTO {
    private Long id;
    private String name;
    private Double budget;
    private Double commissionPercentage;
    private String ownerUsername;
}
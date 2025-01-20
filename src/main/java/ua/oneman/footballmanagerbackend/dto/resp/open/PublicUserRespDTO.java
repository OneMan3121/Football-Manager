package ua.oneman.footballmanagerbackend.dto.resp.open;

import lombok.Data;

import java.util.List;

@Data
public class PublicUserRespDTO {
    private Long id;
    private String username;
    private String firstName;
    private String lastName;
    private String avatarUrl;

    private List<PublicTeamRespDTO> teams;
}
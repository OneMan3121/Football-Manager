package ua.oneman.footballmanagerbackend.dto.resp;

import lombok.Data;

@Data
public class PublicUserRespDTO {
    private Long id;
    private String username;
    private String firstName;
    private String lastName;
    private String avatarUrl;
}
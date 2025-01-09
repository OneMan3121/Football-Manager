package ua.oneman.footballmanagerbackend.dto.resp;

import lombok.Data;

@Data
public class UserRespDTO {
    private Long id;
    private String username;
    private String firstName;
    private String lastName;
    private String email;
}
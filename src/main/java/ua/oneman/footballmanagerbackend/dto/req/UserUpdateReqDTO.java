package ua.oneman.footballmanagerbackend.dto.req;

import lombok.Data;

@Data
public class UserUpdateReqDTO {
    private String firstName;
    private String lastName;
    private String email;
}
package ua.oneman.footballmanagerbackend.dto.req;

import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserUpdateReqDTO {

    @Size(max = 50, message = "First name must not exceed 50 characters")
    private String firstName;

    @Size(max = 50, message = "Last name must not exceed 50 characters")
    private String lastName;

    @Size(max = 100, message = "Email must not exceed 100 characters")
    private String email;
}
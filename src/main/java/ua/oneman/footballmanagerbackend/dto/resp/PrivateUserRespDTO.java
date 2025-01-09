package ua.oneman.footballmanagerbackend.dto.resp;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class PrivateUserRespDTO {
    private Long id;
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private BigDecimal balance;
}
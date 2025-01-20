package ua.oneman.footballmanagerbackend.dto.resp.secured;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class PrivateUserRespDTO {
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private BigDecimal balance;
}

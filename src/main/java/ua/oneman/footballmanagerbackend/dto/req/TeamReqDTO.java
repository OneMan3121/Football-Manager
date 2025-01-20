package ua.oneman.footballmanagerbackend.dto.req;

import jakarta.validation.constraints.*;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class TeamReqDTO {

    @NotBlank(message = "Team name must not be blank")
    @Size(min = 3, max = 50, message = "Team name must be between 3 and 50 characters")
    private String name;

    private String logoUrl;

}
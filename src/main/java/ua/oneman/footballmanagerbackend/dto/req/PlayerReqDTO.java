package ua.oneman.footballmanagerbackend.dto.req;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PlayerReqDTO {

    @NotBlank(message = "First name must not be blank")
    private String firstName;

    @NotBlank(message = "Last name must not be blank")
    private String lastName;

    @NotNull(message = "Age must not be null")
    @Min(value = 1, message = "Age must be greater than 0")
    @jakarta.validation.constraints.Max(value = 100, message = "Age must be less than or equal to 100")
    private Integer age;

    @NotNull(message = "Experience months must not be null")
    @Min(value = 0, message = "Experience months must be greater than or equal to 0")
    private Integer experienceMonths;
}
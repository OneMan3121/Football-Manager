package ua.oneman.footballmanagerbackend.dto.resp;

import lombok.Data;

@Data
public class PlayerRespDTO {

    private Long id;
    private String firstName;
    private String lastName;
    private Integer age;
    private Integer experienceMonths;
    private String teamName;
}
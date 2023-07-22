package ru.romanov.pastbin.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class PersonDTO {
    @NotNull(message = "This field should be contain your full name")
    private String fullName;
    @NotNull(message = "This field should be contain username for your account")
    @Size(min = 3, max = 16, message = "This field must be contain at least 4 and no more than 16 characters")
    private String username;
    @NotNull(message = "This field should be contain password for your account")
    @Size(min = 4, max = 16, message = "This field must be contain at least 4 and no more than 16 characters")
    private String password;
}

package ru.practicum.dto.user;

import lombok.Value;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Value
public class UserDto {
    Long id;
    @NotBlank
    @Email
    @Size(min = 6, max = 256)
    String email;
    @NotBlank
    @Size(min = 2, max = 256)
    String name;


}

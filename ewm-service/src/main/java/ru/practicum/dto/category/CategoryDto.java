package ru.practicum.dto.category;

import lombok.Value;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Null;
import javax.validation.constraints.Size;

@Value
public class CategoryDto {
    @Null
    Long id;

    @NotBlank
    @Size(min = 1, max = 50)
    String name;
}

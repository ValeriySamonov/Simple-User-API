package com.example.user_api.dto.contact;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

@Data
@RequiredArgsConstructor
@Accessors(chain = true)
public class CreateEmailDTO {

    @NotEmpty(message = "Адрес email не может быть пустым")
    @Email(message = "Адрес email должен быть валидным")
    private String email;
}

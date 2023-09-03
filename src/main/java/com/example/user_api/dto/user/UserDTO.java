package com.example.user_api.dto.user;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

@Data
@RequiredArgsConstructor
@Accessors(chain = true)
public class UserDTO {
    private Long id;
    private String username;
}

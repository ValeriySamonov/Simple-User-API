package com.example.user_api.dto.user;

import com.example.user_api.dto.contact.ContactDTO;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@RequiredArgsConstructor
@Accessors(chain = true)
public class UserWithContactDTO {
    private Long id;
    private String username;
    private List<ContactDTO> contacts;
}

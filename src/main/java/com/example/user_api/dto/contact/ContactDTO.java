package com.example.user_api.dto.contact;

import com.example.user_api.enums.ContactType;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

@Data
@RequiredArgsConstructor
@Accessors(chain = true)
public class ContactDTO {
    private Long id;
    private ContactType contactType;
    private String value;
}

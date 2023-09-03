package com.example.user_api.servicies;

import com.example.user_api.dto.contact.ContactDTO;
import com.example.user_api.dto.user.CreateUserDTO;
import com.example.user_api.dto.user.UserDTO;
import com.example.user_api.dto.user.UserWithContactDTO;
import com.example.user_api.enums.ContactType;
import org.springframework.data.domain.Page;

import java.util.List;

public interface UserService {
    Long createUser(CreateUserDTO createUserDTO);
    <T>Long createUserContact(Long userId, T createContactDTO, ContactType contactType);
    Page<UserDTO> getAllUsers(int page, int size);
    UserWithContactDTO getUserById(Long userId);
    List<ContactDTO> getContactByUserId(Long userId);
    List<ContactDTO> getUserContactByType(Long userId, ContactType contactType);
    void deleteUserById(Long userId);

}

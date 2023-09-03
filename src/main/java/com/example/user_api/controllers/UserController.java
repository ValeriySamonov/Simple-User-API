package com.example.user_api.controllers;

import com.example.user_api.dto.contact.ContactDTO;
import com.example.user_api.dto.contact.CreateEmailDTO;
import com.example.user_api.dto.contact.CreatePhoneDTO;
import com.example.user_api.dto.user.CreateUserDTO;
import com.example.user_api.dto.user.UserDTO;
import com.example.user_api.dto.user.UserWithContactDTO;
import com.example.user_api.enums.ContactType;
import com.example.user_api.servicies.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/users")
    public Long createUser(@Valid @RequestBody CreateUserDTO createUserDTO) {
        return userService.createUser(createUserDTO);
    }

    @PostMapping("/users/{userId}/contacts/phone")
    public Long createUserPhone(@PathVariable Long userId,
                                @Valid @RequestBody CreatePhoneDTO createUserPhone) {
        ContactType contactType = ContactType.PHONE;
        return userService.createUserContact(userId, createUserPhone, contactType);
    }

    @PostMapping("/users/{userId}/contacts/email")
    public Long createUserEmail(@PathVariable Long userId,
                                @Valid @RequestBody CreateEmailDTO createUserEmail) {
        ContactType contactType = ContactType.EMAIL;
        return userService.createUserContact(userId, createUserEmail, contactType);
    }

    @GetMapping("/users")
    public Page<UserDTO> getAllUsers(@RequestParam(defaultValue = "0") int page,
                                     @RequestParam(defaultValue = "10") int size) {
        return userService.getAllUsers(page, size);

    }

    @GetMapping("/users/{userId}")
    public UserWithContactDTO getUserById(@PathVariable Long userId) {
        return userService.getUserById(userId);
    }

    @GetMapping("/users/{userId}/contacts")
    public List<ContactDTO> getContactByUserId(@PathVariable Long userId) {
        return userService.getContactByUserId(userId);
    }

    @GetMapping("/users/{userId}/contacts/{contactType}")
    public List<ContactDTO> getUserContactByType(@PathVariable Long userId,
                                                 @PathVariable ContactType contactType) {
        return userService.getUserContactByType(userId, contactType);
    }

    @DeleteMapping("/users/{userId}")
    public void deleteUserById(@PathVariable Long userId) {
        userService.deleteUserById(userId);
    }

}

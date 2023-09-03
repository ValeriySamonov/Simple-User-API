package com.example.user_api.controllers;

import com.example.user_api.TestUserApiApplication;
import com.example.user_api.dto.contact.CreateEmailDTO;
import com.example.user_api.dto.contact.CreatePhoneDTO;
import com.example.user_api.dto.user.CreateUserDTO;
import com.example.user_api.enums.ContactType;
import com.example.user_api.models.Contact;
import com.example.user_api.models.User;
import com.example.user_api.repositories.ContactRepository;
import com.example.user_api.repositories.UserRepository;
import com.example.user_api.servicies.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = TestUserApiApplication.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureMockMvc
@Sql(scripts = "/sql/data-test.sql")
public class UserControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ContactRepository contactRepository;

    @Autowired
    UserService userService;

    @DisplayName("Тест для метода создания пользователя")
    @Test
    @SneakyThrows
    public void createUserTest() {

        CreateUserDTO createUserDTO = new CreateUserDTO()
                .setUsername("User");

        String jsonCreateUser = new ObjectMapper().writeValueAsString(createUserDTO);

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonCreateUser))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(4L));

        Optional<User> savedUser = userRepository.findByUsername("User");
        assertTrue(savedUser.isPresent());

    }

    @DisplayName("Тест для метода создания контакта с телефонным номером")
    @Test
    @SneakyThrows
    public void createUserPhoneTest() {

        long userId = 1L;

        CreatePhoneDTO createPhoneDTO = new CreatePhoneDTO()
                .setPhone("+79001234567");

        String jsonCreatePhone = new ObjectMapper().writeValueAsString(createPhoneDTO);

        mockMvc.perform(post("/api/users/{userId}/contacts/phone", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonCreatePhone))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(3L));

        Optional<Contact> savedContact = contactRepository.findByValueAndId(createPhoneDTO.getPhone(), 3L);
        assertTrue(savedContact.isPresent());
    }

    @DisplayName("Тест для метода создания контакта с адресом email")
    @Test
    @SneakyThrows
    public void createUserEmailTest() {

        long userId = 1L;

        CreateEmailDTO createEmailDTO = new CreateEmailDTO()
                .setEmail("email@mail.com");

        String jsonCreatePhone = new ObjectMapper().writeValueAsString(createEmailDTO);

        mockMvc.perform(post("/api/users/{userId}/contacts/email", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonCreatePhone))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(3L));

        Optional<Contact> savedContact = contactRepository.findByValueAndId(createEmailDTO.getEmail(), 3L);
        assertTrue(savedContact.isPresent());
    }

    @DisplayName("Тест для метода получения всех пользователей")
    @Test
    @SneakyThrows
    public void getAllUsersTest() {

        int size = 10;
        int page = 0;

        mockMvc.perform(get("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("size", String.valueOf(size))
                        .param("page", String.valueOf(page)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].username").value("user1"))
                .andExpect(jsonPath("$.content[1].username").value("user2"))
                .andExpect(jsonPath("$.content[2].username").value("user3"));
    }

    @DisplayName("Тест для метода получения полной информации о пользователе")
    @Test
    @SneakyThrows
    public void getUserByIdTest() {

        long userId = 1L;

        mockMvc.perform(get("/api/users/" + userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userId))
                .andExpect(jsonPath("$.username").value("user1"))
                .andExpect(jsonPath("$.contacts[1].value").value("user1@example.com"))
                .andExpect(jsonPath("$.contacts[0].value").value("+79001234567"));
    }

    @DisplayName("Тест для метода получения контактов пользователя")
    @Test
    @SneakyThrows
    public void getContactByUserIdTest() {

        long userId = 1L;
        long contactId1 = 1L;
        long contactId2 = 2L;

        mockMvc.perform(get("/api/users/" + userId + "/contacts")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(contactId1))
                .andExpect(jsonPath("$[0].contactType").value("PHONE"))
                .andExpect(jsonPath("$[0].value").value("+79001234567"))
                .andExpect(jsonPath("$[1].id").value(contactId2))
                .andExpect(jsonPath("$[1].contactType").value("EMAIL"))
                .andExpect(jsonPath("$[1].value").value("user1@example.com"));
    }

    @DisplayName("Тест для метода получения контактов пользователя по типу Phone")
    @Test
    @SneakyThrows
    public void getUserContactByPhoneTest() {

        long userId = 1L;
        long contactId = 1L;

        mockMvc.perform(get("/api/users/" + userId + "/contacts/" + ContactType.PHONE)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(contactId))
                .andExpect(jsonPath("$[0].contactType").value("PHONE"))
                .andExpect(jsonPath("$[0].value").value("+79001234567"));
    }

    @DisplayName("Тест для метода получения контактов пользователя по типу Email")
    @Test
    @SneakyThrows
    public void getUserContactByEmailTest() {

        long userId = 1L;
        long contactId = 2L;

        mockMvc.perform(get("/api/users/" + userId + "/contacts/" + ContactType.EMAIL)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(contactId))
                .andExpect(jsonPath("$[0].contactType").value("EMAIL"))
                .andExpect(jsonPath("$[0].value").value("user1@example.com"));
    }

    @DisplayName("Тест для метода удаления пользователя")
    @Test
    @SneakyThrows
    public void deleteUserByIdTest() {

        long userId = 1L;

        mockMvc.perform(delete("/api/users/" + userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}

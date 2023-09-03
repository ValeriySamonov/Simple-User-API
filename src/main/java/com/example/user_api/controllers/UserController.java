package com.example.user_api.controllers;

import com.example.user_api.dto.contact.ContactDTO;
import com.example.user_api.dto.contact.CreateEmailDTO;
import com.example.user_api.dto.contact.CreatePhoneDTO;
import com.example.user_api.dto.user.CreateUserDTO;
import com.example.user_api.dto.user.UserDTO;
import com.example.user_api.dto.user.UserWithContactDTO;
import com.example.user_api.enums.ContactType;
import com.example.user_api.servicies.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "User API", description = "Простая реализацию пользовательского API, " +
        "позволяющая осуществлять основные операции по хранению информации о пользователях и их контактах.")
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @Operation(summary = "Создать пользователя", description = "Создание нового пользователя с уникальным именем. " +
            "В случае успешного выполнения возвращается ID созданного пользователя.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Пользователь успешно создан"),
            @ApiResponse(responseCode = "400", description = "Пользователь с таким именем уже существует")
    })
    @PostMapping("/users")
    public Long createUser(@Valid @RequestBody CreateUserDTO createUserDTO) {
        return userService.createUser(createUserDTO);
    }

    @Operation(summary = "Создать телефонный контакт", description = "Создание нового телефонного контакта пользователя. " +
            "Номер телефона должен быть введён в правильном формате: +7, затем 10 цифр номера. " +
            "В случае успешного выполнения возвращается ID созданного контакта.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Контакт успешно создан"),
            @ApiResponse(responseCode = "404", description = "Пользователь не существует")
    })
    @PostMapping("/users/{userId}/contacts/phone")
    public Long createUserPhone(@Parameter(description = "ID пользователя") @PathVariable Long userId,
                                @Valid @RequestBody CreatePhoneDTO createUserPhone) {
        ContactType contactType = ContactType.PHONE;
        return userService.createUserContact(userId, createUserPhone, contactType);
    }

    @Operation(summary = "Создать email контакт", description = "Создание нового email контакта пользователя. " +
            "Адрес электронной почты должен быть введён в правильном формате. " +
            "В случае успешного выполнения возвращается ID созданного контакта.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Контакт успешно создан"),
            @ApiResponse(responseCode = "404", description = "Пользователь не существует")
    })
    @PostMapping("/users/{userId}/contacts/email")
    public Long createUserEmail(@Parameter(description = "ID пользователя") @PathVariable Long userId,
                                @Valid @RequestBody CreateEmailDTO createUserEmail) {
        ContactType contactType = ContactType.EMAIL;
        return userService.createUserContact(userId, createUserEmail, contactType);
    }

    @Operation(summary = "Список пользователей", description = "Постраничный вывод списка имён всех пользователей. " +
            "В качестве дополнительных параметров задаётся количество элементов на странице и номер страницы.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Запрос успешно выполнен"),
            @ApiResponse(responseCode = "404", description = "Пользователь не существует")
    })
    @GetMapping("/users")
    public Page<UserDTO> getAllUsers(@Parameter(description = "Номер страницы") @RequestParam(defaultValue = "0", required = false) int page,
                                     @Parameter(description = "Размер страницы") @RequestParam(defaultValue = "10", required = false) int size) {
        return userService.getAllUsers(page, size);

    }

    @Operation(summary = "Информация о пользователе", description = "Получение полной информации о пользователе. " +
            "Отображается имя пользователя и все его контакты")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Запрос успешно выполнен"),
            @ApiResponse(responseCode = "404", description = "Пользователь не существует")
    })
    @GetMapping("/users/{userId}")
    public UserWithContactDTO getUserById(@Parameter(description = "ID пользователя") @PathVariable Long userId) {
        return userService.getUserById(userId);
    }

    @Operation(summary = "Контакты пользователя", description = "Получение информации о всех контактах пользователя.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Запрос успешно выполнен"),
            @ApiResponse(responseCode = "404", description = "Пользователь не существует")
    })
    @GetMapping("/users/{userId}/contacts")
    public List<ContactDTO> getContactByUserId(@Parameter(description = "ID пользователя") @PathVariable Long userId) {
        return userService.getContactByUserId(userId);
    }

    @Operation(summary = "Контакты пользователя по типу", description = "Получение информации о всех контактах пользователя по заданному типу - email или phone.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Запрос успешно выполнен"),
            @ApiResponse(responseCode = "404", description = "Пользователь не существует")
    })
    @GetMapping("/users/{userId}/contacts/type")
    public List<ContactDTO> getUserContactByType(@Parameter(description = "ID пользователя") @PathVariable Long userId,
                                                 @Parameter(description = "Тип контакта") @RequestParam ContactType contactType) {
        return userService.getUserContactByType(userId, contactType);
    }

    @Operation(summary = "Удаление пользователя", description = "Удаление пользователя. Одновременно удаляется вся информация об его контактах.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Запрос успешно выполнен"),
            @ApiResponse(responseCode = "404", description = "Пользователь не существует")
    })
    @DeleteMapping("/users/{userId}")
    public void deleteUserById(@Parameter(description = "ID пользователя") @PathVariable Long userId) {
        userService.deleteUserById(userId);
    }

}

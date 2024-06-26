package com.example.user_api.servicies;

import com.example.user_api.dto.contact.ContactDTO;
import com.example.user_api.dto.contact.CreateEmailDTO;
import com.example.user_api.dto.contact.CreatePhoneDTO;
import com.example.user_api.dto.user.CreateUserDTO;
import com.example.user_api.dto.user.UserDTO;
import com.example.user_api.dto.user.UserWithContactDTO;
import com.example.user_api.enums.ContactType;
import com.example.user_api.exceptions.UserNotFoundException;
import com.example.user_api.models.Contact;
import com.example.user_api.models.User;
import com.example.user_api.repositories.ContactRepository;
import com.example.user_api.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ContactRepository contactRepository;
    private final ModelMapper modelMapper;

    @Override
    @Transactional
    public Long createUser(CreateUserDTO createUserDTO) {

        User user = new User()
                .setUsername(createUserDTO.getUsername());
        userRepository.save(user);


        return user.getId();
    }

    @Override
    @Transactional
    public <T> Long createUserContact(Long userId, T createContactDTO, ContactType contactType) {

        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);

        String value = null;
        if (createContactDTO instanceof CreateEmailDTO) {
            value = ((CreateEmailDTO) createContactDTO).getEmail();
        } else if (createContactDTO instanceof CreatePhoneDTO) {
            value = ((CreatePhoneDTO) createContactDTO).getPhone();
        }

        Contact contact = new Contact()
                .setContactType(contactType)
                .setValue(value)
                .setUser(user);
        contactRepository.save(contact);

        return contact.getId();
    }

    @Override
    @Transactional
    public Page<UserDTO> getAllUsers(int page, int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<User> usersPage = userRepository.findAll(pageable);

        return usersPage.map(user -> modelMapper.map(user, UserDTO.class));
    }

    @Override
    @Transactional
    public UserWithContactDTO getUserById(Long userId) {

        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);

        UserWithContactDTO userWithContactDTO = modelMapper.map(user, UserWithContactDTO.class);

        if (!user.getContacts().isEmpty()) {
            userWithContactDTO.setContacts(user.getContacts().stream()
                    .map(contact -> modelMapper.map(contact, ContactDTO.class))
                    .toList());
        }

        return userWithContactDTO;
    }

    @Override
    @Transactional
    public List<ContactDTO> getContactByUserId(Long userId) {

        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);

        return user.getContacts().stream()
                .map(contact -> modelMapper.map(contact, ContactDTO.class))
                .toList();
    }

    @Override
    @Transactional
    public List<ContactDTO> getUserContactByType(Long userId, ContactType contactType) {

        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);

        return user.getContacts().stream()
                .filter(contact -> contact.getContactType().equals(contactType))
                .map(contact -> modelMapper.map(contact, ContactDTO.class))
                .toList();
    }

    @Override
    @Transactional
    public void deleteUserById(Long userId) {

        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);

        userRepository.delete(user);
    }

}

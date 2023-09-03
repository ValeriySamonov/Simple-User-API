package com.example.user_api.repositories;

import com.example.user_api.models.Contact;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ContactRepository extends JpaRepository<Contact, Long> {
    Optional<Contact> findByValueAndId(String value, Long id);
}

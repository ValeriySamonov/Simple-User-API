package com.example.user_api.models;

import com.example.user_api.enums.ContactType;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Entity
@Table(name = "contacts")
@Data
@NoArgsConstructor
@Accessors(chain = true)
public class Contact {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private ContactType contactType;

    private String value;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

}

package com.commerce.wallet.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Email(message = "Email is not valid")
    @Column(unique = true, nullable = false)
    private String email;

    @Size(min = 3, max = 30, message = "Username must be between 3 and 30 characters")
    @Column(unique = true, nullable = false)
    private String userName;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Size(min = 8, message = "Password must be at least 8 characters long")
    @Column(nullable = false)
    @JsonIgnore
    private String password;

    @JsonIgnore
    private boolean active = true;

    @JsonIgnore
    private LocalDateTime passwordUpdatedAt = LocalDateTime.now();

    @Column(updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Wallet wallet;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.passwordUpdatedAt = LocalDateTime.now();
    }

    public void setPassword(String password) {
        this.password = password;
        this.passwordUpdatedAt = LocalDateTime.now();
    }

}

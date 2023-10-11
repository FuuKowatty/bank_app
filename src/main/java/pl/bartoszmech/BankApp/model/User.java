package pl.bartoszmech.BankApp.model;

import jakarta.persistence.*;
import lombok.*;
import pl.bartoszmech.BankApp.exception.UserNotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Table(name = "users")
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "first_name")
    private String firstName;

    public User(Long id, String firstName) {
        this.id = id;
        this.firstName = firstName;
    }

    public User() {
        super();
    }
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
package pl.bartoszmech.BankApp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.bartoszmech.BankApp.enums.ExceptionMessages;
import pl.bartoszmech.BankApp.exception.UserNotFoundException;
import pl.bartoszmech.BankApp.model.Account;
import pl.bartoszmech.BankApp.model.User;
import pl.bartoszmech.BankApp.repository.AccountRepository;
import pl.bartoszmech.BankApp.repository.UserRepository;

import java.util.Collections;
import java.util.List;

@Service
public class UserService {
    @Autowired private UserRepository userRepository;
    @Autowired private AccountRepository accountRepository;

    public User findByUserId(Long userId) {
        return userRepository
                .findById(userId)
                .orElseThrow(() -> new UserNotFoundException(ExceptionMessages.USER_NOT_FOUND.getMessage()));
    }
    public User saveUser(User user) {
        return userRepository.save(user);
    }
}

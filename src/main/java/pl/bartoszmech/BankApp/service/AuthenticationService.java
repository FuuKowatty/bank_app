package pl.bartoszmech.BankApp.service;

import org.springframework.stereotype.Service;
import pl.bartoszmech.BankApp.enums.ExceptionMessages;
import pl.bartoszmech.BankApp.exception.UserNotFoundException;
import pl.bartoszmech.BankApp.model.User;
import pl.bartoszmech.BankApp.repository.UserRepository;

import java.util.Objects;

@Service
public class AuthenticationService {

    private final UserRepository userRepository;
    private final InputService inputService;
    private final AccountService accountService;

    public AuthenticationService(UserRepository userRepository, InputService inputService, AccountService accountService) {
        this.userRepository = userRepository;
        this.inputService = inputService;
        this.accountService = accountService;
    }


    public Long login() {
        User attemptData = inputService.askForLogin();

        User user = userRepository.findByUsername(attemptData.getUsername())
                .orElseThrow(() -> new UserNotFoundException(ExceptionMessages.USER_NOT_FOUND.getMessage()));

        if(!Objects.equals(attemptData.getPassword(), user.getPassword())) {
            throw new UserNotFoundException(ExceptionMessages.USER_NOT_FOUND.getMessage());
        }

        return user.getId();
    }

    public Long register() {
        User attemptData = inputService.askForRegister();
        //some validation in the future ...
        ;
        User user = userRepository.save(attemptData);
        accountService.createAccount(user, CurrencyService.actualCurrency);

        return user.getId();
    }
}

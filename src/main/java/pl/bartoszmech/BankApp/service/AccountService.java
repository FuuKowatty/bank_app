package pl.bartoszmech.BankApp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.bartoszmech.BankApp.enums.ExceptionMessages;
import pl.bartoszmech.BankApp.exception.AccountNotFoundException;
import pl.bartoszmech.BankApp.exception.UserNotFoundException;
import pl.bartoszmech.BankApp.model.Account;
import pl.bartoszmech.BankApp.model.User;
import pl.bartoszmech.BankApp.repository.AccountRepository;
import pl.bartoszmech.BankApp.repository.UserRepository;

import java.util.List;
import java.util.Objects;

@Service
public class AccountService {
    @Autowired private AccountRepository accountRepository;
    @Autowired private UserRepository userRepository;

    public void createAccount(User user, String currency) {
        Account account = new Account();
        account.setCurrency(currency);
        account.setUser(user);

        saveAccount(account);
    }

    public List<Account> getUserAccounts(Long userId) {
        return accountRepository.findByUserId(userId);
    }

    public Account getUserAccountByCurrency(Long userId, String currency) {
        List<Account> userAccounts = getUserAccounts(userId);
        return userAccounts.stream()
                .filter(a -> Objects.equals(a.getCurrency(), currency))
                .findFirst()
                .orElseThrow(() -> new AccountNotFoundException(ExceptionMessages.USER_NOT_FOUND.getMessage()));
    }

    public void saveAccount(Account account) {
        accountRepository.save(account);
    }

    public void updateBalance(Account account, Double amount) {
        account.setBalance(account.getBalance() + (amount));
        saveAccount(account);
    }

    public String getCurrencySymbol(String currency) {
        int hyphenIndex = currency.indexOf(" - ");
        if (hyphenIndex == -1) {
            throw new IllegalArgumentException("Invalid string.");
        }
        return currency.substring(0, hyphenIndex);
    }

}

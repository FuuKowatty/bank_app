package pl.bartoszmech.BankApp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.bartoszmech.BankApp.enums.ExceptionMessages;
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
                .orElseThrow(() -> new UserNotFoundException(ExceptionMessages.USER_NOT_FOUND.getMessage()));
    }

    public void saveAccount(Account account) {
        accountRepository.save(account);
    }
    public void withdrawMoney(Long userId, String currency, Double amount) {
        Account userAccount = getUserAccountByCurrency(userId, currency);
        updateBalance(userAccount, amount);
    }

    public void depositMoney(Long userId, String currency, Double amount, Double DEBIT_LIMIT) {
        Account userAccount = getUserAccountByCurrency(userId, currency);
        Double currentBalance = userAccount.getBalance();
        Double withdrawalAmount = -amount;

        boolean isLegalDebit = currentBalance + withdrawalAmount > DEBIT_LIMIT;

        if (!isLegalDebit) {
            System.out.println("Deposit failed. The transaction exceeds the debit limit.");
            return;
        }
        updateBalance(userAccount, withdrawalAmount);
    }

    public void updateBalance(Account account, Double amount) {
        account.setBalance(account.getBalance() + (amount));
        saveAccount(account);
    }
}

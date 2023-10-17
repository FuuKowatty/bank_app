package pl.bartoszmech.BankApp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.bartoszmech.BankApp.enums.ExceptionMessages;
import pl.bartoszmech.BankApp.exception.AccountNotFoundException;
import pl.bartoszmech.BankApp.model.Account;
import pl.bartoszmech.BankApp.model.User;
import pl.bartoszmech.BankApp.repository.AccountRepository;
import pl.bartoszmech.BankApp.repository.UserRepository;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Objects;

@Service
public class AccountService {
    @Autowired private AccountRepository accountRepository;
    @Autowired private UserRepository userRepository;

    @Autowired private CurrencyService currencyService;

    public void createAccount(User user, String currency) {
        Account account = new Account();
        account.setCurrency(currency);
        account.setUser(user);

        saveAccount(account);
    }

    public List<Account> getUserAccounts(Long userId) {
        return accountRepository.findByUserId(userId);
    }

    public Account findAccountByUserId(Long userId) {
        List<Account> userAccounts = getUserAccounts(userId);
        return userAccounts.stream()
                .filter(a -> Objects.equals(a.getCurrency(), CurrencyService.actualCurrency))
                .findFirst()
                .orElseThrow(() -> new AccountNotFoundException(ExceptionMessages.USER_NOT_FOUND.getMessage()));
    }

    public void saveAccount(Account account) {
        accountRepository.save(account);
    }

    public void updateBalance(Account senderAccount, Account recipientAccount, double amount) {
        addToBalance(senderAccount, -amount);
        addToBalance(recipientAccount, convertMoneyByRate(recipientAccount.getCurrency(), amount));
    }

    public void addToBalance(Account account, Double amount) {
        account.setBalance(account.getBalance() + amount);
        saveAccount(account);
    }

    public String getCurrencySymbol(String currency) {
        int hyphenIndex = currency.indexOf(" - ");
        if (hyphenIndex == -1) {
            throw new IllegalArgumentException("Invalid string.");
        }
        return currency.substring(0, hyphenIndex);
    }

    public boolean hasMultipleAccounts(Long userId) {
        return accountRepository.findByUserId(userId).size() > 1;
    }

    public void transferMoneyToForeignAccount(long userId, double amount) {
        Account foreignAccount = null;
        Account primaryAccount = null;
        for (Account account : getUserAccounts(userId)) {
            if (Objects.equals(account.getCurrency(), CurrencyService.actualCurrency)) {
                primaryAccount = account;
            } else {
                foreignAccount = account;
            }
        }

        if (primaryAccount == null || foreignAccount == null) {
            throw new AccountNotFoundException(ExceptionMessages.ACCOUNT_NOT_FOUND.getMessage());
        }

        updateBalance(
                primaryAccount,
                foreignAccount,
                amount
        );
    }

    public void transferMoneyToOtherUser(User senderUser, User recipentUser, double amount) {
        //It finds an account with the same currency as the account that provides the money.
        Account recipentDefaultAccount = findUserAccountByCurrency(recipentUser, CurrencyService.DEFAULT_CURRENCY);
        Account senderDefaultAccount = findUserAccountByCurrency(senderUser, CurrencyService.actualCurrency);

        updateBalance(
                senderDefaultAccount,
                recipentDefaultAccount,
                amount
        );
    }

    private Account findUserAccountByCurrency(User recipentUser, String currency) {
        return getUserAccounts(recipentUser.getId())
                .stream()
                .filter(a -> Objects.equals(a.getCurrency(), currency))
                .findFirst()
                .orElseThrow(() -> new AccountNotFoundException("User does not have an account"));
    }

    public Double convertMoneyByRate(String currency, Double amount) {
        double exchangeRate = currencyService
                .fetchRates()
                .getConversionRate(currency)
                .getValue();

        BigDecimal amountToConvert = new BigDecimal(amount);
        BigDecimal exchangeRateBigDecimal = new BigDecimal(exchangeRate);
        return amountToConvert
                .multiply(exchangeRateBigDecimal)
                .setScale(2, RoundingMode.HALF_UP)
                .doubleValue();
    }
}

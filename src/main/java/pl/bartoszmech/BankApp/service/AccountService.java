package pl.bartoszmech.BankApp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.bartoszmech.BankApp.enums.ExceptionMessages;
import pl.bartoszmech.BankApp.enums.TransactionType;
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
    @Autowired private TransactionService transactionService;

    public AccountService() {
    }

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

    public void updateBalance(Account senderAccount, Account recipientAccount, BigDecimal amount) {
        addToBalance(senderAccount, amount.multiply(new BigDecimal(-1)));
        addToBalance(recipientAccount, convertMoneyByRate(recipientAccount.getCurrency(), amount));
    }

    public void addToBalance(Account account, BigDecimal amount) {
        account.setBalance(account.getBalance().add(amount));
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

    public void transferMoneyToForeignAccount(long userId, BigDecimal amount) {
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
        transactionService.saveTransfer(
                amount,
                convertCurrency(primaryAccount.getCurrency(), foreignAccount.getCurrency()),
                foreignAccount.getId(),
                primaryAccount.getId(),
                TransactionType.TRANSFER_BETWEEN_ACCOUNTS
        );

    }

    private String convertCurrency(String senderCurrency, String recipentCurrency) {
        if(Objects.equals(senderCurrency, recipentCurrency)) {
            return senderCurrency;
        }
        return senderCurrency + "/" + recipentCurrency;
    }

    public void transferMoneyToOtherUser(User senderUser, User recipentUser, BigDecimal amount) {
        //It finds an account with the same currency as the account that provides the money.
        Account recipentDefaultAccount = findUserAccountByCurrency(recipentUser, CurrencyService.DEFAULT_CURRENCY);
        Account senderDefaultAccount = findUserAccountByCurrency(senderUser, CurrencyService.actualCurrency);

        updateBalance(
                senderDefaultAccount,
                recipentDefaultAccount,
                amount
        );
        transactionService.saveTransfer(
                amount,
                convertCurrency(senderDefaultAccount.getCurrency(), recipentDefaultAccount.getCurrency()),
                recipentDefaultAccount.getId(),
                senderDefaultAccount.getId(),
                TransactionType.TRANSFER_TO_ANOTHER_ACCOUNT
        );
    }

    private Account findUserAccountByCurrency(User recipentUser, String currency) {
        return getUserAccounts(recipentUser.getId())
                .stream()
                .filter(a -> Objects.equals(a.getCurrency(), currency))
                .findFirst()
                .orElseThrow(() -> new AccountNotFoundException("User does not have an account"));
    }

    public BigDecimal convertMoneyByRate(String currency, BigDecimal amount) {
        double exchangeRate = currencyService
                .fetchRates()
                .getConversionRate(currency)
                .getValue();

        BigDecimal exchangeRateBigDecimal = new BigDecimal(exchangeRate);
        return amount.multiply(exchangeRateBigDecimal);
    }
}

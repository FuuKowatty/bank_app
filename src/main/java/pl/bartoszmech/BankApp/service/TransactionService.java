package pl.bartoszmech.BankApp.service;

import org.springframework.stereotype.Service;
import pl.bartoszmech.BankApp.enums.TransactionType;
import pl.bartoszmech.BankApp.exception.AccountNotFoundException;
import pl.bartoszmech.BankApp.model.Account;
import pl.bartoszmech.BankApp.model.Transaction;
import pl.bartoszmech.BankApp.repository.AccountRepository;
import pl.bartoszmech.BankApp.repository.TransactionRepository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;


    public TransactionService(TransactionRepository transactionRepository, AccountRepository accountRepository) {
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
    }

    public void savePayment(BigDecimal amount, String currency, long accountId) {
        Transaction newTransaction = new Transaction();
        newTransaction.setAmount(amount);
        newTransaction.setCurrency(currency);
        newTransaction.setAccountTo(
                accountRepository
                        .findById(accountId)
                        .orElseThrow(() -> new AccountNotFoundException("Account not found"))
        );
        newTransaction.setTransactionType(TransactionType.PAYMENT);
        transactionRepository.save(newTransaction);
    }

    public void saveTransfer(BigDecimal amount, String currency, long accountToId, long accountFromId, TransactionType type) {
        Transaction newTransaction = new Transaction();
        newTransaction.setAmount(amount);
        newTransaction.setCurrency(currency);
        newTransaction.setAccountTo(
                accountRepository
                        .findById(accountToId)
                        .orElseThrow(() -> new AccountNotFoundException("Account not found"))
        );
        newTransaction.setAccountFrom(
                accountRepository
                        .findById(accountFromId)
                        .orElseThrow(() -> new AccountNotFoundException("Account not found"))
        );
        newTransaction.setTransactionType(type);
        transactionRepository.save(newTransaction);
    }

    public List<Transaction> findById(long userId) {
        List<Long> userAccountsId = accountRepository.findByUserId(userId).stream()
                .map(Account::getId)
                .toList();

        return userAccountsId.stream()
                .flatMap(accountId -> Stream.concat(
                        transactionRepository.findByAccountFromId(accountId).stream(),
                        transactionRepository.findByAccountToId(accountId).stream()))
                .sorted(Comparator.comparing(Transaction::getDate))
                .toList();
    }
}

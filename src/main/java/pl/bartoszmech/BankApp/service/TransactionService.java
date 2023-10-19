package pl.bartoszmech.BankApp.service;

import org.springframework.stereotype.Service;
import pl.bartoszmech.BankApp.model.Transaction;
import pl.bartoszmech.BankApp.repository.TransactionRepository;

import java.math.BigDecimal;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;


    public TransactionService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public void save(BigDecimal amount, String currency, long accountId) {
        Transaction newTransaction = new Transaction();
//        newTransaction.setAmount(amount);

    }
}

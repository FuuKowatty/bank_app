package pl.bartoszmech.BankApp.service;

import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import pl.bartoszmech.BankApp.enums.ExceptionMessages;
import pl.bartoszmech.BankApp.exception.InvalidValueException;
import pl.bartoszmech.BankApp.model.Transaction;
import pl.bartoszmech.BankApp.model.User;

import java.io.*;
import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

@Service
public class InputService {
    public static final DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM, FormatStyle.MEDIUM);
    public static final String transactionsFileName = "transactions.txt";
    private final AccountService accountService;
    private final Scanner scanner;
    private final ResourceLoader resourceLoader;
    public InputService(AccountService accountService, ResourceLoader resourceLoader) {
        this.accountService = accountService;
        this.resourceLoader = resourceLoader;
        this.scanner = new Scanner(System.in);
    }
    public byte printMenu(boolean hasForeignAccount) {
        System.out.println("----BankApp MENU---- (" + CurrencyService.actualCurrency + ")");
        System.out.println("1. See your balance");
        System.out.println("2. Withdraw money");
        System.out.println("3. Deposit money");
        if (hasForeignAccount) {
            System.out.println("4. Swap accounts");
        } else {
            System.out.println("4. Create foreign account");
        }
        System.out.println("5. Transfer");
        System.out.println("6. Transactions History");
        System.out.println("7. Quit App");
        byte choice = scanner.nextByte();
        scanner.nextLine();

        return choice;
    }



    public String askForName() {
        System.out.println("Whats your name?");
        return scanner.nextLine();
    }

    public BigDecimal askForAmount() {
        System.out.println("How much money do you want to provide?");
        BigDecimal amount = scanner.nextBigDecimal();
        scanner.nextLine();
        if(amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new InvalidValueException(ExceptionMessages.INVALID_VALUE.getMessage());
        }
        return amount;
    }

    public Byte askForAuthentication() {
        System.out.println("Welcome!");
        System.out.println("1. Login");
        System.out.println("2. Register");

        byte choice = scanner.nextByte();
        scanner.nextLine();

        if(choice <= 0 || choice > 2) {
            throw new InvalidValueException(ExceptionMessages.INVALID_VALUE_AUTHENTICATION.getMessage());
        }

        return choice;
    }

    public User askForLogin() {
        User user = new User();
        System.out.print("Username: ");
        user.setUsername(scanner.nextLine());

        System.out.print("Password: ");
        user.setPassword(scanner.nextLine());

        return user;

    }

    public User askForRegister() {
        User user = new User();
        System.out.print("First Name: ");
        user.setFirstName(scanner.nextLine());

        System.out.print("Last Name: ");
        user.setLastName(scanner.nextLine());

        System.out.print("Username: ");
        user.setUsername(scanner.nextLine());

        System.out.print("Password: ");
        user.setPassword(scanner.nextLine());

        return user;
    }

    public String askForCurrency() {
        Map<String, String> allowedCurrencies = CurrencyService.allowedCurrencies();
        System.out.println("Select your new account currency");

        for (Map.Entry<String, String> entry : allowedCurrencies.entrySet()) {
            System.out.println(entry.getKey() + ". " + entry.getValue());
        }
        byte choice = scanner.nextByte();

        if(choice > 0 && choice <= 5) {
            return allowedCurrencies.get("" + choice);
        }

        throw new InvalidValueException("Provided value is not valid");
    }


    public byte askForAccount() {
        System.out.println("Where are you transferring your money");
        System.out.println("1. To my second account");
        System.out.println("2. To other client account");
        byte choice = scanner.nextByte();
        scanner.nextLine();
        return choice;
    }

    public String askForUsername() {
        System.out.println("Please enter the username of the user you want to transfer money to");
        return scanner.nextLine();
    }

    public StringBuilder printTransactions(List<Transaction> transactions) {
        StringBuilder output = new StringBuilder();

        for (Transaction transaction : transactions) {
            output.append("Date: ").append(InputService.formatter.format(transaction.getDate())).append("\n");
            output.append("Amount: ").append(transaction.getAmount() + " " + transaction.getCurrency()).append("\n");
            if (transaction.getAccountFrom() != null) {
                output.append("From Account: ").append(accountService.findUsernameByAccountId(transaction.getAccountFrom().getId())).append("\n");
            } else {
                output.append("From Account: N/A\n");
            }
            output.append("To Account: ").append(accountService.findUsernameByAccountId(transaction.getAccountTo().getId())).append("\n");
            output.append("-----------------------------------------------------\n");
        }

        return output;
    }

    public void printUserTransactions(List<Transaction> transactions) {
        System.out.println(printTransactions(transactions));
    }

    public byte askForPrintMethod() {
        System.out.println("1. Print to console");
        System.out.println("2. Print to note");
        byte choice = scanner.nextByte();
        scanner.nextLine();
        if(choice != 1 && choice != 2) {
            throw new InvalidValueException("Provided value is not valid");
        }
        return choice;
    }

    public void saveTransactions(List<Transaction> transactions){
      try(FileWriter writer = new FileWriter(InputService.transactionsFileName)) {
          StringBuilder transactionsOutput = printTransactions(transactions);
          writer.write(transactionsOutput.toString());
      } catch (IOException e) {
          throw new RuntimeException(e);
      }
    }
}

package pl.bartoszmech.BankApp.service;

import org.springframework.stereotype.Service;
import pl.bartoszmech.BankApp.enums.ExceptionMessages;
import pl.bartoszmech.BankApp.exception.InvalidValueException;
import pl.bartoszmech.BankApp.model.User;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Scanner;

@Service
public class InputService {

    private final Scanner scanner;
    public InputService() {
        this.scanner = new Scanner(System.in);
    }
    public byte printMenu(boolean hasForeignAccount) {
        System.out.println("----BankApp MENU---- (" + CurrencyService.actualCurrency + ")");
        System.out.println("1. See your balance");
        System.out.println("2. Deposit money");
        System.out.println("3. Withdraw money");
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

        //make exception (invalidValue exception could take arguments and be used there)

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


    public double askForAccount() {
        System.out.println("Where are you transferring your money");
        System.out.println("1. To my foreign account");
        System.out.println("2. To other client account");
        double choice = scanner.nextDouble();
        scanner.nextLine();
        return choice;
    }

    public String askForUsername() {
        System.out.println("Please enter the username of the user you want to transfer money to");
        return scanner.nextLine();
    }
}

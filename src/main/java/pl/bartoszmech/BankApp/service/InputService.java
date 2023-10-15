package pl.bartoszmech.BankApp.service;

import org.springframework.stereotype.Service;
import pl.bartoszmech.BankApp.enums.ExceptionMessages;
import pl.bartoszmech.BankApp.exception.InvalidValueException;
import pl.bartoszmech.BankApp.model.User;

import java.util.Scanner;

@Service
public class InputService {

    private final Scanner scanner;
    public InputService() {
        this.scanner = new Scanner(System.in);
    }
    public byte printMenu() {
        System.out.println("----BankApp MENU----");
        System.out.println("1. See your balance");
        System.out.println("2. Deposit money");
        System.out.println("3. Withdraw money");
        System.out.println("4. Quit App");
        byte choice = scanner.nextByte();
        scanner.nextLine();

        //make exception (invalidValue exception could take arguments and be used there)

        return choice;
    }



    public String askForName() {
        System.out.println("Whats your name?");
        return scanner.nextLine();
    }

    public Double askForAmount() {
        System.out.println("How much money do you want to provide?");
        double amount = scanner.nextDouble();
        scanner.nextLine();

        if(amount < 0) {
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
}

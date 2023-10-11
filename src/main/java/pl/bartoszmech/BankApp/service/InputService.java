package pl.bartoszmech.BankApp.service;

import org.springframework.stereotype.Service;

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
        return choice;
    }

    public String askForName() {
        System.out.println("Whats your name?");
        return scanner.nextLine();
    }

    public Double askForAmount() {
        System.out.println("How much money do you want to provide?");
        Double amount = scanner.nextDouble();
        scanner.nextLine();
        return amount;
    }

}

package pl.bartoszmech.BankApp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import pl.bartoszmech.BankApp.model.User;
import pl.bartoszmech.BankApp.repository.UserRepository;

import java.util.Scanner;

@SpringBootApplication
public class Application implements CommandLineRunner {
	private final Scanner scanner = new Scanner(System.in);
	@Autowired
	UserRepository userRepository;
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Override
	public void run(String... args) {

		System.out.println("Whats your name?");
		String firstName = scanner.nextLine();

		// login simulation
		Long loggedUserId = loginUser(firstName);

		byte choice;
		do {
			printMenu();
			choice = scanner.nextByte();
			scanner.nextLine();

			switch (choice) {
				case 1:
					viewBalance();
					break;
				case 2:
					depositMoney();
					break;
				case 3:
					withdrawMoney();
					break;
				default:
					break;
			}

		} while (choice != 5);


	}

	private Long loginUser(String firstName) {
		var user = new User();
		user.setFirstName(firstName);
		return userRepository.save(user).getId();
	}

	private void withdrawMoney() {
	}

	private void depositMoney() {

	}

	private void viewBalance() {

	}

	private void printMenu() {
		System.out.println("----BankApp MENU----");
		System.out.println("1. See your balance");
		System.out.println("2. Deposit money");
		System.out.println("3. Withdraw money");
	}

}

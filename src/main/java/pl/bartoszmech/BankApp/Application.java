package pl.bartoszmech.BankApp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import pl.bartoszmech.BankApp.exception.UserNotFoundException;
import pl.bartoszmech.BankApp.model.Account;
import pl.bartoszmech.BankApp.model.User;
import pl.bartoszmech.BankApp.service.AccountService;
import pl.bartoszmech.BankApp.service.UserService;

import java.util.List;
import java.util.Scanner;

@SpringBootApplication
public class Application implements CommandLineRunner {
	private static final String DEFAULT_CURRENCY = "EURO";
	private static final Double LIMIT_DEBIT = -2000D;
	private final Scanner scanner = new Scanner(System.in);

	@Autowired private AccountService accountService;
	@Autowired private UserService userService;

	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(Application.class);

		// disable spring banner
		app.setBannerMode(Banner.Mode.OFF);
		app.run(args);
	}

	@Override
	public void run(String... args) throws UserNotFoundException {

		// ITS DEMO VERSION IN LATER VERSION IT WILL BE NORMAL AUTHENTICATION
		System.out.println("Whats your name?");
		String firstName = scanner.nextLine();
		// login simulation
		Long loggedUserId = RegisterUser(firstName);

		byte choice;
		do {
			printMenu();
			choice = scanner.nextByte();
			scanner.nextLine();

			switch (choice) {
				case 1:
					viewBalance(loggedUserId);
					break;
				case 2:
					withdrawMoney(loggedUserId);
					break;
				case 3:
					depositMoney(loggedUserId);
					break;
				default:
					break;
			}

		} while (choice != 4);


	}

	private Long RegisterUser(String firstName) {
		var newUser = new User();
		newUser.setFirstName(firstName);
		Long userId =  userService.saveUser(newUser).getId();

		createAccount(userId);
		return userId;
	}

	private User getUserById(Long userId) {
		return userService.findByUserId(userId);
	}

	private List<Account> getUserAccounts(Long userId) {
		return accountService.getUserAccounts(userId);
	}

	private Double askForAmount() {
		System.out.println("How much money do you want to provide?");
		Double amount = scanner.nextDouble();
		scanner.nextLine();
		return amount;
	}
	private void depositMoney(Long userId) {
		Double amount = askForAmount();
		accountService.depositMoney(userId, DEFAULT_CURRENCY, amount, LIMIT_DEBIT);

	}

	private void withdrawMoney(Long userId) {
		Double amount = askForAmount();
		accountService.withdrawMoney(userId, DEFAULT_CURRENCY, amount);
	}

	private void viewBalance(Long userId) {
		List<Account> userAccounts = getUserAccounts(userId);
		System.out.println(userAccounts.get(0).getBalance());
	}

	private void createAccount(Long userId) {
		User user = getUserById(userId);
		accountService.createAccount(user, DEFAULT_CURRENCY);
	}

	private void printMenu() {
		System.out.println("----BankApp MENU----");
		System.out.println("1. See your balance");
		System.out.println("2. Deposit money");
		System.out.println("3. Withdraw money");
		System.out.println("4. Quit App");
	}
}

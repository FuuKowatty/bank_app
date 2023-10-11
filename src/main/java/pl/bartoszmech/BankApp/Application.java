package pl.bartoszmech.BankApp;

import org.springframework.boot.Banner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import pl.bartoszmech.BankApp.exception.UserNotFoundException;
import pl.bartoszmech.BankApp.model.Account;
import pl.bartoszmech.BankApp.model.User;
import pl.bartoszmech.BankApp.service.AccountService;
import pl.bartoszmech.BankApp.service.InputService;
import pl.bartoszmech.BankApp.service.UserService;

import java.util.List;
@SpringBootApplication
public class Application implements CommandLineRunner {
	private static final String DEFAULT_CURRENCY = "EURO";
	private static final Double LIMIT_DEBIT = -2000D;

	private final AccountService accountService;
	private final UserService userService;
	private final InputService inputService;

	public Application(AccountService accountService, UserService userService, InputService inputService) {
		this.accountService = accountService;
		this.userService = userService;
		this.inputService = inputService;
	}

	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(Application.class);

		// disable spring banner
		app.setBannerMode(Banner.Mode.OFF);


		app.run(args);
	}

	@Override
	public void run(String... args) throws UserNotFoundException {

		// ITS DEMO VERSION IN LATER VERSION IT WILL BE NORMAL AUTHENTICATION
		String firstName = inputService.askForName();
		// login simulation
		Long loggedUserId = RegisterUser(firstName);

		byte choice;
		do {
			choice = inputService.printMenu();
			switch (choice) {
				case 1 -> viewBalance(loggedUserId);
				case 2 -> withdrawMoney(loggedUserId, DEFAULT_CURRENCY);
				case 3 -> depositMoney(loggedUserId, DEFAULT_CURRENCY);
			}
		} while (choice != 4);
	}

	private Long RegisterUser(String firstName) {
		// creates user and gets id
		Long userId =  userService.saveUser(new User(firstName)).getId();
		createAccount(userId);
		return userId;
	}

	private void depositMoney(Long userId, String currency) {
		Double amount = (inputService.askForAmount() * -1); // because we sum up to our balance
		Account userAccount = accountService.getUserAccountByCurrency(userId, currency);

		boolean isLegalDebit = checkIfLegal(userAccount.getBalance(), amount);

		if (!isLegalDebit) {
			System.out.println("Deposit failed. The transaction exceeds the debit limit.");
			return;
		}
		accountService.updateBalance(userAccount, amount);
	}

	public boolean checkIfLegal(Double currentBalance, Double amount) {
		return currentBalance + amount > Application.LIMIT_DEBIT;
	}

	private void withdrawMoney(Long userId, String currency) {
		Double amount = inputService.askForAmount();
		accountService.updateBalance(
				accountService.getUserAccountByCurrency(userId, currency),
				amount
		);
	}

	private void viewBalance(Long userId) {
		List<Account> userAccounts = accountService.getUserAccounts(userId);
		System.out.println(userAccounts.get(0).getBalance());
	}

	private void createAccount(Long userId) {
		User user = userService.findByUserId(userId);;
		accountService.createAccount(user, DEFAULT_CURRENCY);
	}
}

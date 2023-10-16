package pl.bartoszmech.BankApp;

import org.springframework.boot.Banner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import pl.bartoszmech.BankApp.exception.UserNotFoundException;
import pl.bartoszmech.BankApp.model.Account;
import pl.bartoszmech.BankApp.service.*;

import java.io.IOException;
import java.util.List;
@SpringBootApplication
public class Application implements CommandLineRunner {
	private static final String DEFAULT_CURRENCY = "EURO";
	private static final Double LIMIT_DEBIT = -2000D;

	private final AccountService accountService;
	private final UserService userService;
	private final InputService inputService;
	private final AuthenticationService authenticationService;
	private final CurrencyService currencyService;

	public Application(AccountService accountService, UserService userService, InputService inputService, AuthenticationService authenticationService, CurrencyService currencyService) {
		this.accountService = accountService;
		this.userService = userService;
		this.inputService = inputService;
		this.authenticationService = authenticationService;
		this.currencyService = currencyService;
	}

	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(Application.class);
		// disable spring banner
		app.setBannerMode(Banner.Mode.OFF);
		app.run(args);
	}

	@Override
	public void run(String... args) throws UserNotFoundException, IOException {
		Long loggedUserId;
		byte authenticateOption = inputService.askForAuthentication();
		if(authenticateOption == 1) {
			loggedUserId = authenticationService.login();
		} else if (authenticateOption == 2) {
			loggedUserId = authenticationService.register(DEFAULT_CURRENCY);
		} else {
			System.out.println("Something went wrong");
			return;
		}
		byte choice;
		do {
			choice = inputService.printMenu();
			switch (choice) {
				case 1 -> viewBalance(loggedUserId);
				case 2 -> withdrawMoney(loggedUserId, DEFAULT_CURRENCY);
				case 3 -> depositMoney(loggedUserId, DEFAULT_CURRENCY);
				case 4 -> createForeignAccount(loggedUserId);
			}
		} while (choice != 5);
	}

	private void createForeignAccount(Long loggedUserId) {
		String output = inputService.askForCurrency();
		String currency = accountService.getCurrencySymbol(output);
		accountService.createAccount(userService.findByUserId(loggedUserId), currency);
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

}

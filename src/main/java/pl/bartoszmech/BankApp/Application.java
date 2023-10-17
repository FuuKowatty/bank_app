package pl.bartoszmech.BankApp;

import org.springframework.boot.Banner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import pl.bartoszmech.BankApp.exception.AccountNotFoundException;
import pl.bartoszmech.BankApp.exception.InvalidValueException;
import pl.bartoszmech.BankApp.exception.UserNotFoundException;
import pl.bartoszmech.BankApp.model.Account;
import pl.bartoszmech.BankApp.model.User;
import pl.bartoszmech.BankApp.service.*;

import java.util.Objects;

@SpringBootApplication
public class Application implements CommandLineRunner {
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
	public void run(String... args) throws UserNotFoundException {
		Long loggedUserId;
		byte authenticateOption = inputService.askForAuthentication();
		if(authenticateOption == 1) {
			loggedUserId = authenticationService.login();
		} else if (authenticateOption == 2) {
			loggedUserId = authenticationService.register();
		} else {
			System.out.println("Something went wrong");
			return;
		}
		byte choice;
		do {
			choice = inputService.printMenu(accountService.hasMultipleAccounts(loggedUserId));
			Account userAccount = accountService.findAccountByUserId(loggedUserId);
			switch (choice) {
				case 1 -> viewBalance(userAccount);
				case 2 -> withdrawMoney(userAccount);
				case 3 -> depositMoney(userAccount);
				case 4 -> {
					if(accountService.hasMultipleAccounts(loggedUserId)) {
						changeAccount(loggedUserId);
					} else {
						createForeignAccount(loggedUserId);
					}
				}
				case 5 -> transferMoney(loggedUserId);
			};
		} while (choice != 6);
	}

	private void transferMoney(long userId) {
		double choice = inputService.askForAccount();

		if(choice != 1D && choice != 2D) {
			throw new InvalidValueException("Provided option is not valid");
		}
		double amount = inputService.askForAmount();
		if (choice == 1) {
			accountService.transferMoneyToForeignAccount(userId, amount);
		} else {
			User senderUser = userService.findByUserId(userId);
			User recipentUser = userService.findByUsername(inputService.askForUsername());
			accountService.transferMoneyToOtherUser(senderUser, recipentUser, amount);
		}
	}

	private void changeAccount(long userId) {
		CurrencyService.actualCurrency = accountService
				.getUserAccounts(userId).stream().filter(a -> !Objects.equals(a.getCurrency(), CurrencyService.actualCurrency))
				.findFirst()
				.orElseThrow(() -> new AccountNotFoundException(""))
				.getCurrency();
	}

	private void createForeignAccount(Long loggedUserId) {
		accountService
				.createAccount(
						userService.findByUserId(loggedUserId),
						accountService.getCurrencySymbol(inputService.askForCurrency())
				);
	}

	private void depositMoney(Account account) {
		Double amount = (inputService.askForAmount() * -1); // because we sum up to our balance

		boolean isLegalDebit = checkIfLegal(account.getBalance(), amount);

		if (!isLegalDebit) {
			System.out.println("Deposit failed. The transaction exceeds the debit limit.");
			return;
		}
		accountService.addToBalance(account, amount);
	}

	public boolean checkIfLegal(Double currentBalance, Double amount) {
		return currentBalance + amount > Application.LIMIT_DEBIT;
	}

	private void withdrawMoney(Account account) {
		Double amount = inputService.askForAmount();
		accountService.addToBalance(
				account,
				amount
		);
	}

	private void viewBalance(Account account) {
		System.out.println(account.getBalance());
	}



}

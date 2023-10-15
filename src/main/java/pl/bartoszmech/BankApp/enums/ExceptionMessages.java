package pl.bartoszmech.BankApp.enums;

import lombok.Getter;

@Getter
public enum ExceptionMessages {
    USER_NOT_FOUND("User not found"),
    ACCOUNT_NOT_FOUND("Account not found"),
    INVALID_VALUE("Please enter a number greater than 0"),
    INVALID_VALUE_AUTHENTICATION("Bad Number!");

    private final String message;

    ExceptionMessages(String message) {
        this.message = message;
    }
}

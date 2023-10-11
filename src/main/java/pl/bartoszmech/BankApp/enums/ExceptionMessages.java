package pl.bartoszmech.BankApp.enums;

import lombok.Getter;

@Getter
public enum ExceptionMessages {
    USER_NOT_FOUND("User not found");
    private final String message;

    ExceptionMessages(String message) {
        this.message = message;
    }
}

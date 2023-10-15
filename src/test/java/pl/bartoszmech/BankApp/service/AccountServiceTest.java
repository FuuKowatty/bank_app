package pl.bartoszmech.BankApp.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.bartoszmech.BankApp.exception.AccountNotFoundException;
import pl.bartoszmech.BankApp.model.User;
import pl.bartoszmech.BankApp.repository.AccountRepository;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AccountServiceTest {
    @InjectMocks
    private AccountService accountService;
    @Mock
    private AccountRepository accountRepository;

    User user;
    String currency;
    @BeforeEach
    void testData() {
        user = new User("John", "Wick", "Jwick123", "password1");
        currency = "EURO";
    }

    @Test
    void getUserAccountByCurrency_invalidCurrency_ThrowsAccountNotFoundException() {
        // Given
        testData();

        //when
        when(accountService.getUserAccounts(user.getId())).thenThrow(AccountNotFoundException.class);

        //then
        assertThrows(AccountNotFoundException.class, () -> {
            accountService.getUserAccountByCurrency(user.getId(), currency);
        });
    };
}
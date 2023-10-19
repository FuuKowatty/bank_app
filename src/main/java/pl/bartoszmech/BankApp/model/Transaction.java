package pl.bartoszmech.BankApp.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.bartoszmech.BankApp.enums.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Table(name = "transactions")
@Entity
@Getter
@Setter
@NoArgsConstructor
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(cascade = CascadeType.MERGE)
    private Account accountFrom = null;
    @ManyToOne(cascade = CascadeType.MERGE)
    private Account accountTo;
    private String currency;
    private BigDecimal amount;
    private LocalDateTime date = LocalDateTime.now();
    @Enumerated(EnumType.STRING)
    private TransactionType transactionType;
}

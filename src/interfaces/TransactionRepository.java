package interfaces;

import modules.Transaction;

import java.math.BigDecimal;
import java.util.UUID;

public interface TransactionRepository {
    UUID create(Transaction transaction);
    boolean withdraw(BigDecimal amount);
    boolean transfer(BigDecimal amount);
    boolean showHistory(BigDecimal amount);

}

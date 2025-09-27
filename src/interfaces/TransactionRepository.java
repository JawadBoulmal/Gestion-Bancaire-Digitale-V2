package interfaces;

import modules.Account;
import modules.Transaction;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.UUID;

public interface TransactionRepository {
    UUID create(Transaction transaction) throws SQLException;
    boolean deposit(BigDecimal amount, Account transferIN, Account transferOUT) throws SQLException;
    boolean withdraw(BigDecimal amount);
    boolean transfer(BigDecimal amount);
    boolean showHistory(BigDecimal amount);

}

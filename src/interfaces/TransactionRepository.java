package interfaces;

import enums.TransactionsType;
import modules.Account;
import modules.Transaction;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.UUID;

public interface TransactionRepository {
    UUID create(Transaction transaction) throws SQLException;
    UUID deposit(BigDecimal amount, Account transferIN, Account transferOUT) throws SQLException;
    UUID withdraw(BigDecimal amount,Account transferIN,Account transferOUT) throws SQLException;
    UUID transfer(BigDecimal amount,Account transferIN,Account transferOUT) throws SQLException;
    boolean showHistory(BigDecimal amount);

}

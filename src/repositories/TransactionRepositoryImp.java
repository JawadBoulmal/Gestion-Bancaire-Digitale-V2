package repositories;

import enums.TransactionsType;
import enums.VirementStatus;
import interfaces.TransactionRepository;
import modules.Account;
import modules.Transaction;
import modules.User;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.UUID;

public class TransactionRepositoryImp implements TransactionRepository {
    private final Connection connection;

    public TransactionRepositoryImp(Connection connection) {
        this.connection = connection;
    }

    @Override
    public UUID create(Transaction transaction) throws SQLException {
        try{
            String insertTransaction = """
            INSERT INTO transactions(amount, transferIN, transferOUT, type, status, fee_Rule, created_at, updated_at)
            VALUES (
                    ?,
                    ?,
                    ?,
                    ?,
                    ?,
                    null,
                    now(),
                    now()
                   )
        """;

            try (PreparedStatement stmt = connection.prepareStatement(insertTransaction)) {
                stmt.setObject(1, transaction.getAmount());
                stmt.setObject(2, UUID.fromString(transaction.getTransferIN().getId()));
                stmt.setObject(3, UUID.fromString(transaction.getTransferOUT().getId()));
                stmt.setObject(4, transaction.getType().name(), java.sql.Types.OTHER);
                stmt.setObject(5, transaction.getStatus().name(), java.sql.Types.OTHER);
                stmt.executeUpdate();
            }
            return transaction.getId();
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public boolean deposit(BigDecimal amount,Account transferIN,Account transferOUT) throws SQLException {
        UUID id = UUID.randomUUID();
        Transaction transaction = new Transaction(
                id,
                amount,
                transferIN,
                transferOUT,
                TransactionsType.DEPOSIT,
                VirementStatus.PENDING,
                null,
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        UUID result = this.create(transaction);
        if(result != null){
            String insertTransaction = """
                   UPDATE accounts SET solde = ? , updated_at = now() where id = ?;
                   """;
            try (PreparedStatement stmt = connection.prepareStatement(insertTransaction)) {
                stmt.setObject(1, transferIN.getSolde().add(transaction.getAmount()));
                stmt.setObject(2, UUID.fromString(transaction.getTransferIN().getId()));
                stmt.executeUpdate();
            }
            return true;
        }
        return false;
    }


    @Override
    public boolean withdraw(BigDecimal amount,Account transferIN,Account transferOUT) throws SQLException {
        UUID id = UUID.randomUUID();
        Transaction transaction = new Transaction(
                id,
                amount,
                transferIN,
                transferOUT,
                TransactionsType.WITHDRAW,
                VirementStatus.SEETLED,
                null,
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        UUID result = this.create(transaction);
        if(result != null){
            String insertTransaction = """
                   UPDATE accounts SET solde = ? , updated_at = now() where id = ?;
                   """;
            try (PreparedStatement stmt = connection.prepareStatement(insertTransaction)) {
                stmt.setObject(1, transferIN.getSolde().subtract(transaction.getAmount()));
                stmt.setObject(2, UUID.fromString(transaction.getTransferIN().getId()));
                stmt.executeUpdate();
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean transfer(BigDecimal amount) {
        return false;
    }

    @Override
    public boolean showHistory(BigDecimal amount) {
        return false;
    }
}

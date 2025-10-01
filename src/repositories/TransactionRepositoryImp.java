package repositories;

import enums.TransactionsType;
import enums.VirementStatus;
import interfaces.BankFeeRepository;
import interfaces.TransactionRepository;
import modules.Account;
import modules.BankFee;
import modules.Transaction;
import modules.User;
import services.BankFeeService;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.UUID;

public class TransactionRepositoryImp implements TransactionRepository {
    private final Connection connection;
    private BankFeeRepositoryImp bankFeeRepo;
    private BankFeeService bankFeeService;


    public TransactionRepositoryImp(Connection connection) {
        this.connection = connection;
    }

    @Override
    public UUID create(Transaction transaction) throws SQLException {
        try{
            String insertTransaction = """
            INSERT INTO transactions(amount, transferIN, transferOUT, type, status,id, fee_Rule, created_at, updated_at)
            VALUES (
                    ?,
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
                stmt.setObject(6, transaction.getId());
                stmt.executeUpdate();
            }
            return transaction.getId();
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public UUID deposit(BigDecimal amount,Account transferIN,Account transferOUT) throws SQLException {
        UUID id = UUID.randomUUID();
        Transaction transaction = new Transaction(
                id,
                amount,
                transferIN,
                transferOUT,
                TransactionsType.DEPOSIT,
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
                stmt.setObject(1, transferIN.getSolde().add(transaction.getAmount()));
                stmt.setObject(2, UUID.fromString(transaction.getTransferIN().getId()));
                stmt.executeUpdate();
            }
            return result;
        }
        return result;
    }


    @Override
    public UUID withdraw(BigDecimal amount,Account transferIN,Account transferOUT) throws SQLException {
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
            return result;
        }
        return result;
    }

    @Override
    public UUID transfer(BigDecimal amount,Account transferIN,Account transferOUT,boolean isOut) throws SQLException  {
        UUID id = UUID.randomUUID();
        Transaction transaction ;
        if(!isOut){
            transaction = new Transaction(
                    id,
                    amount,
                    transferIN,
                    transferOUT,
                    TransactionsType.TRANSFERIN,
                    VirementStatus.SEETLED,
                    null,
                    LocalDateTime.now(),
                    LocalDateTime.now()
            );
        }else{
            transaction = new Transaction(
                    id,
                    amount,
                    transferIN,
                    transferOUT,
                    TransactionsType.TRANSFEROUT,
                    VirementStatus.PENDING,
                    null,
                    LocalDateTime.now(),
                    LocalDateTime.now()
            );
        }


        connection.setAutoCommit(false);
        UUID result = null;
        try{
            BigDecimal amountWithPercent = amount.add(amount.multiply(new BigDecimal("0.05")));
            try (PreparedStatement stmt = connection.prepareStatement(
                    "UPDATE accounts SET solde = ?, updated_at = now() WHERE id = ?")) {
                stmt.setBigDecimal(1, transferIN.getSolde().subtract(amountWithPercent));
                stmt.setObject(2, transferIN.getId(),java.sql.Types.OTHER);
                stmt.executeUpdate();
            }

            try (PreparedStatement stmt = connection.prepareStatement(
                    "UPDATE accounts SET solde = ?, updated_at = now() WHERE id = ?")) {
                stmt.setBigDecimal(1, transferOUT.getSolde().add(amount));
                stmt.setObject(2, transferOUT.getId(),java.sql.Types.OTHER);
                stmt.executeUpdate();
            }

            result = this.create(transaction);
            connection.commit();

            if(result != null && isOut){
                this.bankFeeRepo = new BankFeeRepositoryImp(this.connection);
                this.bankFeeService = new BankFeeService(this.bankFeeRepo);
                BankFee bankFee = new BankFee(
                        UUID.randomUUID(),
                        transaction,
                        "This is transfer out",
                        BigDecimal.ZERO,
                        amount.multiply(new BigDecimal("0.05")),
                        LocalDateTime.now(),
                        LocalDateTime.now()
                );
                this.bankFeeService.SaveBankFee(bankFee);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            connection.rollback();
        } finally {
            connection.setAutoCommit(true);
        }
        return result;
    }

    @Override
    public boolean showHistory(BigDecimal amount) {
        return false;
    }
}

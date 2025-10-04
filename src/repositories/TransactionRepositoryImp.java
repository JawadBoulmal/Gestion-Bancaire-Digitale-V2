package repositories;

import enums.TransactionsType;
import enums.VirementStatus;
import interfaces.TransactionRepository;
import modules.Account;
import modules.BankFee;
import modules.Transaction;
import services.BankFeeService;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TransactionRepositoryImp implements TransactionRepository {
    private final Connection connection;
    private BankFeeRepositoryImp bankFeeRepo;
    private BankFeeService bankFeeService;
    private AccountRepositoryImp AccountRepo;


    public TransactionRepositoryImp(Connection connection) {
        this.connection = connection;
        this.AccountRepo = new AccountRepositoryImp(connection);
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
    public List<String> showHistory() {
        List<Transaction> transactions = getAll();
        List<String> history = new ArrayList<>();

        for (Transaction t : transactions) {
            history.add(generateTransactionHistory(t));
        }

        return history;
    }
    public String generateTransactionHistory(Transaction t) {
        String senderName = (t.getTransferOUT() != null && t.getTransferOUT().getClient() != null)
                ? t.getTransferOUT().getClient().getFirstName() + " " + t.getTransferOUT().getClient().getLastName()
                : "Unknown Sender";

        String receiverName = (t.getTransferIN() != null && t.getTransferIN().getClient() != null)
                ? t.getTransferIN().getClient().getFirstName() + " " + t.getTransferIN().getClient().getLastName()
                : "Unknown Receiver";

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        String date = t.getCreatedAt() != null ? t.getCreatedAt().format(formatter) : "Unknown Date";

        switch (t.getType()) {
            case TRANSFERIN:
                return date + " | TRANSFER IN  | " + receiverName + " received " + t.getAmount() + " DH "
                        + "from account type " + (t.getTransferOUT() != null ? t.getTransferOUT().getType() : "Unknown")
                        + " to account type " + (t.getTransferIN() != null ? t.getTransferIN().getType() : "Unknown");

            case TRANSFEROUT:
                return date + " | TRANSFER OUT | " + senderName + " sent " + t.getAmount() + " DH to " + receiverName;

            case DEPOSIT:
                return date + " | DEPOSIT      | " + senderName + " deposited " + t.getAmount() + " DH into his account";

            case WITHDRAW:
                return date + " | WITHDRAW     | " + senderName + " withdrew " + t.getAmount() + " DH from his account";

            default:
                return date + " | TRANSACTION  | " + senderName + " and " + receiverName + " | Amount: " + t.getAmount() + " DH";
        }
    }

    public List<Transaction> getAll() {
        List<Transaction> transactions = new ArrayList<>();
        String sql = """
            SELECT t.*,
                   ReceiverAcc.id   AS ReceiverID,
                   SenderAcc.id     AS SenderID,
                   CONCAT(cReceiver.firstName, ' ', cReceiver.lastName) AS receiver_name,
                   cReceiver.email  AS receiver_email,
                   CONCAT(cSender.firstName, ' ', cSender.lastName)     AS sender_name,
                   cSender.email    AS sender_email
            FROM transactions t
                     LEFT JOIN accounts ReceiverAcc ON ReceiverAcc.id = t.transferIN
                     LEFT JOIN accounts SenderAcc   ON SenderAcc.id = t.transferOUT
                     LEFT JOIN client cReceiver     ON cReceiver.id = ReceiverAcc.UserID
                     LEFT JOIN client cSender       ON cSender.id   = SenderAcc.UserID
            ORDER BY t.created_at DESC
            """;

        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Account transferINAcc = this.AccountRepo.getAccountById(
                        rs.getString("ReceiverID") != null ? UUID.fromString(rs.getString("ReceiverID")) : null
                );
                Account transferOUTAcc = this.AccountRepo.getAccountById(
                        rs.getString("SenderID") != null ? UUID.fromString(rs.getString("SenderID")) : null
                );

                Transaction t = new Transaction(
                        rs.getObject("id", java.util.UUID.class),
                        rs.getBigDecimal("amount"),
                        transferINAcc,
                        transferOUTAcc,
                        TransactionsType.valueOf(rs.getString("type")),
                        VirementStatus.valueOf(rs.getString("status")),
                        null,
                        rs.getObject("created_at", LocalDateTime.class),
                        rs.getObject("updated_at", LocalDateTime.class)
                );
                transactions.add(t);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return transactions;
    }
}

package repositories;

import interfaces.BankFeeRepository;
import modules.BankFee;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class BankFeeRepositoryImp implements BankFeeRepository {
    private Connection connection;

    public BankFeeRepositoryImp(Connection conn){
        this.connection = conn;
    }

    public UUID save(BankFee bankFee){
        String sql = """
           INSERT INTO BankFee(transaction, description, debit_amount, credit_amount)
           VALUES (?,?,?,?)
           RETURNING id
           """;
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setObject(1, bankFee.getTransaction().getId());
            stmt.setString(2, bankFee.getDescription());
            stmt.setBigDecimal(3, bankFee.getDebit_amount());
            stmt.setBigDecimal(4, bankFee.getCredit_amount());
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getObject("id", UUID.class);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}

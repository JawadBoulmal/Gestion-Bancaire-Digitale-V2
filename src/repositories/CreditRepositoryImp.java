package repositories;

import enums.AccountType;
import enums.CreditStatus;
import enums.CreditType;
import enums.VirementStatus;
import interfaces.CreditRepository;
import modules.Account;
import modules.Client;
import modules.Credit;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CreditRepositoryImp implements CreditRepository {
    private Connection conn;

    public CreditRepositoryImp(Connection conn){
        this.conn = conn;
    }

    @Override
    public boolean demander(Credit credit) {
        String sql = """
    INSERT INTO credits(amount, duree, taux, justification, account, feeRule, credit_type, status,amountpayeach)
    VALUES (?, ?, ?, ?, ?, NULL, ?, ?, ?)
    """;

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setBigDecimal(1, credit.getAmount());
            stmt.setInt(2, credit.getDuree());
            stmt.setFloat(3, credit.getTaux());
            stmt.setString(4, credit.getJutstification());
            stmt.setObject(5, UUID.fromString(credit.getAccount().getId()));
            stmt.setObject(6, credit.getCreditType(), java.sql.Types.OTHER);
            stmt.setObject(7, credit.getStatus(), java.sql.Types.OTHER);
            stmt.setObject(8, credit.getAmountEach());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error inserting credit: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<Credit> getCreditsByUserID(UUID userid) {
        List<Credit> credits = new ArrayList<>();
        String sql = """
        SELECT
            accounts.id as account_id ,
            accounts.userid as user_id,
            credits.id as credit_account_id,
            accounts.isactive,
            accounts.type,
            accounts.solde,
            credits.created_at,
            credits.updated_at ,
            credits.amount,
            credits.duree,
            credits.taux,
            credits.justification,
            credits.account,
            credits.amountpayeach,
            feerule,
            credit_type,
            status,
            type,
            solde,
            client.firstname,
            client.lastname,
            client.telephone,
            client.role,
            client.email,
            client.salaire,
            client.cin
            FROM Credits
            JOIN accounts ON accounts.id = Credits.account
            JOIN client ON client.id = accounts.userid
            WHERE client.id = ?
        """;
        try(PreparedStatement stmt = conn.prepareStatement(sql)){
            stmt.setObject(1,userid);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Client client = new Client(
                        rs.getObject("user_id", java.util.UUID.class),
                        rs.getString("firstname"),
                        rs.getString("lastname"),
                        rs.getString("telephone"),
                        rs.getString("cin"),
                        rs.getBigDecimal("salaire"),
                        rs.getString("email"),
                        "Not"
                );
                Account account = new Account(
                        rs.getString("account_id"),
                        rs.getBigDecimal("solde"),
                        AccountType.valueOf(rs.getString("type")),
                        client,
                        rs.getTimestamp("created_at").toLocalDateTime(),
                        rs.getTimestamp("updated_at").toLocalDateTime()
                );
                Credit credit = new Credit(
                        rs.getObject("credit_account_id", java.util.UUID.class),
                        rs.getBigDecimal("amount"),
                        rs.getInt("duree"),
                        rs.getFloat("taux"),
                        null,
                        rs.getString("justification"),
                        CreditType.valueOf(rs.getString("credit_type")),
                        account,
                        CreditStatus.valueOf(rs.getString("status")),
                        rs.getBigDecimal("amountpayeach"),
                        rs.getObject("created_at",LocalDateTime.class),
                        rs.getObject("updated_at",LocalDateTime.class)
                );
                credits.add(credit);
            }
            return credits;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}

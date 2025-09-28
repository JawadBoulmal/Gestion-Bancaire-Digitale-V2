package repositories;

import enums.AccountType;
import interfaces.AccountRepository;
import modules.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Objects;
import java.util.UUID;

public class AccountRepositoryImp implements AccountRepository {
    private final Connection connection;
    public AccountRepositoryImp(Connection conn){
        this.connection = conn;
    }


    @Override
    public UUID create(Account account) {
        String sql = """
           INSERT INTO accounts(userid,isactive,type,solde,created_at,updated_at)
           VALUES (?,?,?::accounttype,?,now(),now())
           RETURNING id
           """;
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setObject(1, account.getClient().getId());
            stmt.setBoolean(2, true);
            stmt.setObject(3, account.getType().name(), java.sql.Types.OTHER);
            stmt.setBigDecimal(4, account.getSolde());
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getObject("id", UUID.class);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    @Override
    public Account getAccountById(UUID id) {
        String sql = """
            SELECT c.id, c.firstName, c.lastName, c.email, c.telephone, c.role, c.salaire, c.cin ,a.id as account_id, a.isActive , a.type , a.solde , a.created_at , a.updated_at FROM Accounts a
            LEFT JOIN client c ON c.id  = a.userid WHERE a.id = ?
           """;
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setObject(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Client client = new Client(
                        rs.getObject("id", java.util.UUID.class),
                        rs.getString("firstname"),
                        rs.getString("lastname"),
                        rs.getString("telephone"),
                        rs.getString("cin"),
                        rs.getBigDecimal("salaire"),
                        rs.getString("email"),
                        "Not"
                );
                return new Account(
                        rs.getString("account_id"),
                        rs.getBigDecimal("solde"),
                        AccountType.valueOf(rs.getString("type")),
                        client,
                        LocalDateTime.now(),
                        LocalDateTime.now()
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    @Override
    public ArrayList<Account> getAccountsByUserID(UUID userID) {
        String sql = """
            SELECT u.id as user_id, u.firstName, u.lastName, u.email, u.salaire, u.cin, u.telephone, u.role, 
                   a.id as account_id, a.solde, a.type, a.isActive, a.created_at, a.updated_at
            FROM accounts a
            LEFT JOIN client u ON u.id = a.userid
            WHERE u.id = ?
            """;

        ArrayList<Account> accounts = new ArrayList<>();

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setObject(1, userID);

            try (ResultSet rs = stmt.executeQuery()) {
                Client client = null;

                while (rs.next()) {
                    if (client == null) {
                        client = new Client(
                                rs.getObject("user_id", java.util.UUID.class),
                                rs.getString("firstname"),
                                rs.getString("lastname"),
                                rs.getString("telephone"),
                                rs.getString("cin"),
                                rs.getBigDecimal("salaire"),
                                rs.getString("email"),
                                "Not"
                        );
                    }

                    Account account = new Account(
                            rs.getString("account_id"),
                            rs.getBigDecimal("solde"),
                            AccountType.valueOf(rs.getString("type")),
                            client,
                            rs.getTimestamp("created_at").toLocalDateTime(),
                            rs.getTimestamp("updated_at").toLocalDateTime()
                    );

                    accounts.add(account);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return accounts;
    }


    @Override
    public User updateProfile() {
        return null;
    }

    @Override
    public boolean updatePassword() {
        return false;
    }

    @Override
    public boolean closeORactive(boolean status , UUID id) {
            String sql =
           """
           UPDATE accounts SET isActive = ? , updated_at = now() WHERE accounts.id = ?
           """;
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setBoolean(1, status);
                stmt.setObject(2, id);
                int rowUpdated = stmt.executeUpdate();
                if(rowUpdated > 0){
                    return true;
                }
                return false;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        return false;
    }


}

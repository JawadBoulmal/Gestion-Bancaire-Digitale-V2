package interfaces;

import modules.Account;
import modules.User;

import java.util.ArrayList;
import java.util.UUID;

public interface AccountRepository {
    UUID create(Account account);
    User updateProfile();
    Account getAccountById(UUID id);
    ArrayList<Account> getAccountsByUserID(UUID id);
    boolean updatePassword();
    boolean close();
}

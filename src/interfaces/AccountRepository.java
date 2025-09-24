package interfaces;

import modules.Account;
import modules.User;

import java.util.ArrayList;
import java.util.UUID;

public interface AccountRepository {
    UUID create(User user);
    ArrayList<Account> ListAccounts();
    User updateProfile();
    boolean updatePassword();
    boolean close();

}

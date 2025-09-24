package repositories;

import interfaces.AccountRepository;
import modules.Account;
import modules.User;

import java.util.ArrayList;
import java.util.UUID;

public class AccountRepositoryImp implements AccountRepository {
    @Override
    public UUID create(User user) {
        return null;
    }

    @Override
    public ArrayList<Account> ListAccounts() {
        return null;
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
    public boolean close() {
        return false;
    }
}

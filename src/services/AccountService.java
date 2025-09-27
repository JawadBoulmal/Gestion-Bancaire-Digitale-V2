package services;

import Utils.Utils;
import enums.AccountType;
import interfaces.AccountRepository;
import interfaces.UserRepository;
import modules.Account;
import modules.Client;
import modules.User;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

public class AccountService {
    private AccountRepository AccountRepo ;
    private UserService UserService ;

    public AccountService (AccountRepository repository,UserRepository UserRepo){
        this.AccountRepo = repository;
        this.UserService = new UserService(UserRepo);
    }

    public ArrayList<Account> getAccountByUserId(String user_id){
        if(!Utils.isValidUUID(user_id)){
            return null;
        }
        return this.AccountRepo.getAccountsByUserID(UUID.fromString(user_id));
    }

    public UUID CreateNewAccount(UUID userID , BigDecimal amount, AccountType type){
        String accountID = String.valueOf(UUID.randomUUID());
        User client = this.UserService.getUserById("app", Optional.of(userID));

        Account account = new Account(
                accountID,
                amount,
                type,
                (Client) client,
                LocalDateTime.now(),
                LocalDateTime.now()
        );
        return this.AccountRepo.create(account);
    }

}

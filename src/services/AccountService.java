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
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import Utils.*;

import static Utils.ConsoleColors.colorizeCell;


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

    public void listUserAccounts(List<Account> accountUser){
        System.out.printf(
                "\n| %-5s | %-36s | %-20s | %-30s |\n",
                colorizeCell("key", ConsoleColors.YELLOW,5),
                colorizeCell("Account ID", ConsoleColors.YELLOW,36),
                colorizeCell("Solde", ConsoleColors.YELLOW,20),
                colorizeCell("Type", ConsoleColors.YELLOW,30));
        int index = 1;
        for(Account account : accountUser){
            System.out.printf("| %-5s | %-36s | %-20s | %-30s | \n",index,account.getId(),account.getSolde(),account.getType());
            index++;
        }
    }

    public boolean getAccountById(UUID AccID){
        Account acc = this.AccountRepo.getAccountById(AccID);
        if(acc != null){
            return true;
        }
        return false;
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

    public boolean closeORactiveAcc(boolean status, UUID id){
        if(getAccountById(id)){
            return this.AccountRepo.closeORactive(status,id);
        }
        System.out.println("Account Not Found !");
        return false;
    }

}

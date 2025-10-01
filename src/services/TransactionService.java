package services;

import enums.TransactionsType;
import enums.VirementStatus;
import interfaces.AccountRepository;
import interfaces.TransactionRepository;
import interfaces.UserRepository;
import modules.Account;
import modules.Fee_rule;
import modules.Transaction;
import repositories.UserRepositoryImp;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public class TransactionService {
    private TransactionRepository TransactionRepo ;
    private AccountRepository AccountRepo ;

    public TransactionService(TransactionRepository repository,AccountRepository AccountRepo){
        this.TransactionRepo = repository;
        this.AccountRepo = AccountRepo;
    }

    public UUID AddTransaction(BigDecimal Ammount, UUID transferIN, UUID transferOUT, TransactionsType type , VirementStatus Status) throws SQLException {
        Account transferINAcc = this.AccountRepo.getAccountById(transferIN);
        Account transferOUTAcc = this.AccountRepo.getAccountById(transferOUT);

        UUID result = null;
        if(type == TransactionsType.DEPOSIT){
            result = this.TransactionRepo.deposit(Ammount,transferINAcc,transferOUTAcc);
        } else if (type == TransactionsType.WITHDRAW) {
            if(transferINAcc.getSolde().compareTo(Ammount) < 0){
                System.out.println("The Account Solde is not enough to withdraw !!");
                return null;
            }
            result = this.TransactionRepo.withdraw(Ammount,transferINAcc,transferOUTAcc);
        } else if (type == TransactionsType.TRANSFERIN) {
            if(transferINAcc.getSolde().compareTo(Ammount) < 0){
                System.out.println("The Account Solde is not enough to transfer !!");
                return null;
            }
            result = this.TransactionRepo.transfer(Ammount,transferINAcc,transferOUTAcc,false);
        } else if (type == TransactionsType.TRANSFEROUT) {
            if(transferINAcc.getSolde().compareTo(Ammount.multiply(new BigDecimal("0.05"))) < 0){
                System.out.println("The Account Solde is not enough to transfer !!");
                System.out.println("Remember the fees will be applied +5%");
                return null;
            }
            result = this.TransactionRepo.transfer(Ammount,transferINAcc,transferOUTAcc,true);
        }
        if(result != null){
            return result;
        }else{
            return null;
        }
    }
}

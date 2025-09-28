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

    public UUID AddTransaction(BigDecimal Ammount, UUID transferIN, UUID transferOUT, TransactionsType type) throws SQLException {
        UUID id = UUID.randomUUID();
        Account transferINAcc = this.AccountRepo.getAccountById(transferIN);
        Account transferOUTAcc = this.AccountRepo.getAccountById(transferOUT);
        Transaction transaction = new Transaction(
                id,
                Ammount,
                transferINAcc,
                transferOUTAcc,
                type,
                VirementStatus.PENDING,
                null,
                LocalDateTime.now(),
                LocalDateTime.now()
        );
        boolean result = false ;
        if(type == TransactionsType.DEPOSIT){
            result = this.TransactionRepo.deposit(Ammount,transferINAcc,transferOUTAcc);
        } else if (type == TransactionsType.WITHDRAW) {
            result = this.TransactionRepo.withdraw(Ammount,transferINAcc,transferOUTAcc);
        }
        if(result){
            return this.TransactionRepo.create(transaction);
        }else{
            return null;
        }
    }
}

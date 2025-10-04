package services;

import interfaces.TransactionRepository;
import modules.Transaction;

import java.sql.SQLException;
import java.util.List;

public class RapportService {
    private final TransactionRepository transactionRepo;

    public RapportService(TransactionRepository transactionRepo) {
        this.transactionRepo = transactionRepo;
    }

    public void getAllTransactions() throws SQLException {
        List<String> transactionsHistory = transactionRepo.showHistory();
        for (String History : transactionsHistory){
            System.out.println(History);
        }
    }
}

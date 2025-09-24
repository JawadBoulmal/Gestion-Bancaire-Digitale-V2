package repositories;

import interfaces.TransactionRepository;
import modules.Transaction;

import java.math.BigDecimal;
import java.util.UUID;

public class TransactionRepositoryImp implements TransactionRepository {
    @Override
    public UUID create(Transaction transaction) {
        return null;
    }

    @Override
    public boolean withdraw(BigDecimal amount) {
        return false;
    }

    @Override
    public boolean transfer(BigDecimal amount) {
        return false;
    }

    @Override
    public boolean showHistory(BigDecimal amount) {
        return false;
    }
}

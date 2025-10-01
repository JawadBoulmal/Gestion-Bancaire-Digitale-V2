package modules;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public class BankFee {
    private UUID id;
    private Transaction transaction;
    private String description;
    private BigDecimal debit_amount;
    private BigDecimal credit_amount;
    private LocalDateTime created_at;
    private LocalDateTime updated_at;

    public BankFee(UUID id, Transaction transaction, String description, BigDecimal debit_amount, BigDecimal credit_amount, LocalDateTime created_at, LocalDateTime updated_at) {
        this.id = id;
        this.transaction = transaction;
        this.description = description;
        this.debit_amount = debit_amount;
        this.credit_amount = credit_amount;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Transaction getTransaction() {
        return transaction;
    }

    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getDebit_amount() {
        return debit_amount;
    }

    public void setDebit_amount(BigDecimal debit_amount) {
        this.debit_amount = debit_amount;
    }

    public BigDecimal getCredit_amount() {
        return credit_amount;
    }

    public void setCredit_amount(BigDecimal credit_amount) {
        this.credit_amount = credit_amount;
    }

    public LocalDateTime getCreated_at() {
        return created_at;
    }

    public void setCreated_at(LocalDateTime created_at) {
        this.created_at = created_at;
    }

    public LocalDateTime getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(LocalDateTime updated_at) {
        this.updated_at = updated_at;
    }
}

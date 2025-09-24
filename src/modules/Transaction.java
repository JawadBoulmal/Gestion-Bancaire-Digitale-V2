package modules;

import enums.TransactionsType;
import enums.VirementStatus;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;

public class Transaction {
    private UUID id ;
    private BigDecimal amount ;
    private Account transferIN ;
    private Account transferOUT ;
    private TransactionsType type ;
    private VirementStatus Status ;
    private Fee_rule fee_Rule ;
    private Date updatedAt ;
    private Date createdAt ;

    public Transaction(UUID id, BigDecimal amount, Account transferIN, Account transferOUT, TransactionsType type, VirementStatus status, Fee_rule fee_Rule, Date updatedAt, Date createdAt) {
        this.id = id;
        this.amount = amount;
        this.transferIN = transferIN;
        this.transferOUT = transferOUT;
        this.type = type;
        this.Status = status;
        this.fee_Rule = fee_Rule;
        this.updatedAt = updatedAt;
        this.createdAt = createdAt;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Account getTransferIN() {
        return transferIN;
    }

    public void setTransferIN(Account transferIN) {
        this.transferIN = transferIN;
    }

    public Account getTransferOUT() {
        return transferOUT;
    }

    public void setTransferOUT(Account transferOUT) {
        this.transferOUT = transferOUT;
    }

    public TransactionsType getType() {
        return type;
    }

    public void setType(TransactionsType type) {
        this.type = type;
    }

    public VirementStatus getStatus() {
        return Status;
    }

    public void setStatus(VirementStatus status) {
        Status = status;
    }

    public Fee_rule getFee_Rule() {
        return fee_Rule;
    }

    public void setFee_Rule(Fee_rule fee_Rule) {
        this.fee_Rule = fee_Rule;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}

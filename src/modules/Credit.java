package modules;

import enums.CreditStatus;
import enums.CreditType;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

public class Credit {

    private UUID id;
    private BigDecimal amount;
    private int duree;
    private float taux;
    private Fee_rule feeRule;
    private String jutstification;
    private CreditType creditType;
    private Account account;
    private CreditStatus status;
    private BigDecimal amountEach;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Credit(UUID id, BigDecimal amount, int duree, float taux, Fee_rule feeRule, String jutstification, CreditType creditType, Account account, CreditStatus status, BigDecimal amountEach, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.amount = amount;
        this.duree = duree;
        this.taux = taux;
        this.feeRule = feeRule;
        this.jutstification = jutstification;
        this.creditType = creditType;
        this.account = account;
        this.status = status;
        this.amountEach = amountEach;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public BigDecimal getAmountEach() {
        return amountEach;
    }

    public void setAmountEach(BigDecimal amountEach) {
        this.amountEach = amountEach;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public CreditStatus getStatus() {
        return status;
    }

    public void setStatus(CreditStatus status) {
        this.status = status;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public CreditType getCreditType() {
        return creditType;
    }

    public void setCreditType(CreditType creditType) {
        this.creditType = creditType;
    }

    public String getJutstification() {
        return jutstification;
    }

    public void setJutstification(String jutstification) {
        this.jutstification = jutstification;
    }

    public Fee_rule getFeeRule() {
        return feeRule;
    }

    public void setFeeRule(Fee_rule feeRule) {
        this.feeRule = feeRule;
    }

    public float getTaux() {
        return taux;
    }

    public void setTaux(float taux) {
        this.taux = taux;
    }

    public int getDuree() {
        return duree;
    }

    public void setDuree(int duree) {
        this.duree = duree;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }
}

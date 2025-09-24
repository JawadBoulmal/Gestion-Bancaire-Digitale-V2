package modules;

import enums.AccountType;
import enums.TransactionsType;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class Account {
    private String id ;
    private Client client ;
    private Boolean isActive;
    private AccountType type;
    private BigDecimal solde;
    private Date createdAt ;
    private Date updatedAt ;

    public Account(String accountId , BigDecimal balance , Client client,Date createdAt,Date updatedAt) {
        this.id = accountId;
        this.isActive = true;
        this.solde = balance;
        this.client = client;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }


    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    public AccountType getType() {
        return type;
    }

    public void setType(AccountType type) {
        this.type = type;
    }

    public BigDecimal getSolde() {
        return solde;
    }

    public void setSolde(BigDecimal solde) {
        this.solde = solde;
    }


}

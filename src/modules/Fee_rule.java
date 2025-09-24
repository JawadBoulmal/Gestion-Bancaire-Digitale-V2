package modules;

import enums.ModeFeeRule;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

public class Fee_rule {
    private UUID id;
    private String operation_type;
    private ModeFeeRule mode;
    private BigDecimal value;
    private String currency;
    private Boolean is_active;
    private Date createdAt;
    private Date updatedAt;

    public Fee_rule(UUID id, String operation_type, ModeFeeRule mode, BigDecimal value, String currency, Boolean is_active, Date createdAt, Date updatedAt) {
        this.id = id;
        this.operation_type = operation_type;
        this.mode = mode;
        this.value = value;
        this.currency = currency;
        this.is_active = is_active;
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

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getOperation_type() {
        return operation_type;
    }

    public void setOperation_type(String operation_type) {
        this.operation_type = operation_type;
    }

    public ModeFeeRule getMode() {
        return mode;
    }

    public void setMode(ModeFeeRule mode) {
        this.mode = mode;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Boolean getIs_active() {
        return is_active;
    }

    public void setIs_active(Boolean is_active) {
        this.is_active = is_active;
    }
}

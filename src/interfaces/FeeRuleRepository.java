package interfaces;

import modules.Fee_rule;

import java.util.UUID;

public interface FeeRuleRepository {
    UUID create(Fee_rule feeRule);
    Fee_rule update(Fee_rule feeRule);
    boolean delete(UUID feeRuleId);
    boolean disactivate(UUID feeRuleId);
}

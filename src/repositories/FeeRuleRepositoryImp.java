package repositories;

import interfaces.FeeRuleRepository;
import modules.Fee_rule;

import java.util.UUID;

public class FeeRuleRepositoryImp implements FeeRuleRepository {
    @Override
    public UUID create(Fee_rule feeRule) {
        return null;
    }

    @Override
    public Fee_rule update(Fee_rule feeRule) {
        return null;
    }

    @Override
    public boolean delete(UUID feeRuleId) {
        return false;
    }

    @Override
    public boolean disactivate(UUID feeRuleId) {
        return false;
    }
}

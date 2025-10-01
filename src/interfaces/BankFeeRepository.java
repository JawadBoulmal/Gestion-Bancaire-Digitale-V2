package interfaces;

import modules.BankFee;

import java.util.UUID;

public interface BankFeeRepository {
    UUID save(BankFee bankFee);

}

package services;

import interfaces.BankFeeRepository;
import modules.BankFee;

import java.util.UUID;

public class BankFeeService {
    private BankFeeRepository bankFeeRepository;

    public BankFeeService(BankFeeRepository bankFeeRepository){
        this.bankFeeRepository = bankFeeRepository;
    }
    public UUID SaveBankFee(BankFee bankFee){
        return this.bankFeeRepository.save(bankFee);
    }

}

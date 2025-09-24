package interfaces;

import modules.Credit;

import java.util.UUID;

public interface CreditRepository {
    UUID demander(Credit credit);
}

package interfaces;

import modules.Credit;

import java.util.List;
import java.util.UUID;

public interface CreditRepository {
    boolean demander(Credit credit);
    List<Credit> getCreditsByUserID(UUID userid);
}

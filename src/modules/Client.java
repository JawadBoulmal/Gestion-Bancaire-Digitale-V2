package modules;

import enums.Roles;

import java.math.BigDecimal;
import java.util.UUID;

public class Client extends User {
    private BigDecimal salaire;

    public Client(UUID id, String firstName, String lastName, String telephone, String CIN, BigDecimal salaire, String email,  String password ) {
        super(id, firstName,lastName,telephone,CIN, email,  password);
        this.salaire = salaire;
        this.role = Roles.CLIENT;
    }

    public BigDecimal getSalaire() {
        return salaire;
    }

    public void setSalaire(BigDecimal salaire) {
        this.salaire = salaire;
    }
}

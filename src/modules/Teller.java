package modules;

import java.util.UUID;

public class Teller extends User{

    public Teller(UUID id, String firstName, String lastName, String telephone, String CIN, String email, String password) {
        super(id, firstName, lastName, telephone, CIN, email, password);
        this.role = "TELLER";
    }
}

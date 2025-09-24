package modules;

import enums.Roles;

import java.util.UUID;

public class Auditor extends User{

    public Auditor(UUID id, String firstName, String lastName, String telephone, String CIN, String email, String password) {
        super(id, firstName, lastName, telephone, CIN, email, password);
        this.role = Roles.AUDITOR;
    }
}

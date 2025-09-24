package modules;

import enums.Roles;

import java.util.UUID;

public class Admin extends User{

    public Admin(UUID id, String firstName, String lastName, String telephone, String CIN, String email,  String password) {
        super(id, firstName, lastName, telephone, CIN, email,  password);
        this.role = Roles.ADMIN;
    }
}

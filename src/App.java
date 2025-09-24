import Database.Database;
import interfaces.UserRepository;
import repositories.UserRepositoryImp;
import services.AuthService;
import services.UserService;
import view.Acceuil;
import view.AuthView;

import java.sql.Connection;
import java.sql.SQLException;

public class App {
    public static void main(String[] args) throws SQLException {
        AuthView AuthView = new AuthView();
        Acceuil Acceuil = new Acceuil();
        Acceuil.showPrincipleMenu();

    }
}

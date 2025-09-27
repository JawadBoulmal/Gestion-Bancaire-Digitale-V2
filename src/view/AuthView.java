package view;

import Database.Database;
import enums.Roles;
import interfaces.UserRepository;
import repositories.UserRepositoryImp;
import services.AuthService;
import services.UserService;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AuthView {
    private Scanner scanner = new Scanner(System.in);
    Connection conn ;
    UserRepository UserRepository ;
    AuthService AuthService ;
    UserService UserService ;
    Acceuil AcceuilView ;
    boolean isConnected ;

    public AuthView(){
        conn = Database.getInstance().getConnection();
        UserRepository = new UserRepositoryImp(conn);
        AuthService = new AuthService(UserRepository);
        UserService = new UserService(UserRepository);
        isConnected = services.AuthService.isIsOnline();
    }

    public void showLoginMenu() throws SQLException {
        System.out.println(isConnected);

        System.out.println("===================================");
        System.out.println("         🔐 Login Menu              ");
        System.out.println("===================================");

        System.out.print("📧 Email    : ");
        String email = scanner.next();

        System.out.print("🔑 Password : ");
        String password = scanner.next();

        services.AuthService.setIsOnline(this.AuthService.Login(email,password));
        AcceuilView = new Acceuil();
    }


}

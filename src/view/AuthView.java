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
    private Scanner scanner2 = new Scanner(System.in);
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
        isConnected = services.AuthService.isOnline;
    }

    public void showLoginMenu() throws SQLException {
        System.out.println("===================================");
        System.out.println("         🔐 Login Menu              ");
        System.out.println("===================================");

        System.out.print("📧 Email    : ");
        String email = scanner.next();

        System.out.print("🔑 Password : ");
        String password = scanner.next();

        this.AuthService.Login(email,password);
        AcceuilView = new Acceuil();
    }
    public void showRegisterMenu() throws SQLException {
        System.out.println("===================================");
        System.out.println("         🔐 Add User Menu              ");
        System.out.println("===================================");

        System.out.print("📧 First Name    : ");
        String firstName = scanner.nextLine();

        System.out.print("📧 Last Name    : ");
        String lasttName = scanner.nextLine();

        System.out.print("📧 Telephone    : ");
        String telephone = scanner.nextLine();

        System.out.print("📧 CIN    : ");
        String CIN = scanner.nextLine();

        System.out.print("📧 Salaire    : ");
        BigDecimal Salaire = scanner2.nextBigDecimal();

        System.out.print("🔑 Address : ");
        String address = scanner.nextLine();

        System.out.print("📧 Email    : ");
        String email = scanner.nextLine();

        while(!AuthService.validationEmail(email)){
            System.out.println("Email Not Valid Try Again !");
            System.out.print("📧 Email    : ");
            email = scanner.nextLine();
        }

        System.out.print("🔑 Password : ");
        String password = scanner.nextLine();

        while(!AuthService.validationPassword(password)){
            System.out.println("Password Must Be More Than 6 Characters !");
            System.out.print("🔑 Password : ");
            password = scanner.nextLine();
        }

        boolean result = AuthService.Register(firstName,lasttName,telephone,CIN,Salaire,email,password, Roles.CLIENT);
        if(result){
            System.out.println("╔═══════════════════════════════════════════════════════╗");
            System.out.println("   User "+firstName+" Has been added successfully");
            System.out.println("╚═══════════════════════════════════════════════════════╝");
            showLoginMenu();
        }else{
            System.out.println("╔═══════════════════════════════════════════════════════╗");
            System.out.println("   ✘ Login creation failed!           ");
            System.out.println("   🔄 No account associated with this informations.           ");
            System.out.println("╚═══════════════════════════════════════════════════════╝");
            showRegisterMenu();
        }
    }

}

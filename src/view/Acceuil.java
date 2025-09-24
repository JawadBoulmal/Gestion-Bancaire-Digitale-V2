package view;

import Database.Database;
import interfaces.UserRepository;
import modules.User;
import repositories.UserRepositoryImp;
import services.AuthService;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;

import static java.lang.System.exit;

public class Acceuil {
    private Scanner scanner = new Scanner(System.in);
    Connection conn ;
    UserRepository UserRepository ;
    AuthService AuthService ;
    boolean isConnected = services.AuthService.isOnline;
    private User userInfos = UserRepositoryImp.userInfo;
    private AuthView AuthView ;

    public Acceuil() throws SQLException {
        conn = Database.getInstance().getConnection();
        UserRepository = new UserRepositoryImp(conn);
        AuthService = new AuthService(UserRepository);
        this.showPrincipleMenu();
    }

    public void showPrincipleMenu() throws SQLException {
        System.out.print(isConnected);
        if(isConnected){
            System.out.println("===========================================");
            System.out.println("  🔐 Logged in as: " + userInfos.getFirstName() + " " + userInfos.getLastName());
            System.out.println("===========================================");
            System.out.println("  1️⃣    Create account");
            System.out.println("  2️⃣    List my accounts");
            System.out.println("  3️⃣    Deposit");
            System.out.println("  4️⃣    Withdraw");
            System.out.println("  5️⃣    Transfer");
            System.out.println("  6️⃣    Update profile");
            System.out.println("  7️⃣    Change password");
            System.out.println("  8️⃣    Close account");
            System.out.println("  9️⃣    Logout");
            System.out.println("  1️0️⃣  Transactions History");
            System.out.println("  0️⃣    Exit");
            System.out.println("===========================================");
            System.out.print("➤ Choose an option: ");

            int choice = scanner.nextInt();
        }else {
            AuthView = new AuthView();
            AuthView.showLoginMenu();
        }
    }
}

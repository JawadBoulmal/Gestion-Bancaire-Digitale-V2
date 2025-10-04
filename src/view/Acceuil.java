package view;

import Database.Database;
import enums.Roles;
import interfaces.UserRepository;
import interfaces.View;
import modules.User;
import repositories.UserRepositoryImp;
import services.AuthService;
import services.UserService;
import view.ChoicesView.ComptesView;
import view.ChoicesView.CreditView;
import view.ChoicesView.RapportView;
import view.ChoicesView.TransactionsView;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.InputMismatchException;
import java.util.Scanner;

import static java.lang.System.exit;
import static services.AuthService.setIsOnline;

public class Acceuil implements View {
    private Scanner scanner = new Scanner(System.in);
    Connection conn ;
    UserRepository UserRepository ;
    AuthService AuthService ;
    boolean isConnected;
    private User userInfos = UserRepositoryImp.userInfo;
    private AuthView AuthView ;

    public Acceuil() throws SQLException {
        conn = Database.getInstance().getConnection();
        UserRepository = new UserRepositoryImp(conn);
        AuthService = new AuthService(UserRepository);
        AuthView = new AuthView();
        this.isConnected = services.AuthService.isIsOnline();

        this.showPrincipleMenu();
    }

    public void logout() throws SQLException {
        this.isConnected = false;
        this.userInfos = null;
        UserRepositoryImp.userInfo = null;
        System.out.println("LOGGED OUT SUCCESSFULLY !");
        showPrincipleMenu();
    }

    @Override
    public void pincipaleMenu() throws SQLException {
        Roles role = userInfos.getRole();
        if(role != null){
            System.out.println("=============================================================");
            System.out.println("  Connecté (« Logged in as [ "+ userInfos.getFirstName() + " " + userInfos.getLastName()+" ] as "+role+" ») " );
            System.out.println("=============================================================");
            switch (role){
                case AUDITOR -> {
                    System.out.println("  1️⃣  Rapports");
                    System.out.println("  2️⃣  Logout");
                    System.out.println("=====================================================");
                    System.out.print("➤ Choose an option: ");

                    int choice = scanner.nextInt();
                    if (choice == 1) {
                        RapportView RapportView = new RapportView();
                        RapportView.pincipaleMenu();
                    } else if (choice == 2) {
                        logout();
                    }
                    System.err.println("\nINVALID OPTION\n");
                    showPrincipleMenu();
                }
                case ADMIN -> {
                    System.out.println("  1️⃣  Comptes");
                    System.out.println("  2️⃣  Transactions");
                    System.out.println("  3️⃣  Crédits");
                    System.out.println("  4️⃣  Rapports");
                    System.out.println("  5️⃣  Logout");
                    System.out.println("=====================================================");
                    System.out.print("➤ Choose an option: ");

                    int choice = scanner.nextInt();

                    switch(choice){
                        case 1:
                            ComptesView ComptesView = new ComptesView();
                            ComptesView.pincipaleMenu();
                            break;
                        case 2:
                            TransactionsView TransactionsView = new TransactionsView();
                            TransactionsView.pincipaleMenu();
                            break;
                        case 3:
                            CreditView CreditView = new CreditView();
                            CreditView.pincipaleMenu();
                        case 4:
                            RapportView RapportView = new RapportView();
                            RapportView.pincipaleMenu();
                        case 5:
                            logout();
                            break;
                        default:
                            System.err.println("\nINVALID OPTION\n");
                            showPrincipleMenu();
                            break;
                    }
                }
                case MANAGER -> {
                    System.out.println("  1️⃣  Comptes");
                    System.out.println("  2️⃣  Transactions");
                    System.out.println("  3️⃣  Crédits");
                    System.out.println("  4️⃣  Logout");
                    System.out.println("=====================================================");
                    System.out.print("➤ Choose an option: ");

                    int choice = scanner.nextInt();

                    switch(choice){
                        case 1:
                            ComptesView ComptesView = new ComptesView();
                            ComptesView.pincipaleMenu();
                            break;
                        case 2:
                            TransactionsView TransactionsView = new TransactionsView();
                            TransactionsView.pincipaleMenu();
                            break;
                        case 3:
                            CreditView CreditView = new CreditView();
                            CreditView.pincipaleMenu();
                        case 4:
                            logout();
                            break;
                        default:
                            System.err.println("\nINVALID OPTION\n");
                            showPrincipleMenu();
                            break;
                    }
                }
                case TELLER -> {

                }
            }
        }else{
            showPrincipleMenu();

        }
    }

    public void showPrincipleMenu() throws SQLException {
        try{
            if(isConnected){
                pincipaleMenu();
            }else {
                System.out.println("===================================");
                System.out.println("        🏠 Accueil (Non Connecté)       ");
                System.out.println("===================================");
                System.out.println("  1️⃣  Login");
                System.out.println("===================================");
                System.out.print("➤ Choose an option: ");
                int choice = scanner.nextInt();
                if (choice == 1) {
                    this.AuthView.showLoginMenu();
                } else {
                    System.err.println("\nINVALID OPTION\n");
                    showPrincipleMenu();
                }
            }
        } catch (InputMismatchException e) {
            System.err.println("\nINVALID INPUT , PLEASE CHOOSE ONE OF THE DISPONIBLE OPTIONS .\n");
            scanner.nextLine();
            showPrincipleMenu();
        }
    }
}

package view;

import Database.Database;
import interfaces.UserRepository;
import modules.User;
import repositories.UserRepositoryImp;
import services.AuthService;
import view.ChoicesView.ComptesView;
import view.ChoicesView.CreditView;
import view.ChoicesView.TransactionsView;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.InputMismatchException;
import java.util.Scanner;

import static java.lang.System.exit;

public class Acceuil {
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

    public void showPrincipleMenu() throws SQLException {
        try{
            if(isConnected){
                System.out.println("===================================================");
                System.out.println("  Connecté (« Logged in as [ "+ userInfos.getFirstName() + " " + userInfos.getLastName()+" ] ») " );
                System.out.println("===================================================");
                System.out.println("  1️⃣    Comptes");
                System.out.println("  2️⃣    Transactions ");
                System.out.println("  3️⃣    Fees");
                System.out.println("  4️⃣    Crédits");
                System.out.println("  5️⃣    Rapports");
                System.out.println("  6️⃣    Compte");
                System.out.println("  0️⃣    Exit");
                System.out.println("===========================================");
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
                    case 4:
                        CreditView CreditView = new CreditView();
                        CreditView.pincipaleMenu();
                    default:
                        System.err.println("\nINVALID OPTION\n");
                        showPrincipleMenu();
                        break;
                }

            }else {
                System.out.println("===================================");
                System.out.println("        🏠 Accueil (Non Connecté)       ");
                System.out.println("===================================");
                System.out.println("  1️⃣  Login");
                System.out.println("  0️⃣  Exit");
                System.out.println("===================================");
                System.out.print("➤ Choose an option: ");
                int choice = scanner.nextInt();
                switch (choice){
                    case 1 :
                        this.AuthView.showLoginMenu();
                        break;
                    case 0 :
                        System.out.println("Exiting the program ...");
                        exit(0);
                        break;
                    default:
                        System.err.println("\nINVALID OPTION\n");
                        showPrincipleMenu();
                        break;
                }
            }
        } catch (InputMismatchException e) {
            System.err.println("\nINVALID INPUT , PLEASE CHOOSE ONE OF THE DISPONIBLE OPTIONS .\n");
            scanner.nextLine();
            showPrincipleMenu();
        }
    }
}

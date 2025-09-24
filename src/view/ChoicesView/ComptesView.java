package view.ChoicesView;

import Database.Database;
import enums.Roles;
import interfaces.UserRepository;
import interfaces.View;
import repositories.UserRepositoryImp;
import services.AuthService;
import view.Acceuil;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Objects;
import java.util.Scanner;

public class ComptesView implements View {
    private Scanner scanner = new Scanner(System.in);
    private Scanner scanner2 = new Scanner(System.in);

    private Connection conn;
    private UserRepository UserRepository ;
    private AuthService AuthService;

    public ComptesView(){
        conn = Database.getInstance().getConnection();
        UserRepository = new UserRepositoryImp(conn);
        AuthService = new AuthService(UserRepository);
    }

    @Override
    public void pincipaleMenu() throws SQLException {
        System.out.println("===================================================");
        System.out.println("             Comptes - Options");
        System.out.println("===================================================");
        System.out.println("  1️⃣   Create account");
        System.out.println("  2️⃣   List my accounts");
        System.out.println("  3️⃣   Update profile");
        System.out.println("  4️⃣   Change password");
        System.out.println("  5️⃣   Close account");
        System.out.println("  0️⃣   Back to Main Menu");
        System.out.println("===================================================");
        System.out.print("➤ Choose an option: ");
        int choice = scanner.nextInt();

        switch (choice){
            case 0 :
                new Acceuil();
                break;
            case 1 :
                System.out.println("Accont for :");
                System.out.println("  1️⃣   Manager");
                System.out.println("  2️⃣   Auditor");
                System.out.println("  3️⃣   Teller");
                System.out.println("  4️⃣   Client");
                int roleChoice = scanner.nextInt();
                switch (roleChoice){
                    case 1:
                        CreateComptes(Roles.MANAGER);
                        break;
                    case 2:
                        CreateComptes(Roles.AUDITOR);
                        break;
                    case 3:
                        CreateComptes(Roles.TELLER);
                        break;
                    case 4:
                        CreateComptes(Roles.CLIENT);
                        break;
                    default:
                        System.err.println("\nINVALID OPTION\n");
                        pincipaleMenu();
                        break;
                }
                break;
            default:
                System.err.println("\nINVALID OPTION\n");
                pincipaleMenu();
                break;
        }
    }

    public void CreateComptes(Roles role) throws SQLException {
        boolean result ;
        BigDecimal Salaire = null;
        String telephone ;

        scanner.nextLine();
        System.out.println("===================================");
        System.out.println("         🔐 Create "+role.toString().toLowerCase()+"              ");
        System.out.println("===================================");

        System.out.print("📧 First Name    : ");
        String firstName = scanner.nextLine();

        System.out.print("📧 Last Name    : ");
        String lasttName = scanner.nextLine();

        System.out.print("📧 Telephone    : ");
        telephone = scanner.nextLine();

        while(UserRepository.existsByTelephone(telephone)){
            System.err.println("\nTelephone Already exists !\n");
            System.out.print("📧 Telephone    : ");
            telephone = scanner.nextLine();
        }

        System.out.print("📧 CIN    : ");
        String CIN = scanner.nextLine();

        if(Objects.equals(role, Roles.CLIENT)){
            System.out.print("📧 Salaire    : ");
            Salaire = scanner2.nextBigDecimal();
        }

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


        if(Objects.equals(role, Roles.CLIENT)){
            result = AuthService.Register(firstName,lasttName,telephone,CIN,Salaire,email,password, Roles.CLIENT);

            if(result){
                System.out.println("╔═══════════════════════════════════════════════════════════════════╗");
                System.out.println("      ✅ User " + firstName + " " + lasttName + " has been added successfully");
                System.out.println("╚═══════════════════════════════════════════════════════════════════╝");
                System.out.println("╔═══════════════════════════════════════════════════════════════════╗");
                System.out.println("                         📝 User Information                        ");
                System.out.println("╠═══════════════════════════════════════════════════════════════════╣");
                System.out.println("  📧 First Name   : " + firstName);
                System.out.println("  📧 Last Name    : " + lasttName);
                System.out.println("  📞 Telephone    : " + telephone);
                System.out.println("  🆔 CIN          : " + CIN);
                System.out.println("  💰 Salaire      : " + Salaire);
                System.out.println("  🏠 Address      : " + address);
                System.out.println("  🏠 Role         : Client");
                System.out.println("  ✉️ Email        : " + email);
                System.out.println("  ✉️ Password     : " + password);
                System.out.println("╚═══════════════════════════════════════════════════════════════════╝");
                pincipaleMenu();
            }else{
                System.out.println("╔═══════════════════════════════════════════════════════╗");
                System.out.println("   ✘ Login creation failed!           ");
                System.out.println("   🔄 No account associated with this informations.           ");
                System.out.println("╚═══════════════════════════════════════════════════════╝");
                CreateComptes(role);
            }
        }else {
            result = AuthService.Register(firstName,lasttName,telephone,CIN,Salaire,email,password, role);
            if(result){
                System.out.println("╔═══════════════════════════════════════════════════════════════════╗");
                System.out.println("      ✅ User " + firstName + " " + lasttName + " has been added successfully");
                System.out.println("╚═══════════════════════════════════════════════════════════════════╝");
                System.out.println("╔═══════════════════════════════════════════════════════════════════╗");
                System.out.println("                         📝 User Information                        ");
                System.out.println("╠═══════════════════════════════════════════════════════════════════╣");
                System.out.println("  📧 First Name   : " + firstName);
                System.out.println("  📧 Last Name    : " + lasttName);
                System.out.println("  📞 Telephone    : " + telephone);
                System.out.println("  🆔 CIN          : " + CIN);
                System.out.println("  🏠 Address      : " + address);
                System.out.println("  🏠 Role         : "+role);
                System.out.println("  ✉️ Email        : " + email);
                System.out.println("  ✉️ Password     : " + password);
                System.out.println("╚═══════════════════════════════════════════════════════════════════╝");
                pincipaleMenu();
            }else{
                System.out.println("╔═══════════════════════════════════════════════════════╗");
                System.out.println("   ✘ Login creation failed!           ");
                System.out.println("   🔄 No account associated with this informations.           ");
                System.out.println("╚═══════════════════════════════════════════════════════╝");
                CreateComptes(role);
            }
        }


    }

}

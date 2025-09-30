package view.ChoicesView;

import Database.Database;
import Utils.ConsoleColors;
import Utils.*;
import enums.AccountType;
import enums.Roles;
import interfaces.UserRepository;
import interfaces.View;
import modules.Account;
import modules.Client;
import modules.User;
import repositories.AccountRepositoryImp;
import repositories.UserRepositoryImp;
import services.AccountService;
import services.AuthService;
import services.UserService;
import view.Acceuil;

import java.math.BigDecimal;
import java.sql.Array;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;

import static Utils.Utils.maskEmail;

public class ComptesView implements View {
    private Scanner scanner = new Scanner(System.in);
    private Scanner scanner2 = new Scanner(System.in);

    private Connection conn;
    private UserRepository UserRepository ;
    private AuthService AuthService;
    private UserService UserService;
    private AccountRepositoryImp accountRepository;
    private AccountService AccountService;

    public ComptesView(){
        conn = Database.getInstance().getConnection();
        UserRepository = new UserRepositoryImp(conn);
        accountRepository = new AccountRepositoryImp(conn);
        AuthService = new AuthService(UserRepository);
        UserService = new UserService(UserRepository);
        AccountService = new AccountService(accountRepository,UserRepository);
    }

    @Override
    public void pincipaleMenu() throws SQLException {
        System.out.println("===================================================");
        System.out.println("             Comptes - Options");
        System.out.println("===================================================");
        String[] options = {
                "Create User",
                "List Users",
                "List Accounts",
                "Update profile",
                "Change password",
                "Close account",
                "Activate account",
                "Create account",
                "Back to Main Menu"
        };

        for (int i = 0; i < options.length; i++) {
            if (i == options.length - 1) {
                System.out.printf("  0️⃣   %s%n", options[i]);
            } else {
                System.out.printf("  %d️⃣   %s%n", i + 1, options[i]);
            }
        }
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
                System.out.print("➤ Choose an option: ");
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
                    case 0:
                        pincipaleMenu();
                        break;
                    default:
                        System.err.println("\nINVALID OPTION\n");
                        pincipaleMenu();
                        break;
                }
                break;
            case 2:
                listUsers();
                pincipaleMenu();
                break;
            case 3:
                listAccounts();
                pincipaleMenu();
                break;
            case 4:
                updateProfile();
                pincipaleMenu();
                break;
            case 6:
                closeOrActiveAccount(false);
                pincipaleMenu();
                break;
            case 7:
                closeOrActiveAccount(true);
                pincipaleMenu();
                break;
            case 8:
                createAccountForUser();
                pincipaleMenu();
                break;

            default:
                System.err.println("\nINVALID OPTION\n");
                pincipaleMenu();
                break;
        }
    }

    private void closeOrActiveAccount(boolean status){
        List<User> users = this.UserService.listUsers();
        System.out.print("Enter The User Key : ");
        int userKey = scanner.nextInt();
        User client = users.get(userKey-1);
        List<Account> userAccounts = this.AccountService.getAccountByUserId(client.getId().toString());

        this.AccountService.listUserAccounts(userAccounts);

        int keyAccount = scanner.nextInt();

        UUID accID;
        try {
            accID = UUID.fromString(userAccounts.get(keyAccount - 1).getId());
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid UUID format!");
            return;
        }
        boolean result = this.AccountService.closeORactiveAcc(status,accID);
        if(result){
            if(status){
                System.out.println("Account has been Activated successfully");
                return;
            }
            System.out.println("Account has been Deactivated successfully");
            return;
        }
        System.out.println("Failed to deactivate the account try again !!");
    }

    private void createAccountForUser() throws SQLException {
        System.out.print("Enter The User ID : ");
        String useridInput = scanner.next();

        UUID userId;
        try {
            userId = UUID.fromString(useridInput);
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid UUID format!");
            return;
        }

        User user = UserService.getUserById("BankSystem", Optional.of(userId));
        if (user == null) {
            System.out.println("No user found !!");
            pincipaleMenu();
            return;
        }
        else if (user.getRole() != Roles.CLIENT){
            System.out.println("The user should be client ! try again with another user .");
            pincipaleMenu();
            return;
        }

        ArrayList<Account> accounts = AccountService.getAccountByUserId(String.valueOf(userId));
        List<AccountType> typesExist = accounts.stream().map(Account::getType).toList();
        List<AccountType> allTypes = Arrays.asList(AccountType.values());

        List<AccountType> availableTypes = allTypes.stream().filter(type -> !typesExist.contains(type)).toList();
        if (availableTypes.isEmpty()) {
            System.out.println("This user already has all account types.");
        } else {
            for (int i = 0; i < availableTypes.size(); i++) {
                System.out.printf(" %d - %s%n", i + 1, availableTypes.get(i).name());
            }
            System.out.println(" 0 - Exit");

            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            if (choice == 0) {
                System.out.println("Returning to main menu...");
                pincipaleMenu();
                return;
            }else if (choice > 0 && choice <= availableTypes.size()) {
                AccountType chosenType = availableTypes.get(choice - 1);
                System.out.println("You chose to create account of type: " + chosenType);

                System.out.println("Enter the initial amount :");
                BigDecimal amount = scanner.nextBigDecimal();

                UUID accountID = AccountService.CreateNewAccount(userId , amount, chosenType );
                if(accountID != null){
                    System.out.println("New account created with ID: " + accountID);
                    pincipaleMenu();
                }else{
                    System.out.println("Failed to create account. Please try again later.");
                    pincipaleMenu();
                }


            } else {
                System.out.println("Invalid choice!");
            }

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

    public void listUsers() {
        List<User> users = UserService.getAll();

        if (users.isEmpty()) {
            System.out.println("⚠️ No users found.");
            return;
        }

        String format = "| %-36s | %-20s | %-30s | %-10s | %-16s |%n";
        System.out.printf(
                format,
                ConsoleColors.colorizeCell("User ID",ConsoleColors.YELLOW,36),
                ConsoleColors.colorizeCell("FULL NAME",ConsoleColors.YELLOW,20),
                ConsoleColors.colorizeCell("EMAIL",ConsoleColors.YELLOW,30),
                ConsoleColors.colorizeCell("ROLE",ConsoleColors.YELLOW,10),
                ConsoleColors.colorizeCell("TOTAL ACCOUNTS",ConsoleColors.YELLOW,16)
               );

        for (User user : users) {
            String fullName = user.getFirstName() + " " + user.getLastName();
            if (user.getPassword().equals("Not")){
                System.out.printf(format,
                        user.getId(),
                        fullName,
                        maskEmail(user.getEmail()),
                        user.getRole().name(),
                        0
                );
            }else{
                System.out.printf(format,
                        user.getId(),
                        fullName,
                        maskEmail(user.getEmail()),
                        user.getRole().name(),
                        user.getPassword()
                );
            }

        }

        System.out.println("================================================================================================================");

    }


    public void updateProfile() throws SQLException {
        scanner.nextLine();
        System.out.print("🔑 Email : ");
        String email = scanner.nextLine();
        User user = UserService.getUserById((email),null);
        if(user !=  null){
            if(user.getRole() == Roles.CLIENT){
                Client client = (Client) user;
                System.out.println("╔══════════════════════════════════════════════════════════╗");
                System.out.printf("  %-15s : %s%n", "- USER ID", client.getId());
                System.out.printf("  %-15s : %s %s%n", "1 - FullName", client.getLastName(), client.getFirstName());
                System.out.printf("  %-15s : %s%n", "2 - Telephone", client.getTelephone());
                System.out.printf("  %-15s : %s%n", "3 - Email", client.getEmail());
                System.out.printf("  %-15s : %s%n", "4 - Role", client.getRole().name());
                System.out.printf("  %-15s : %.2f%n", "5 - Salaire", client.getSalaire());
                System.out.printf("  %-15s  \n", "0 - Exit");
                System.out.println("╚══════════════════════════════════════════════════════════╝");
                System.out.print("➤ Choose an option: ");
                int Choice = scanner.nextInt();
                switch (Choice){
                    case 1 :
                        System.out.println("Old Name : "+client.getLastName() + " " + client.getFirstName() + "\n");
                        System.out.print("➤ New First Name : ");
                        String NewFName = scanner.next();
                        System.out.print("➤ New Last Name : ");
                        String NewLName = scanner.next();
                        user.setFirstName(NewFName);
                        user.setLastName(NewLName);
                        UserService.updateInformation(user);
                    case 0 :
                        pincipaleMenu();
                }
            }else{
                System.out.println("╔══════════════════════════════════════════════════════════╗");
                System.out.printf("  %-15s : %s%n", "- USER ID", user.getId());
                System.out.printf("  %-15s : %s %s%n", "1 - FullName", user.getLastName(), user.getFirstName());
                System.out.printf("  %-15s : %s%n", "2 - Telephone", user.getTelephone());
                System.out.printf("  %-15s : %s%n", "3 - Email", user.getEmail());
                System.out.printf("  %-15s : %s%n", "4 - Role", user.getRole().name());
                System.out.printf("  %-15s%n", "0 - Exit");
                System.out.println("╚══════════════════════════════════════════════════════════╝");
                int Choice = scanner.nextInt();
                switch (Choice){
                    case 1 :
                        System.out.println("Old Name : "+user.getLastName() + " " + user.getFirstName() + "\n");
                        System.out.print("➤ New First Name : ");
                        String NewFName = scanner.next();
                        if(Objects.equals(NewFName, "0")){
                            pincipaleMenu();
                        }
                        System.out.print("➤ New Last Name : ");
                        String NewLName = scanner.next();
                        if(Objects.equals(NewLName, "0")){
                            pincipaleMenu();
                        }
                        user.setFirstName(NewFName);
                        user.setLastName(NewLName);
                        UserService.updateInformation(user);
                    case 0 :
                        pincipaleMenu();
                }
            }
            pincipaleMenu();
        }else{
            System.out.println("╔═══════════════════════════════════════════════════════╗");
            System.out.println("   ✘ User Not Found            ");
            System.out.println("   🔄 No account associated with this informations.           ");
            System.out.println("╚═══════════════════════════════════════════════════════╝");
            pincipaleMenu();
        }
    }


    public void listAccounts() throws SQLException {
        try{
            List<User> users = this.UserService.listUsers();
            System.out.print("Enter The User Key : ");
            int userkey = scanner.nextInt();

            ArrayList<Account> Accounts = AccountService.getAccountByUserId(users.get(userkey - 1).getId().toString());
            Optional<Account> optionalAccount = Accounts.stream().findFirst();

            if(Accounts.isEmpty()){
                System.out.println("The user not have any account !!");
                pincipaleMenu();
            }

            Account firstAccount = optionalAccount.get();
            int colId = 36;
            int colSolde = 10;
            int colSalaire = 16;
            int colType = 8;
            int colStatus = 24;


            System.out.println("Account Owner : "+firstAccount.getClient().getFirstName() +" "+firstAccount.getClient().getLastName());
            System.out.println("╔════════════════════════════════════════════════════════════════════════════════════════════════════════════╗");
             System.out.printf("║ %-36s │ %10s │ %16s │ %-8s │ %-24s ║%n",
                                "Account ID", "Solde", "Client Salaire", "Type", "Account Status");
            System.out.println("╠════════════════════════════════════════════════════════════════════════════════════════════════════════════╣");

            for (Account acc : Accounts) {
                System.out.printf("║ %-" + colId + "s | %" + colSolde + "d | %" + colSalaire + "d | %-"
                                + colType + "s | %-"+ colStatus +"s ║%n",
                        acc.getId(),
                        acc.getSolde().intValue(),         // assuming BigDecimal
                        acc.getClient().getSalaire().intValue(),
                        acc.getType(),
                        acc.getActive() ? "Actived" : "Disactivated");
            }

            System.out.println("╚════════════════════════════════════════════════════════════════════════════════════════════════════════════╝");
        } catch (IllegalArgumentException e) {
            System.err.println(e.getMessage());
        }
    }

}

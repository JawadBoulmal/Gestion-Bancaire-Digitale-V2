package view.ChoicesView;

import Database.Database;
import enums.AccountType;
import enums.CreditType;
import interfaces.UserRepository;
import interfaces.View;
import modules.Account;
import modules.Credit;
import repositories.AccountRepositoryImp;
import repositories.CreditRepositoryImp;
import repositories.UserRepositoryImp;
import services.AccountService;
import services.CreditService;
import services.UserService;
import view.Acceuil;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.UUID;

import static Utils.ConsoleColors.*;
import static Utils.ConsoleColors.YELLOW;
import static Utils.ConsoleColors.colorizeCell;

public class CreditView implements View {
    private AccountRepositoryImp accountRepo;
    private AccountService accountService;
    private UserRepository userRepository;
    private UserService userService;
    private Scanner scanner = new Scanner(System.in);
    private CreditService creditService;
    Connection conn;

    public CreditView(){
        this.conn = Database.getInstance().getConnection();
        CreditRepositoryImp CreditRepo = new CreditRepositoryImp(conn);
        this.creditService = new CreditService(CreditRepo);

        accountRepo = new AccountRepositoryImp(conn);
        accountService = new AccountService(accountRepo,userRepository);
        userRepository = new UserRepositoryImp(conn);
        userService = new UserService(userRepository);
    }

    @Override
    public void pincipaleMenu() throws SQLException {
        System.out.println("===================================================");
        System.out.println("  1️⃣    Demander");
        System.out.println("  3️⃣    Lister les credits");
        System.out.println("  0️⃣    Back To Menu");
        System.out.println("===========================================");
        System.out.print("➤ Choose an option: ");

        int choice = scanner.nextInt();

        switch (choice){
            case 1 :
                this.createCredit();
                pincipaleMenu();
                break;
            case 0:
                new Acceuil();
                pincipaleMenu();
                break;
            default:
                System.err.println("\nINVALID OPTION\n");
                pincipaleMenu();
                break;
        }
    }


    private boolean createCredit() throws SQLException {
        System.out.print("Enter the user id : ");
        String userID = scanner.next();
        Optional<Account> creditAccountOpt = this.creditService.getCreditAccountByUserId(UUID.fromString(userID));
        if(creditAccountOpt.isPresent()){
            Account account = this.accountService.getAccountByUserId(userID).stream().filter(accountU -> accountU.getType() == AccountType.CREDIT).findFirst().orElse(null);
            System.out.print("Enter the amount : ");
            BigDecimal amount = scanner.nextBigDecimal();

            System.out.print("Enter the taux : ");
            float Taux = scanner.nextFloat();

            System.out.print("Enter the justification : ");
            String justification = scanner.next();
            scanner.nextLine();
            System.out.print("Enter the duration of the credit (Mois) : ");
            int duree = scanner.nextInt();


            CreditType type = showCreditTypes();

            Credit result = this.creditService.createCredit(
                    amount,
                    duree,
                    Taux,
                    justification,
                    type,
                    account
            );
            if(result != null){
                System.out.println("1 . Confirm");
                System.out.println("2 . Cancel");
                System.out.print("-> ");
                int Choice = scanner.nextInt();
                if(Choice == 1){
                    this.creditService.requestCredit(result);
                    System.out.println("The credit has been created successfully !!");
                    List<Credit> ListCredits = this.creditService.getCreditsByUserID(account.getClient().getId());
                    System.out.println(colorizeCell("List Of All Credits For "+ListCredits.get(0).getAccount().getClient().getLastName() +" "+ListCredits.get(0).getAccount().getClient().getFirstName() ,GREEN,1));

                    int index = 1;
                    String format = "| %-5s | %-10s | %-10s | %-10s | %-10s | %-15s | %-15s |\n";
                    System.out.printf(format,
                            colorizeCell("Key",YELLOW,5),
                            colorizeCell("Amount",YELLOW,10),
                            colorizeCell("Status",YELLOW,10),
                            colorizeCell("Duree",YELLOW,10),
                            colorizeCell("Type",YELLOW,10),
                            colorizeCell("Each Month",YELLOW,15),
                            colorizeCell("Created",YELLOW,15)
                    );
                    for (Credit creditEle : ListCredits){
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

                        System.out.printf(format,
                                index,
                                creditEle.getAmount(),
                                creditEle.getStatus(),
                                creditEle.getDuree() + " Mois",
                                creditEle.getCreditType(),
                                creditEle.getAmountEach() == null ? BigDecimal.ZERO : creditEle.getAmountEach(),
                                creditEle.getCreatedAt().format(formatter)
                        );
                        index++;
                    }
                    return true;
                }
            }else{
                System.out.println("Failed to create the credit !!");
            }
        }else{
            System.out.println("The user doesnt have credit account create One before request credit !!");
            pincipaleMenu();
            return false;
        }
        return false;
    }

    private CreditType showCreditTypes() throws SQLException {
        scanner.nextLine();
        System.out.println("Choose credit type:");
        System.out.println("1 . Simple");
        System.out.println("2 . Composée");
        System.out.println("0 . back to menu");
        System.out.print("->");
        int type = scanner.nextInt();

        switch (type){
            case 1:
                return CreditType.SIMPLE;
            case 2:
                return CreditType.COMPOSEE;
            case 0:
                pincipaleMenu();
            default:
                System.err.println("\nINVALID OPTION\n");
                return showCreditTypes();
        }
    }
}

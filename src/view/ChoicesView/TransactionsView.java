package view.ChoicesView;

import Database.Database;
import Utils.ConsoleColors;
import enums.Roles;
import enums.TransactionsType;
import enums.VirementStatus;
import interfaces.TransactionRepository;
import interfaces.UserRepository;
import interfaces.View;
import modules.Account;
import modules.Fee_rule;
import modules.User;
import repositories.AccountRepositoryImp;
import repositories.TransactionRepositoryImp;
import repositories.UserRepositoryImp;
import services.AccountService;
import services.TransactionService;
import services.UserService;
import view.Acceuil;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;

import static Utils.ConsoleColors.colorizeCell;

public class TransactionsView implements View {
    private Scanner scanner = new Scanner(System.in);
    private TransactionService transactionService;
    private TransactionRepositoryImp transactionRepo;
    private AccountRepositoryImp accountRepo;
    private AccountService accountService;
    private UserRepository userRepository;
    private UserService userService;

    private Connection conn;
    public TransactionsView(){
        conn = Database.getInstance().getConnection();
        transactionRepo = new TransactionRepositoryImp(conn);
        accountRepo = new AccountRepositoryImp(conn);
        accountService = new AccountService(accountRepo,userRepository);

        userRepository = new UserRepositoryImp(conn);
        userService = new UserService(userRepository);

        transactionService = new TransactionService(transactionRepo,accountRepo);

    }

    @Override
    public void pincipaleMenu() throws SQLException {
        System.out.println("===================================================");
        System.out.println("             Comptes - Options");
        System.out.println("===================================================");
        System.out.println("  1️⃣   Deposit");
        System.out.println("  2️⃣   Withdraw");
        System.out.println("  3️⃣   Transfer");
        System.out.println("  0️⃣   Back to Main Menu");
        System.out.println("===================================================");
        System.out.print("➤ Choose an option: ");
        int choice = scanner.nextInt();
        switch (choice){
            case 0 :
                new Acceuil();
                break;
            case 1 :
                ShowDeposit();
                break;
            case 2 :
                ShowWithdraw();
                break;
            case 3 :
                ShowTransfer();
                break;


            default:
                System.err.println("\nINVALID OPTION\n");
                pincipaleMenu();
                break;
        }
    }

    public void listUserAccounts(List<Account> accountUser){
        this.accountService.listUserAccounts(accountUser);
    }



    public void ShowTransfertIN() throws SQLException {
        try{
            System.out.println("You want to send money between the same user accounts !");

            List<User> users = this.userService.listUsers();

            System.out.print("Enter the number :");
            int numberQueue = scanner.nextInt();
            int index = numberQueue - 1;
            User user = users.get(index);

            if(user == null){
                System.out.println("======================================================");
                System.out.println(" No user with this id . ");
                System.out.println("======================================================");
                return ;
            }

            if(user.getRole() != Roles.CLIENT){
                System.out.println("======================================================");
                System.out.println(" Transfer Failed !! You sould Transfert to a client . ");
                System.out.println("======================================================");
                return ;
            }
            ArrayList<Account> accountUser = this.accountService.getAccountByUserId(user.getId().toString());
            if(accountUser.isEmpty()){
                System.out.println("======================================================");
                System.out.println("          "+user.getLastName()+" doesn't have any account yet ! ");
                System.out.println("======================================================");
                return ;
            }
            System.out.println("Accounts For "+user.getLastName()+" "+user.getFirstName());

            listUserAccounts(accountUser);

            System.out.print("Enter the account number (Sender) :");
            int senderAccountID = scanner.nextInt();
            if(Objects.equals(senderAccountID, "0")){
                ShowTransfertIN();
                return;
            }

            System.out.print("Enter the account id that will receive money from (Receiver) :");
            int receiverAccountID = scanner.nextInt();
            if(Objects.equals(receiverAccountID, "0")){
                ShowTransfertIN();
                return;
            }

            System.out.print("Enter the amount :");
            BigDecimal amount = scanner.nextBigDecimal();
            if(amount.compareTo(new BigDecimal(0)) == 0){
                ShowTransfertIN();
                return;
            }

            UUID result = transactionService.AddTransaction(amount,UUID.fromString(accountUser.get(senderAccountID-1).getId()),UUID.fromString(accountUser.get(receiverAccountID-1).getId()), TransactionsType.TRANSFERIN,VirementStatus.SEETLED);
            if (result != null){
                System.out.println("╔══════════════════════════════════════╗");
                System.out.println("   💰 Transfer Completed Successfully   ");
                System.out.println("   😉 See you again soon!               ");
                System.out.println("╚══════════════════════════════════════╝");
                pincipaleMenu();
            }else{
                System.out.println("==============================================");
                System.out.println(" Transfer Failed !! Try Again later . ");
                System.out.println("==============================================");
                pincipaleMenu();
            }
        } catch (NumberFormatException e) {
            System.out.println("╔══════════════════════════════════════════╗");
            System.out.println("   ✘ Transfer Amount Value is Not Valid!     ");
            System.out.println("   🔄 Please try again later.               ");
            System.out.println("╚══════════════════════════════════════════╝");
        } catch (InputMismatchException e) {
            System.out.println("There is a problem , try again !");
            ShowDeposit();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (IndexOutOfBoundsException e){
            System.out.println("Choose the correct user !");
            ShowTransfertIN();
        }



    }
    public void ShowTransfertOUT(){

    }
    public void ShowTransfer(){
        try{
            System.out.println("0 : Menu ");
            System.out.println("  1️⃣   Transfert IN");
            System.out.println("  2️⃣   Transfert OUT");
            System.out.print("➤ Choose an option: ");
            int choice = scanner.nextInt();
            switch(choice){
                case 1:
                    ShowTransfertIN();
                    pincipaleMenu();
                    break;
                case 2:
                    ShowTransfertOUT();
                    pincipaleMenu();
                    break;
                default:
                    System.out.println("Invalid choice !");
                    ShowTransfer();
            }



        } catch (NumberFormatException e) {
            System.out.println("There is a problem , try again !");
        } catch (InputMismatchException e) {
            System.out.println("There is a problem , try again !");
            ShowDeposit();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public void ShowWithdraw()  {
        try{
            System.out.println("Withdraw Money to account user !");
            List<User> users = this.userService.listUsers();

            System.out.println("0 : Menu ");
            System.out.print("Enter The User key : ");
            int userKey = scanner.nextInt();

            String userID = users.get(userKey-1).getId().toString();
            List<Account> userAccounts = accountService.getAccountByUserId(userID);
            this.accountService.listUserAccounts(userAccounts);

            System.out.print("Enter The Account Key : ");
            int accountKey = scanner.nextInt();

            UUID TransfertIN = UUID.fromString(userAccounts.get(accountKey-1).getId());

            System.out.print("Amount That you want to Withdraw : ");
            BigDecimal Amount = scanner.nextBigDecimal();

            UUID result = transactionService.AddTransaction(Amount,TransfertIN,TransfertIN, TransactionsType.WITHDRAW,VirementStatus.SEETLED);
            if (result != null){
                System.out.println("╔══════════════════════════════════════╗");
                System.out.println("   💰 Withdraw Completed Successfully   ");
                System.out.println("   😉 See you again soon!               ");
                System.out.println("╚══════════════════════════════════════╝");
                pincipaleMenu();
            }else{
                System.out.println("==============================================");
                System.out.println(" Withdraw Failed !! Try Again later . ");
                System.out.println("==============================================");
                pincipaleMenu();
            }
        }catch (NumberFormatException e) {
            System.out.println("╔══════════════════════════════════════════╗");
            System.out.println("   ✘ Withdraw Amount Value is Not Valid!     ");
            System.out.println("   🔄 Please try again later.               ");
            System.out.println("╚══════════════════════════════════════════╝");
        } catch (InputMismatchException e) {
            System.out.println("There is a problem , try again !");
            ShowWithdraw();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void ShowDeposit(){
        try{
            Scanner input = new Scanner(System.in);
            System.out.println("Deposit Money to account user !");
            List<User> users = this.userService.listUsers();

            System.out.println("0 : Menu ");
            System.out.print("Enter The User Key : ");
            int userKey = input.nextInt();

            String userID = users.get(userKey-1).getId().toString();
            List<Account> userAccounts = accountService.getAccountByUserId(userID);

            this.accountService.listUserAccounts(userAccounts);

            System.out.print("Enter The Account Key : ");
            int accountKey = input.nextInt();

            System.out.print("Amount That you want to deposit  : ");
            BigDecimal Amount = input.nextBigDecimal();

            UUID TransfertIN = UUID.fromString(userAccounts.get(accountKey-1).getId());
            UUID result = transactionService.AddTransaction(Amount,TransfertIN,TransfertIN, TransactionsType.DEPOSIT,VirementStatus.SEETLED);
            if (result != null){
                System.out.println("==============================================");
                System.out.println(" 🎉 ✔ The Deposit Operation was Successful! 🎉 ");
                System.out.println("==============================================");
                pincipaleMenu();
            }else{
                System.out.println("==============================================");
                System.out.println(" Deposit Failed !! Try Again later . ");
                System.out.println("==============================================");
                ShowDeposit();
            }
        } catch (NumberFormatException e) {
            System.out.println("╔══════════════════════════════════════════╗");
            System.out.println("   ✘ Deposit Amount Value is Not Valid!     ");
            System.out.println("   🔄 Please try again later.               ");
            System.out.println("╚══════════════════════════════════════════╝");
        } catch (InputMismatchException e) {
            System.out.println("There is a problem , try again !");
            ShowDeposit();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


}

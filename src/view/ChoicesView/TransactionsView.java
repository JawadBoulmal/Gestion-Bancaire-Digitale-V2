package view.ChoicesView;

import Database.Database;
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
import services.TransactionService;
import services.UserService;
import view.Acceuil;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.UUID;

public class TransactionsView implements View {
    private Scanner scanner = new Scanner(System.in);
    private TransactionService transactionService;
    private TransactionRepositoryImp transactionRepo;
    private AccountRepositoryImp accountRepo;

    private Connection conn;
    public TransactionsView(){
        conn = Database.getInstance().getConnection();
        transactionRepo = new TransactionRepositoryImp(conn);
        accountRepo = new AccountRepositoryImp(conn);

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



    public void ShowTransfer(){
        try{





        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public void ShowWithdraw()  {
        try{
            System.out.println("0 : Menu ");
            System.out.print("Enter The Account ID : ");
            String AccountID = scanner.next();
            UUID TransfertIN = UUID.fromString(AccountID);

            System.out.print("Amount That you want to Withdraw : ");
            BigDecimal Amount = scanner.nextBigDecimal();

            UUID result = transactionService.AddTransaction(Amount,TransfertIN,TransfertIN, TransactionsType.WITHDRAW);
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
            ShowDeposit();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void ShowDeposit(){
        try{
            Scanner input = new Scanner(System.in);

            System.out.println("0 : Menu ");
            System.out.print("Enter The User Account Number : ");
            String AccountID = input.nextLine();

            System.out.print("Amount That you want to deposit  : ");
            BigDecimal Amount = input.nextBigDecimal();
            UUID TransfertIN = UUID.fromString(AccountID);
            UUID result = transactionService.AddTransaction(Amount,TransfertIN,TransfertIN, TransactionsType.DEPOSIT);
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

package view.ChoicesView;

import Database.Database;
import interfaces.View;
import repositories.TransactionRepositoryImp;
import services.RapportService;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class RapportView implements View {
    private Scanner scanner = new Scanner(System.in);
    private RapportService rapportService;
    private TransactionRepositoryImp transactionRepo;
    private Connection conn;
    public RapportView(){
        conn = Database.getInstance().getConnection();
        transactionRepo = new TransactionRepositoryImp(conn);
        rapportService = new RapportService(transactionRepo);
    }

    @Override
    public void pincipaleMenu() throws SQLException {
        int choice;
        do {
            System.out.println("\n===== RAPPORTS (AUDITOR) =====");
            System.out.println("1. View Transactions");
            System.out.println("2. View Logs");
            System.out.println("0. Exit");
            System.out.print("➤ Enter your choice: ");
            choice = scanner.nextInt();

            switch (choice) {
                case 1 -> viewTransactions();
                case 2 -> rapportService.getAllTransactions();
                case 0 -> {
                    System.out.println("Exiting Rapport View...");
                }
                default -> System.out.println("Invalid choice. Try again.");
            }
        } while (choice != 0);
    }
    private void viewTransactions() throws SQLException {

    }
}

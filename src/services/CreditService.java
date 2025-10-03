package services;

import Database.Database;
import enums.*;
import modules.Account;
import modules.BankFee;
import modules.Credit;
import modules.Transaction;
import repositories.*;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static Utils.ConsoleColors.*;
import static Utils.ConsoleColors.colorizeCell;

public class CreditService {

    private Connection conn;
    private CreditRepositoryImp creditRepository;
    private AccountRepositoryImp accountRepo;
    private AccountService accountService;

    private BankFeeService bankFeeService;
    private BankFeeRepositoryImp bankFeeRepositoryImp;

    private TransactionService TransactionService;
    private TransactionRepositoryImp TransactionRepositoryImp;
    private AccountRepositoryImp AccountRepositoryImp;

    public CreditService(CreditRepositoryImp repo){
        this.creditRepository = repo;
        this.conn = Database.getInstance().getConnection();
        UserRepositoryImp userRepository = new UserRepositoryImp(conn);
        this.accountRepo = new AccountRepositoryImp(conn);
        this.accountService = new AccountService(accountRepo,userRepository);

        this.bankFeeRepositoryImp = new BankFeeRepositoryImp(this.conn);
        this.bankFeeService = new BankFeeService(this.bankFeeRepositoryImp);

        this.TransactionRepositoryImp = new TransactionRepositoryImp(this.conn);
        this.AccountRepositoryImp = new AccountRepositoryImp(this.conn);
        this.TransactionService = new TransactionService(this.TransactionRepositoryImp,this.AccountRepositoryImp);
    }

    public boolean requestCredit(Credit credit){
        return this.creditRepository.demander(credit);
    }

    public Optional<Account> getCreditAccountByUserId(UUID userId) {
        return this.accountService.getAccountByUserId(userId.toString())
                .stream()
                .filter(account -> account.getType() == AccountType.CREDIT)
                .findFirst();
    }

    public List<Credit> getCreditsByUserID(UUID userID){
        return this.creditRepository.getCreditsByUserID(userID);
    }

    public Credit createCredit(BigDecimal amount , int duree ,float taux , String justification , CreditType type,Account account) throws SQLException {

        int duration = duree;
        BigDecimal ammount = amount;
        BigDecimal realTaux = new BigDecimal(taux).multiply(new BigDecimal("0.01"));
        BigDecimal amountPercentTotal = ammount.multiply(realTaux).add(ammount) ;
        BigDecimal ForMonth = amountPercentTotal.divide(new BigDecimal(duration));
        BigDecimal Salaire40per = account.getClient().getSalaire().multiply(new BigDecimal("0.4"));
        BigDecimal BankFeeRev = ammount.multiply(realTaux);


        List<Credit> ListCredits = getCreditsByUserID(account.getClient().getId());
        BigDecimal totalAmountEach = BigDecimal.ZERO;
        for(Credit creditEle : ListCredits){
            totalAmountEach = totalAmountEach.add(
                    creditEle.getAmountEach() == null ? BigDecimal.ZERO : creditEle.getAmountEach()
            );
        }
        Salaire40per = Salaire40per.subtract(totalAmountEach);

        boolean isAble = Salaire40per.compareTo(ForMonth) >= 0;

        Credit credit = new Credit(
                UUID.randomUUID(),
                amount,
                duree,
                taux,
                null,
                justification,
                type,
                account,
                CreditStatus.ACTIVE,
                ForMonth,
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        if(!ListCredits.isEmpty()) {
            System.out.println(colorizeCell("*************************************", YELLOW, 1));
            System.out.println(colorizeCell("This user has already take " + ListCredits.size() + " Credits", GREEN, 1));
            System.out.println(colorizeCell("*************************************", YELLOW, 1));
            int index = 1;
            System.out.println(colorizeCell("List Of All Credits For " + ListCredits.get(0).getAccount().getClient().getLastName() + " " + ListCredits.get(0).getAccount().getClient().getFirstName(), GREEN, 1));

            String format = "| %-5s | %-10s | %-10s | %-10s | %-10s | %-15s | %-15s |\n";
            System.out.printf(format,
                    colorizeCell("Key", YELLOW, 5),
                    colorizeCell("Amount", YELLOW, 10),
                    colorizeCell("Status", YELLOW, 10),
                    colorizeCell("Duree", YELLOW, 10),
                    colorizeCell("Type", YELLOW, 10),
                    colorizeCell("Each Month", YELLOW, 15),
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
            System.out.println("THIS IS THE TOTAL OF ALL THE CREDITS PAY : " + totalAmountEach);
        }


        System.out.println("THE AMOUNT PERCENT : "+amountPercentTotal);
        System.out.println("THE AMOUNT FOR EACH MONTH : "+ForMonth);
        System.out.println("THE SALAIRE IS : "+account.getClient().getSalaire());
        System.out.println("40% OF THE CLIENT SALAIRE IS : "+Salaire40per);
        System.out.println(colorizeCell("RESULT : ",RED,1)+(isAble ? "He is able to take this credit ." : "this client not able to take this credit !!" ));

        if(isAble){
            return credit;
        }
        return null;
    }
}

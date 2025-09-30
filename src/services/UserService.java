package services;

import Utils.ConsoleColors;
import enums.Roles;
import interfaces.UserRepository;
import modules.*;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.*;

import static Utils.ConsoleColors.colorizeCell;

public class UserService {
    private UserRepository userRepository;

    public UserService(UserRepository repo){
        userRepository = repo;
    }
    public boolean CreateClient(String firstName , String lastName, String telephone, String CIN, BigDecimal Salaire,String email,String password) throws SQLException {
        UUID userId = UUID.randomUUID();
        User user = new Client(userId,firstName,lastName,telephone,CIN,Salaire,email,password);

        UUID UserID = userRepository.save(user, user.getRole());
        if(UserID != null){
            System.out.println("User Has been Created Successfully : \n\tUser ID : "+UserID);
            return true;
        }
        return false;
    }

    public boolean CreateOthers(String firstName , String lastName, String telephone,String CIN, String email,String password , Roles role) throws SQLException {
        UUID userId = UUID.randomUUID();
        User user;
        switch (role){
            case MANAGER -> {
                user = new Manager(userId,firstName,lastName,telephone,CIN,email,password);
            }
            case AUDITOR -> {
                user = new Auditor(userId,firstName,lastName,telephone,CIN,email,password);
            }
            case ADMIN -> {
                user = new Admin(userId,firstName,lastName,telephone,CIN,email,password);
            }
            case TELLER -> {
                user = new Teller(userId,firstName,lastName,telephone,CIN,email,password);
            }
            default -> {
                user = null;
            }
        }
        UUID UserID = userRepository.save(user, role);
        if(UserID != null){
            System.out.println("User Has been Created Successfully : \n\tUser ID : "+UserID);
            return true;
        }
        return false;
    }
    public boolean CheckEmailUserIfExists(String Email) throws SQLException {
        return userRepository.emailExist(Email);
    }

    public ArrayList<User> getAll(){
        return userRepository.getAll();

    }

    public User getUserById(String email, Optional<UUID> id){
        return userRepository.getById(email,id);
    }

    public User updateInformation(User user){
        User result = userRepository.update(user);
        if(result != null){
            System.out.println("User Has been Updated Successfully : \n\tUser ID : "+result.getId());
            return result;
        }
        System.out.println("Failed update the user !");
        return null;
    }


    public List<User> listUsers() {
        List<User> users = this.getAll();

        if (users.isEmpty()) {
            System.out.println("⚠️ No users found.");
            return null;
        }

        String format = "| %-5s | %-36s | %-20s | %-30s | %-10s | %-15s |%n";

        System.out.printf(
                format,
                colorizeCell("Key", ConsoleColors.YELLOW, 5),
                colorizeCell("USER ID", ConsoleColors.YELLOW, 36),
                colorizeCell("FULL NAME", ConsoleColors.YELLOW, 20),
                colorizeCell("EMAIL", ConsoleColors.YELLOW, 30),
                colorizeCell("ROLE", ConsoleColors.YELLOW, 10),
                colorizeCell("TOTAL ACCOUNTS", ConsoleColors.YELLOW, 10)
        );

        int index = 1;
        for (User user : users) {
            String fullName = user.getFirstName() + " " + user.getLastName();
            if(user.getRole().equals(Roles.CLIENT)){
                if (user.getPassword().equals("Not")){
                    System.out.printf(
                            format,
                            colorizeCell(String.valueOf(index), ConsoleColors.YELLOW, 5),
                            user.getId(),
                            fullName,
                            user.getEmail(),
                            user.getRole().name(),
                            0
                    );
                    index++;
                }else{
                    System.out.printf(format,
                            index,
                            user.getId(),
                            fullName,
                            user.getEmail(),
                            user.getRole().name(),
                            user.getPassword()
                    );
                    index++;
                }
            }
        }

        return users;
    }


}

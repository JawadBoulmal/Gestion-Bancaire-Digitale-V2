package services;

import enums.Roles;
import interfaces.UserRepository;
import modules.User;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AuthService {
    private final UserRepository userRepository;
    private UserService userService;
    public static boolean isOnline;

    public AuthService(UserRepository repo){
        userRepository = repo;
        userService = new UserService(this.userRepository);
    }

    public boolean Register(String firstName , String lastName, String telephone, String CIN, BigDecimal Salaire, String email, String password , Roles role) throws SQLException {
        boolean User ;

        while(!validationEmail(email)){
            System.out.println("The Email Not Valid !");
            return false;
        }


        if(role == Roles.CLIENT){
            User = this.userService.CreateClient( firstName ,  lastName,  telephone,  CIN,  Salaire, email, password);
        }else {
            User = this.userService.CreateOthers( firstName ,  lastName,  telephone,  CIN, email, password);
        }
        if(User){
            return true;
        }else{
            return false;
        }
    }

    public boolean Login(String email , String password) throws SQLException {
        User UserID = userRepository.login(email,password);
        if(UserID != null){
            System.out.print("YOU ARE LOGGED SUCCESSFULLY !");
            isOnline = true;
            return true;
        }
        System.out.print("FAILED TO LOG IN , CREDENTIALS INVALID !");
        return false;
    }

    public boolean validationEmail(String Email) throws SQLException {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(Email);
        boolean fullMatchEmail = matcher.matches();
        if(fullMatchEmail && !this.userService.CheckEmailUserIfExists(Email)){
            return true;
        }
        return false;
    }
    public boolean validationPassword(String password){
        int min = 6;
        if(password.length() < min){
            return false;
        }
        return true;
    }
}

package Utils;

import java.util.UUID;

import static Utils.ConsoleColors.*;

public class Utils {
    public static boolean isValidUUID(String input) {
        if (input == null) return false;

        try {
            UUID.fromString(input);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    public static String maskEmail(String email) {
        if (email == null || !email.contains("@")) {
            return email;
        }

        String[] parts = email.split("@", 2);
        String username = parts[0];
        String domain = parts[1];

        if (username.length() <= 6) {
            return username + "@" + domain;
        }

        String masked = username.substring(0, 6) + "..." + "@" + domain;
        return masked;
    }

    public static void backToBlack(String input,String msg){
        if(input.equals("99")){
            showErrorSuccess(msg,true);

        }
    }

    public static void showErrorSuccess(String msg, boolean status){
        if(status){
            System.out.println(colorizeCell(msg,GREEN,1));
        }else{
            System.out.println(colorizeCell(msg,RED,1));
        }
    }
}

import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter password: ");
        String password = sc.nextLine();
        try {
            checkStrength(password);
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    static void checkStrength(String password) {
         
        if (password == null || password.trim().isEmpty()) {
    throw new IllegalArgumentException("Password cannot be empty or blank!");
    }
        if (password.length() > 16) {
    throw new IllegalArgumentException("Password too long! Maximum 16 characters allowed.");
   }  

        int score = 0;
        boolean hasUpper = false;
        boolean hasLower = false;
        boolean hasDigit = false;
        boolean hasSpecial = false;

        if (password.length() >= 8) score += 2;
        
        

        for (char c : password.toCharArray()) {
            if (Character.isUpperCase(c)) hasUpper = true;
            else if (Character.isLowerCase(c)) hasLower = true;
            else if (Character.isDigit(c)) hasDigit = true;
            else hasSpecial = true;
        }

        if (hasUpper) score += 1;
        if (hasLower) score += 1;
        if (hasDigit) score += 1;
        if (hasSpecial) score += 2;

        String[] common = {"password", "123456", "qwerty", "admin", "hello"};
        for (String word : common) {
            if (password.toLowerCase().contains(word)) {
                score -= 3;
                break;
            }
        }

        System.out.println("Score: " + score);
        if (score <= 3)
            System.out.println("Weak Password");
        else if (score <= 6)
            System.out.println("Medium Password");
        else if (score <= 9)
            System.out.println("Strong Password");
        else
            System.out.println("Very Strong Password");
    }
}

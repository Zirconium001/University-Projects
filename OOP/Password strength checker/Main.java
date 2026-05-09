import java.util.*;

public class Main {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter password: ");
        String password = sc.nextLine();

        checkStrength(password);
    }

    static void checkStrength(String password) {
        int score = 0;

        boolean hasUpper = false;
        boolean hasLower = false;
        boolean hasDigit = false;
        boolean hasSpecial = false;

        // Length scoring
        if (password.length() >= 8) score += 2;
        if (password.length() >= 12) score += 2;
        if (password.length() >= 16) score += 2;

        // Character checks
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

        // Common password penalty
        String[] common = {"password", "123456", "qwerty", "admin"};
        for (String word : common) {
            if (password.toLowerCase().contains(word)) {
                score -= 3;
                break;
            }
        }

        // Strength result
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

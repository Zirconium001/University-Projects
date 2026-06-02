package service;

public class BankingService {

    public static void verifyATM(int pin)
            throws SecurityException {

        int correctPin = 1234;

        if (pin != correctPin) {
            throw new SecurityException(
                    "Incorrect PIN.");
        }
    }

    public static double calculateInterest(
            double amount,
            double divisor) {

        return amount / divisor;
    }
}

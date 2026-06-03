package exceptions;

public class BankingExceptions {

    private BankingExceptions() {}

    public static class AccountNotFoundException extends Exception {
        public AccountNotFoundException(String message) {
        super(message);
        }
    }

    public static class InsufficientBalanceException extends Exception {
        public InsufficientBalanceException(String message) {
        super(message);
        }
    }

    public static class InvalidAmountException extends Exception {
        public InvalidAmountException(String message) {
        super(message);
        }
    }
}

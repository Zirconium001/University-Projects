import java.util.ArrayList;
import java.util.Scanner;

import model.BankAccount;
import exceptions.BankingExceptions.*;

public class SmartBankingSystem {

    private static ArrayList<BankAccount> accounts =
            new ArrayList<>();
    private static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {

        accounts.add(new BankAccount(101, "Tanjiro", 10000));
        accounts.add(new BankAccount(102, "Nezuko", 5000));

        int choice;
        do {
            System.out.println(
                    "\n========== SMART BANKING SYSTEM ==========");
            System.out.println("1.  Open Account");
            System.out.println("2.  Deposit");
            System.out.println("3.  Withdraw");
            System.out.println("4.  Transfer Money");
            System.out.println("5.  Check Balance");
            System.out.println("6.  View Transaction History");
            System.out.println("7.  Close Account");
            System.out.println("8.  Change PIN");
            System.out.println("0.  Exit");
            System.out.println("========================================");
            System.out.print("Enter choice: ");
            choice = sc.nextInt();
            sc.nextLine();

            try {
                switch (choice) {
                    case 1:
                        openAccount();
                        break;
                    case 2:
                        System.out.print("Enter account number: ");
                        BankAccount depAcc = findAccount(sc.nextInt());
                        System.out.print("Enter amount to deposit: ");

                        double depAmt = sc.nextDouble();
                        depAcc.deposit(depAmt);
                        System.out.println("Deposit successful. "+ depAcc);
                        break;

                    case 3:
                        System.out.print("Enter account number: ");
                        BankAccount wdAcc = findAccount(sc.nextInt());
                        verifyPin(wdAcc);
                        System.out.print("Enter amount to withdraw: ");

                        double wdAmt = sc.nextDouble();
                        wdAcc.withdraw(wdAmt);
                        System.out.println("Withdrawal successful. "+ wdAcc);
                        break;

                    case 4:
                        System.out.print("Enter your account number: ");
                        BankAccount srcAcc = findAccount(sc.nextInt());
                        verifyPin(srcAcc);
                        System.out.print("Enter receiver account number: ");
                        BankAccount recAcc = findAccount(sc.nextInt());
                        System.out.print("Enter amount to transfer: ");

                        double trAmt = sc.nextDouble();
                        srcAcc.transferMoney(recAcc, trAmt);
                        System.out.println( "Transfer successful.");
                        break;

                    case 5:
                        System.out.print("Enter account number: ");

                        BankAccount balAcc = findAccount(sc.nextInt());
                        balAcc.checkBalance();
                        break;

                    case 6:
                        System.out.print("Enter account number: ");
                        BankAccount txAcc = findAccount(sc.nextInt());
                        System.out.println("\n--- Transaction History for Account "+ txAcc.getAccountNumber()+ " ---");
                        if (txAcc.getTransactions().isEmpty()) {
                            System.out.println("No transactions yet.");
                        } else {
                            for (String t : txAcc.getTransactions()) {
                                System.out.println(t);
                            }
                        }
                        System.out.println("-------------------------------------");
                        break;

                    case 7:
                        System.out.print("Enter account number to close: ");
                        BankAccount clAcc = findAccount(sc.nextInt());
                        verifyPin(clAcc);
                        System.out.print("Are you sure you want to close account "+ clAcc.getAccountNumber()+ "? (yes/no): ");
                        String confirm = sc.next();
                        if (confirm.equalsIgnoreCase("yes")) {
                            clAcc.closeAccount();
                            System.out.println("Account closed successfully.");
                        } else {
                            System.out.println("Close request cancelled.");
                        }
                        break;

                    case 8:
                        System.out.print("Enter account number: ");
                        BankAccount pinAcc = findAccount(sc.nextInt());
                        System.out.print("Enter current PIN: ");
                        int oldPin = sc.nextInt();
                        System.out.print("Enter new PIN: ");
                        int newPin = sc.nextInt();
                        pinAcc.changePin(oldPin, newPin);
                        System.out.println("PIN changed successfully.");
                        break;

                    case 0:
                        System.out.println("Thank you for using Smart Banking System. Goodbye!");
                        break;

                    default:
                        System.out.println("Invalid choice. Please try again.");
                }

            }
            catch (InvalidAmountException e) {
                System.out.println(e.getMessage());
            }
            catch (InsufficientBalanceException e) {
                System.out.println(e.getMessage());
            }
            catch (AccountNotFoundException e) {
                System.out.println(e.getMessage());
            }
            catch (SecurityException e) {
                System.out.println(e.getMessage());
            }
            catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
            finally {
                System.out.println("Returning to menu...");
            }

        } while (choice != 0);
        sc.close();
    }
    private static void openAccount()
            throws InvalidAmountException {

        System.out.print("Enter customer name: ");
        String name = sc.nextLine();
        System.out.println("Select account type:");
        System.out.println("1. Savings");
        System.out.println("2. Checking");
        System.out.println("3. Fixed Deposit");
        System.out.print("Enter choice: ");
        int typeChoice = sc.nextInt();
        sc.nextLine();

        String accountType;
        switch (typeChoice) {
            case 2:
                accountType = "Checking";
                break;
            case 3:
                accountType = "Fixed Deposit";
                break;
            default:
                accountType = "Savings";
        }
        double minDeposit = accountType.equals("Savings") ? 500
                : accountType.equals("Checking") ? 1000 : 5000;

        System.out.println("Minimum initial deposit for "+ accountType + ": " + minDeposit);
        System.out.print("Enter initial deposit amount: ");
        double initialDeposit = sc.nextDouble();
        sc.nextLine();

        if (initialDeposit < minDeposit) {
            throw new InvalidAmountException(accountType+ " requires minimum initial deposit of "+ minDeposit);
        }
        System.out.print("Set a 4-digit PIN: ");
        int pin = sc.nextInt();
        sc.nextLine();

        if (pin < 1000 || pin > 9999) {
            throw new InvalidAmountException("PIN must be a 4-digit number.");
        }
        BankAccount newAccount = new BankAccount(name, initialDeposit, accountType, pin);
        accounts.add(newAccount);

        System.out.println("\n=== Account Created Successfully! ===");
        System.out.println("Account Number: "+ newAccount.getAccountNumber());
        System.out.println("Customer Name:  "+ newAccount.getCustomerName());
        System.out.println("Account Type:   "+ newAccount.getAccountType());
        System.out.println("Balance:        "+ newAccount.getBalance());
        System.out.println("========================================");
    }
    private static BankAccount findAccount(int accountNumber)
            throws AccountNotFoundException {

        for (BankAccount acc : accounts) {
            if (acc.getAccountNumber() == accountNumber) {
                if (!acc.isActive()) {
                    throw new AccountNotFoundException("Account " + accountNumber+ " is closed.");
                }
                return acc;
            }
        }
        throw new AccountNotFoundException("Account not found: " + accountNumber);
    }
    private static void verifyPin(BankAccount account)
            throws SecurityException {

        int attempts = 0;

        while (attempts < 3) {
            System.out.print("Enter PIN for account "+ account.getAccountNumber()+ ": ");
            int enteredPin = sc.nextInt();

            if (account.verifyPin(enteredPin)) {
                return;
            }

            attempts++;
            System.out.println("Incorrect PIN. " + (3 - attempts)+ " attempts remaining.");
        }
        throw new SecurityException("Account "+ account.getAccountNumber() + " locked after 3 failed PIN attempts.");
    }
}

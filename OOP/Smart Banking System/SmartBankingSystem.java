import java.util.Scanner;

import model.BankAccount;
import service.BankingService;
import util.FileHandler;
import exceptions.*;

public class SmartBankingSystem {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        BankAccount acc1 =
                new BankAccount(
                        101,
                        "Tanjiro",
                        10000);

        BankAccount acc2 =
                new BankAccount(
                        102,
                        "Nezuko",
                        5000);

        int choice;

        do {

            System.out.println("\n===== SMART BANKING SYSTEM =====");
            System.out.println("1. Deposit");
            System.out.println("2. Withdraw");
            System.out.println("3. Transfer Money");
            System.out.println("4. Check Balance");
            System.out.println("5. ATM Verification");
            System.out.println("6. Interest Calculation");
            System.out.println("7. Read File");
            System.out.println("0. Exit");

            System.out.print("Enter choice: ");
            choice = sc.nextInt();

            try {

                switch (choice) {

                    case 1:

                        System.out.print(
                                "Enter amount: ");

                        double dep =
                                sc.nextDouble();

                        try {
                            acc1.deposit(dep);
                            System.out.println(
                                    "Deposit Successful");
                        }

                        catch (
                                InvalidAmountException e) {

                            System.out.println(
                                    e.getMessage());
                        }

                        break;

                    case 2:

                        System.out.print(
                                "Enter amount: ");

                        double wd =
                                sc.nextDouble();

                        acc1.withdraw(wd);

                        System.out.println(
                                "Withdraw Successful");

                        break;

                    case 3:

                        System.out.print(
                                "Enter amount: ");

                        double tr =
                                sc.nextDouble();

                        acc1.transferMoney(
                                acc2,
                                tr);

                        System.out.println(
                                "Transfer Successful");

                        break;

                    case 4:

                        acc1.checkBalance();
                        break;

                    case 5:

                        int attempts = 0;

                        while (attempts < 3) {

                            try {

                                System.out.print(
                                        "Enter PIN: ");

                                int pin =
                                        sc.nextInt();

                                BankingService
                                        .verifyATM(pin);

                                System.out.println(
                                        "PIN Verified");

                                break;
                            }

                            catch (
                                    SecurityException e) {

                                attempts++;

                                System.out.println(
                                        "Wrong PIN");

                                if (attempts == 3) {

                                    throw new SecurityException(
                                            "ATM Blocked!");
                                }
                            }
                        }

                        break;

                    case 6:

                        System.out.print(
                                "Enter amount: ");

                        double amount =
                                sc.nextDouble();

                        System.out.print(
                                "Enter divisor: ");

                        double divisor =
                                sc.nextDouble();

                        double result =
                                BankingService
                                        .calculateInterest(
                                                amount,
                                                divisor);

                        System.out.println(
                                "Result = "
                                        + result);

                        break;

                    case 7:

                        FileHandler
                                .readCustomerData();

                        break;

                    case 0:

                        System.out.println(
                                "Program Ended");
                        break;
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

            catch (ArithmeticException e) {
                System.out.println(
                        "Cannot divide by zero.");
            }

            catch (SecurityException e) {
                System.out.println(
                        e.getMessage());
            }

            catch (Exception e) {
                System.out.println(
                        "Error: "
                                + e.getMessage());
            }

            finally {
                System.out.println(
                        "Returning to menu...");
            }

        } while (choice != 0);

        sc.close();
    }
}
package model;

import java.util.ArrayList;
import java.util.List;
import exceptions.BankingExceptions.*;

public class BankAccount {
    private static int nextAccountNumber = 1001;
    private int accountNumber;
    private String customerName;
    private double balance;
    private String accountType;
    private int pin;
    private boolean active;
    private List<String> transactions;
    public BankAccount(String customerName, double initialDeposit, String accountType, int pin)
            throws InvalidAmountException {

        if (initialDeposit < 0) {
            throw new InvalidAmountException("Initial deposit cannot be negative.");
        }
        this.accountNumber = nextAccountNumber++;
        this.customerName = customerName;
        this.balance = initialDeposit;
        this.accountType = accountType;
        this.pin = pin;
        this.active = true;
        this.transactions = new ArrayList<>();
        addTransaction("Account opened - Type: " + accountType + ", Initial deposit: " + initialDeposit);
    }
    public BankAccount(int accountNumber, String customerName, double balance) {
        this.accountNumber = accountNumber;
        this.customerName = customerName;
        this.balance = balance;
        this.accountType = "Savings";
        this.pin = 1234;
        this.active = true;
        this.transactions = new ArrayList<>();
    }
    public int getAccountNumber() {
        return accountNumber;
    }
    public String getCustomerName() {
        return customerName;
    }
    public double getBalance() {
        return balance;
    }
    public String getAccountType() {
        return accountType;
    }
    public boolean isActive() {
        return active;
    }
    public List<String> getTransactions() {
        return transactions;
    }
    public boolean verifyPin(int enteredPin) {
        return this.pin == enteredPin;
    }
    public void changePin(int oldPin, int newPin)
            throws SecurityException {
        if (this.pin != oldPin) {
            throw new SecurityException("Incorrect current PIN.");
        }
        this.pin = newPin;
        addTransaction("PIN changed successfully.");
    }
    public void closeAccount() {
        this.active = false;
        addTransaction("Account closed.");
    }
    public void deposit(double amount)
            throws InvalidAmountException {
        if (!active) {
            throw new InvalidAmountException("Account is closed.");
        }
        if (amount <= 0) {
            throw new InvalidAmountException("Invalid deposit amount.");
        }
        balance += amount;
        addTransaction("Deposited: " + amount);
    }
    public void withdraw(double amount)
            throws InvalidAmountException,
            InsufficientBalanceException {

        if (!active) {
            throw new InvalidAmountException( "Account is closed.");
        }
        if (amount <= 0) {
            throw new InvalidAmountException("Invalid withdrawal amount.");
        }
        if (amount > balance) {
            throw new InsufficientBalanceException( "Insufficient balance.");
        }
        balance -= amount;
        addTransaction("Withdrawn: " + amount);
    }
    public void transferMoney(BankAccount receiver,  double amount)
            throws InvalidAmountException,
            InsufficientBalanceException,
            AccountNotFoundException {

        if (receiver == null || !receiver.isActive()) {
            throw new AccountNotFoundException("Receiver account not found or inactive.");
        }

        withdraw(amount);
        receiver.balance += amount;
        receiver.addTransaction("Received: " + amount + " from account " + accountNumber);
        addTransaction("Transferred: " + amount + " to account " + receiver.getAccountNumber());
    }
    public void checkBalance() {
        System.out.println("Account " + accountNumber  + " (" + customerName + ") [" + accountType + "] Balance: " + balance);
    }

    private void addTransaction(String description) {
        transactions.add(java.time.LocalDateTime.now().toString().replace('T', ' ').substring(0, 19) + " | " + description);
    }
    @Override
    public String toString() {
        return "Account " + accountNumber + " - " + customerName + " - " + accountType + " - Balance: " + balance + (active ? "" : " [CLOSED]");
    }
}

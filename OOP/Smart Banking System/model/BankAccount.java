package model;

import exceptions.*;

public class BankAccount {

    private int accountNumber;
    private String customerName;
    private double balance;

    public BankAccount(int accountNumber,
                       String customerName,
                       double balance) {

        this.accountNumber = accountNumber;
        this.customerName = customerName;
        this.balance = balance;
    }

    public int getAccountNumber() {
        return accountNumber;
    }

    public double getBalance() {
        return balance;
    }

    public void deposit(double amount)
            throws InvalidAmountException {

        if (amount <= 0) {
            throw new InvalidAmountException(
                    "Invalid deposit amount.");
        }

        balance += amount;
    }

    public void withdraw(double amount)
            throws InvalidAmountException,
            InsufficientBalanceException {

        if (amount <= 0) {
            throw new InvalidAmountException(
                    "Invalid withdrawal amount.");
        }

        if (amount > balance) {
            throw new InsufficientBalanceException(
                    "Insufficient balance.");
        }

        balance -= amount;
    }

    public void transferMoney(BankAccount receiver,
                              double amount)
            throws InvalidAmountException,
            InsufficientBalanceException,
            AccountNotFoundException {

        if (receiver == null) {
            throw new AccountNotFoundException(
                    "Account not found.");
        }

        withdraw(amount);
        receiver.balance += amount;
    }

    public void checkBalance() {
        System.out.println(balance);
    }
}

package org.bankSystem.model;

public class BankAccount {

    private double balance;
    private int accountId;
    private Users users;
    private Currency currency;


    public BankAccount(double balance, int accountId, Users users, Currency currency) {
        this.balance = balance;
        this.accountId = accountId;
        this.users = users;
        this.currency = currency;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public int getAccountId() {
        return accountId;
    }

    public void setId(int id) {
        this.accountId = id;
    }

    public Users getUsers() {
        return users;
    }

    public void setUsers(Users users) {
        this.users = users;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    @Override
    public String toString() {
        return "BankAccount{" +
                "balance=" + balance +
                ", id='" + accountId + '\'' +
                ", users=" + users +
                ", currency=" + currency +
                '}';
    }
}

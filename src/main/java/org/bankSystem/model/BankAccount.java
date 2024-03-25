package org.bankSystem.model;

public class BankAccount {

    private double balance;
    private int id;
    private Users users;
    private Currency currency;


    public BankAccount(double balance, int id, Users users, Currency currency) {
        this.balance = balance;
        this.id = id;
        this.users = users;
        this.currency = currency;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    @Override
    public String toString() {
        return "BankAccount{" +
                "balance=" + balance +
                ", id='" + id + '\'' +
                ", users=" + users +
                ", currency=" + currency +
                '}';
    }
}

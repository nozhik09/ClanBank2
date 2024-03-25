package org.bankSystem.model;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class Users {

    private int id;
    private String name;
    private String email;
    private String password;
    private Role role;
    private List<BankAccount> accounts;

    public Users(int id, String name, String email, String password, Role role, List<BankAccount> accounts) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
        this.accounts = new ArrayList<>();
    }

    public List<BankAccount> getBankAccounts() {
        return accounts;
    }

    public void setBankAccounts(List<BankAccount> accounts) {
        this.accounts = accounts;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "Users{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", role=" + role +
                ", accounts=" + accounts +
                '}';
    }
}

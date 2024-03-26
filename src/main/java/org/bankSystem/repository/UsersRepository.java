package org.bankSystem.repository;

import org.bankSystem.model.BankAccount;
import org.bankSystem.model.Role;
import org.bankSystem.model.Users;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class UsersRepository {
    private Map<Integer, Users> usersMap;
    public final AtomicInteger userId = new AtomicInteger(1);

    public UsersRepository() {
        this.usersMap = new HashMap<>();
        initUser();
    }

    private void initUser() {
        List<BankAccount> emptyAccountsList = new ArrayList<>();
        Users adminUser = new Users(userId.getAndIncrement(), "admin123",
                "Vasya@gmail.com", "Kakasss1223!", Role.ADMIN, emptyAccountsList);
        Users user2 = new Users(userId.getAndIncrement(), "user133",
                "admnin", "admnin", Role.USER, emptyAccountsList);
        Users user3 = new Users(userId.getAndIncrement(), "user143",
                "user", "user", Role.USER, emptyAccountsList);

        addUser(adminUser);
        addUser(user2);
        addUser(user3);
    }

    public void addUser(Users user) {
        if (user.getId() == 0) {
            int id = userId.getAndIncrement();
            user.setId(id);
        }
        usersMap.put(user.getId(), user);
    }


    //Добавляем нашему Юзеру аккаут банка, и там выбираем в какой валюте и чего... д
    public void addBankAccountToUserByEmail(String email, BankAccount account) {
        Users user = findUserByEmail(email);
        if (user != null) {
            user.getBankAccounts().add(account);
        }
    }

    // Этот метод возможно нужен будет админу:
    public void addBankAccountToUserByUser(int userId, BankAccount account) {
        Users user = getUserById(userId);
        if (user != null) {
            user.getBankAccounts().add(account);
        }
    }


    public Users getUserById(int id) {
        return usersMap.get(id);
    }

    public Users findUserByEmail(String email) {
        for (Users user : usersMap.values()) {
            if (user.getEmail().equalsIgnoreCase(email)) {
                return user;
            }
        }
        return null;
    }

    public void deleteUser(int id) {
        usersMap.remove(id);
    }

    public void deleteUser(String email){
        usersMap.remove(findUserByEmail(email).getId());
    }

    public void updateUser(Users newUser) {
        int newUserId = newUser.getId();
        Users user = getUserById(newUserId);

        user.setName(newUser.getName());
        user.setEmail(newUser.getEmail());
        user.setPassword(newUser.getPassword());
        user.setRole(newUser.getRole());
        user.setBankAccounts(newUser.getBankAccounts());

        usersMap.put(newUserId, user);
    }


    public List<Users> getAllUsers() {
        return new ArrayList<>(usersMap.values());
    }

    public void changeRole(int userId, Role newRole) {
        Users user = getUserById(userId);
        if (user != null) {
            user.setRole(newRole);
        }
    }
}


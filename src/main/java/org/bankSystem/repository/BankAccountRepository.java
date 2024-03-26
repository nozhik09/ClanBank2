package org.bankSystem.repository;
import org.bankSystem.model.BankAccount;
import org.bankSystem.model.Currency;
import org.bankSystem.model.Role;
import org.bankSystem.model.Users;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class BankAccountRepository {

    private Map<Integer, BankAccount> bankAccounts = new HashMap<>();

    private AtomicInteger nextAccountId = new AtomicInteger(1);

    public BankAccountRepository() {
        Users adminUser = new Users(1, "Vasya@gmail.com", "Kakasss1223!", "Kaka50s1223!", Role.USER, new ArrayList<>());
        Users user2 = new Users(2, "user133", "Vasya@gm3il.com", "Kaka54s1223!", Role.USER, new ArrayList<>());
        Users user3 = new Users(3, "user143", "user", "user", Role.USER, new ArrayList<>());

        // Инициализация валют
        Currency currencyUSD = new Currency("US Dollar", "USD");
        Currency currencyEUR = new Currency("Euro", "EUR");
        Currency currencyJPY = new Currency("Japanese Yen", "JPY");

        bankAccounts.put(101, new BankAccount(1000.0, 101, adminUser, currencyEUR));
        bankAccounts.put(102, new BankAccount(2000.0, 102, user2, currencyUSD));
        bankAccounts.put(103, new BankAccount(3000.0, 103, user3, currencyJPY));

    }
    public int generateNewAccountId() {
        return nextAccountId.getAndIncrement();
    }

    public void addBankAccount(BankAccount bankAccount) {
        if (bankAccount != null && !bankAccounts.containsKey(bankAccount.getAccountId())) {
            bankAccounts.put(bankAccount.getAccountId(), bankAccount);
        } else {
            throw new IllegalArgumentException("Account already exist or is null");
        }
        int accountId = generateNewAccountId();
        bankAccount.setAccountId(accountId);
        bankAccounts.put(accountId, bankAccount);

    }

    public Optional<BankAccount> getBankAccountById(int id) {
        return Optional.ofNullable(bankAccounts.get(id));
    }

    public void deleteBankAccount(int id) {
        bankAccounts.remove(id);
    }

    public void updateBankAccount(BankAccount updatedAccount) {
        bankAccounts.put(updatedAccount.getAccountId(), updatedAccount);
    }

    public List<BankAccount> getBankAccountsByUserId(int userId) {
        return bankAccounts.values().stream()
                .filter(account -> account.getUsers().getId() == userId)
                .collect(Collectors.toList());
    }

    public List<BankAccount> getBankAccountsByCurrency(String currencyCode) {
        return bankAccounts.values().stream()
                .filter(account -> account.getCurrency().getCode().equals(currencyCode))
                .collect(Collectors.toList());
    }

    public List<BankAccount> getAllBankAccounts() {
        return new ArrayList<>(bankAccounts.values());
    }

    public Optional<BankAccount> getBankAccountByIdAndCurrency(int accountId, String currencyCode) {
        return bankAccounts.values().stream()
                .filter(bankAccount -> bankAccount.getAccountId() == accountId && bankAccount.getCurrency().getCode().equalsIgnoreCase(currencyCode))
                .findFirst();
    }

    public Optional<BankAccount> getBankAccountByUserIdAndCurrency(int userId, String currencyCode) {
        return bankAccounts.values().stream()
                .filter(bankAccount -> bankAccount.getUsers().getId() == userId && bankAccount.getCurrency().getCode().equalsIgnoreCase(currencyCode))
                .findFirst();
    }

}

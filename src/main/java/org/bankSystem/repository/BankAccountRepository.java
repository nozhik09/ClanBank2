package repository;
import model.BankAccount;
import model.Currency;
import model.Role;
import model.Users;

import java.util.*;
import java.util.stream.Collectors;

public class BankAccountRepository {

    private Map<Integer, BankAccount> bankAccounts = new HashMap<>();

    public BankAccountRepository() {
        Users adminUser = new Users(1, "Vasya@gmail.com", "Kakasss1223!","Kaka50s1223!", Role.USER, new ArrayList<>());
        Users user2 = new Users(2, "user133", "Vasya@gm3il.com", "Kaka54s1223!", Role.USER, new ArrayList<>());
        Users user3 = new Users(3, "user143", "Vasya@gm2mail.com", "Kaka35s1223!", Role.USER, new ArrayList<>());

        // Инициализация валют
        Currency currencyUSD = new Currency("US Dollar", "USD");
        Currency currencyEUR = new Currency("Euro", "EUR");
        Currency currencyJPY = new Currency("Japanese Yen", "JPY");

        bankAccounts.put(101, new BankAccount(1000.0, 101, adminUser, currencyEUR));
        bankAccounts.put(102, new BankAccount(2000.0, 102, user2, currencyUSD));
        bankAccounts.put(103, new BankAccount(3000.0, 103, user3, currencyJPY));

    }

    public void addBankAccount(BankAccount bankAccount) {
        bankAccounts.put(bankAccount.getId(), bankAccount);
    }

    public Optional<BankAccount> getBankAccountById(int id) {
        return Optional.ofNullable(bankAccounts.get(id));
    }

    public void deleteBankAccount(int id) {
        bankAccounts.remove(id);
    }

    public void updateBankAccount(BankAccount updatedAccount) {
        bankAccounts.put(updatedAccount.getId(), updatedAccount);
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

}

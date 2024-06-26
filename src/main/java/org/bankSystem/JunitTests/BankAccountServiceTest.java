package org.bankSystem.JunitTests;

import org.bankSystem.model.BankAccount;
import org.bankSystem.model.Currency;
import org.bankSystem.model.Role;
import org.bankSystem.model.Users;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.bankSystem.repository.BankAccountRepository;
import org.bankSystem.repository.UsersRepository;
import org.bankSystem.service.BankAccountService;
import org.bankSystem.service.CurrencyService;
import org.bankSystem.service.OperationService;
import org.bankSystem.service.UserService;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class BankAccountServiceTest {

    private BankAccountService bankAccountService;
    private BankAccountRepository bankAccountRepository;
    private UsersRepository usersRepository;
    private UserService userService;
    private CurrencyService currencyService;
    private OperationService operationService;

    @BeforeEach
    void setUp() {
        bankAccountRepository = new BankAccountRepository();
        usersRepository = new UsersRepository();
        userService = new UserService(usersRepository,bankAccountRepository);
        currencyService = new CurrencyService();
        currencyService.setTestMode(true);
        operationService = new OperationService();

        bankAccountService = new BankAccountService(bankAccountRepository, userService, currencyService, operationService);
    }
    @Test

    void exchangeCurrencyTest() {
        // Предположим, что курс обмена 1 USD = 0.85 EUR
        double exchangeRate = 0.85;
        double amountToExchange = 100.0;
        double expectedUsdBalanceAfterExchange = 900.0; // 1000 - 100
        double expectedEurBalanceAfterExchange = 585.0; // 500 + (100 * 0.85)

        Users user = new Users(1, "test@example.com", "Test", "pass", null, new ArrayList<>());
        Currency usd = new Currency("US Dollar", "USD");
        Currency eur = new Currency("Euro", "EUR");
        BankAccount usdAccount = new BankAccount(1000.0, 1, user, usd);
        BankAccount eurAccount = new BankAccount(500.0, 2, user, eur);

        bankAccountRepository.addBankAccount(usdAccount);
        bankAccountRepository.addBankAccount(eurAccount);

        bankAccountService.exchangeCurrency(usdAccount.getAccountId(), "USD", "EUR", amountToExchange); // Выполнение операции обмена валюты

        BankAccount updatedUsdAccount = bankAccountRepository.getBankAccountById(1).orElseThrow();
        BankAccount updatedEurAccount = bankAccountRepository.getBankAccountById(2).orElseThrow();

        assertEquals(expectedUsdBalanceAfterExchange, updatedUsdAccount.getBalance());
        assertEquals(expectedEurBalanceAfterExchange, updatedEurAccount.getBalance());
    }


    @Test
    void testOpenAccount() {
        int existingUserId = 1;
        String currencyCode = "USD";
        double initialBalance = 100.0;

        Users userBefore = usersRepository.getUserById(existingUserId);
        assertNotNull(userBefore, "До выполнения операции пользователь должен существовать в репозитории");

        bankAccountService.openAccount(existingUserId, currencyCode, initialBalance);

        Users userAfter = usersRepository.getUserById(existingUserId);
        assertNotNull(userAfter, "После выполнения операции пользователь не должен быть null");

        boolean hasAccount = userAfter.getBankAccounts().stream()
                .anyMatch(account -> account.getBalance() == initialBalance && account.getCurrency().getCode().equals(currencyCode));

        assertTrue(hasAccount, "У пользователя должен быть новый счет с указанными балансом и валютой");

        BankAccount newAccount = userAfter.getBankAccounts().stream()
                .findFirst()
                .orElse(null);

        assertNotNull(newAccount, "Новый счет не должен быть null");
        assertNotNull(bankAccountRepository.getBankAccountById(newAccount.getAccountId()).orElse(null), "Новый счет должен быть в репозитории");
    }



    @Test
    void deposit() {
        int userId = 1;
        double initialBalance = 50.0;
        String currencyCode = "USD";

        bankAccountService.openAccount(userId, currencyCode, initialBalance);
        BankAccount account = usersRepository.getUserById(userId).getBankAccounts().get(0);

        double depositAmount = 100.0;
        bankAccountService.deposit(account.getAccountId(), depositAmount);

        BankAccount updatedAccount = bankAccountRepository.getBankAccountById(account.getAccountId()).orElse(null);
        assertNotNull(updatedAccount, "Аккаунт должен существовать после депозита");
        assertEquals(initialBalance + depositAmount, updatedAccount.getBalance(), "Баланс аккаунта должен увеличиться на сумму депозита");
    }

    @Test
    void withdraw() {
        int userId = 1;
        double initialBalance = 150.0;
        String currencyCode = "USD";

        bankAccountService.openAccount(userId, currencyCode, initialBalance);
        BankAccount account = usersRepository.getUserById(userId).getBankAccounts().get(0);

        double withdrawAmount = 50.0;
        bankAccountService.withdraw(account.getAccountId(), withdrawAmount);

        BankAccount updatedAccount = bankAccountRepository.getBankAccountById(account.getAccountId()).orElse(null);
        assertNotNull(updatedAccount, "Аккаунт должен существовать после снятия");
        assertEquals(initialBalance - withdrawAmount, updatedAccount.getBalance(), "Баланс аккаунта должен уменьшиться на сумму снятия");
    }



    @Test
    void closeAccount() {
        Users user = new Users(1, "email@example.com", "password", "User",Role.USER, new ArrayList<BankAccount>());
        usersRepository.addUser(user);
        BankAccount account = new BankAccount(100.0, 1, user, new Currency("USD", "US Dollar"));
        bankAccountRepository.addBankAccount(account);

        bankAccountService.closeAccount(account.getAccountId());

        assertNull(bankAccountRepository.getBankAccountById(account.getAccountId()).orElse(null));
    }

    @Test
    void printAccountOperation() {
    }

    @Test
    void testCheckBalance() {
        // Создаем пользователя и счет
        Users user = new Users(1, "email@example.com", "password", "User",Role.USER, new ArrayList<BankAccount>());
        usersRepository.addUser(user);
        BankAccount account = new BankAccount(100.0, 1, user, new Currency("USD", "US Dollar"));
        bankAccountRepository.addBankAccount(account);

        // Проверяем баланс
        double balance = bankAccountService.checkBalance(account.getAccountId());
        assertEquals(100.0, balance);
    }

    @Test
    void transfer() {
        int userFromId = 1;
        int userToId = 2;
        double initialBalanceFrom = 200.0;
        double initialBalanceTo = 100.0;
        String currencyCode = "USD";

        bankAccountService.openAccount(userFromId, currencyCode, initialBalanceFrom);
        bankAccountService.openAccount(userToId, currencyCode, initialBalanceTo);
        BankAccount accountFrom = usersRepository.getUserById(userFromId).getBankAccounts().get(0);
        BankAccount accountTo = usersRepository.getUserById(userToId).getBankAccounts().get(0);

        double transferAmount = 50.0;
        bankAccountService.transfer(accountFrom.getAccountId(), accountTo.getAccountId(), transferAmount);

        BankAccount updatedAccountFrom = bankAccountRepository.getBankAccountById(accountFrom.getAccountId()).orElse(null);
        BankAccount updatedAccountTo = bankAccountRepository.getBankAccountById(accountTo.getAccountId()).orElse(null);
        assertNotNull(updatedAccountFrom, "Аккаунт отправителя должен существовать после перевода");
        assertNotNull(updatedAccountTo, "Аккаунт получателя должен существовать после перевода");
        assertEquals(initialBalanceFrom - transferAmount, updatedAccountFrom.getBalance(), "Баланс отправителя должен уменьшиться на сумму перевода");
        assertEquals(initialBalanceTo + transferAmount, updatedAccountTo.getBalance(), "Баланс получателя должен увеличиться на сумму перевода");

    }
}
package JunitTests;

import model.BankAccount;
import model.Currency;
import model.Role;
import model.Users;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import repository.BankAccountRepository;
import repository.UsersRepository;
import service.BankAccountService;
import service.CurrencyService;
import service.OperationService;
import service.UserService;

import java.util.ArrayList;

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
        operationService = new OperationService();

        bankAccountService = new BankAccountService(bankAccountRepository, userService, currencyService, operationService);
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
        assertNotNull(bankAccountRepository.getBankAccountById(newAccount.getId()).orElse(null), "Новый счет должен быть в репозитории");
    }



    @Test
    void deposit() {
        int userId = 1;
        double initialBalance = 50.0;
        String currencyCode = "USD";

        bankAccountService.openAccount(userId, currencyCode, initialBalance);
        BankAccount account = usersRepository.getUserById(userId).getBankAccounts().get(0);

        double depositAmount = 100.0;
        bankAccountService.deposit(account.getId(), depositAmount);

        BankAccount updatedAccount = bankAccountRepository.getBankAccountById(account.getId()).orElse(null);
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
        bankAccountService.withdraw(account.getId(), withdrawAmount);

        BankAccount updatedAccount = bankAccountRepository.getBankAccountById(account.getId()).orElse(null);
        assertNotNull(updatedAccount, "Аккаунт должен существовать после снятия");
        assertEquals(initialBalance - withdrawAmount, updatedAccount.getBalance(), "Баланс аккаунта должен уменьшиться на сумму снятия");
    }

    @Test
    void exchangeCurrency() {

        Users user = new Users(1, "email@example.com", "password", "User", Role.USER, new ArrayList<BankAccount>());
        usersRepository.addUser(user);
        BankAccount account = new BankAccount(100.0, 1, user, new Currency("USD", "US Dollar"));
        bankAccountRepository.addBankAccount(account);

        bankAccountService.exchangeCurrency(account.getId(), "EUR", 50.0);
    }


    @Test
    void closeAccount() {
        Users user = new Users(1, "email@example.com", "password", "User",Role.USER, new ArrayList<BankAccount>());
        usersRepository.addUser(user);
        BankAccount account = new BankAccount(100.0, 1, user, new Currency("USD", "US Dollar"));
        bankAccountRepository.addBankAccount(account);

        bankAccountService.closeAccount(account.getId());

        assertNull(bankAccountRepository.getBankAccountById(account.getId()).orElse(null));
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
        double balance = bankAccountService.checkBalance(account.getId());
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
        bankAccountService.transfer(accountFrom.getId(), accountTo.getId(), transferAmount);

        BankAccount updatedAccountFrom = bankAccountRepository.getBankAccountById(accountFrom.getId()).orElse(null);
        BankAccount updatedAccountTo = bankAccountRepository.getBankAccountById(accountTo.getId()).orElse(null);
        assertNotNull(updatedAccountFrom, "Аккаунт отправителя должен существовать после перевода");
        assertNotNull(updatedAccountTo, "Аккаунт получателя должен существовать после перевода");
        assertEquals(initialBalanceFrom - transferAmount, updatedAccountFrom.getBalance(), "Баланс отправителя должен уменьшиться на сумму перевода");
        assertEquals(initialBalanceTo + transferAmount, updatedAccountTo.getBalance(), "Баланс получателя должен увеличиться на сумму перевода");

    }
}
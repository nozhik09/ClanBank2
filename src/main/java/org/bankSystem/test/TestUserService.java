/*
 * created by max$
 */

package org.bankSystem.test;


import org.bankSystem.model.*;

import org.bankSystem.repository.BankAccountRepository;
import org.bankSystem.repository.CurrencyRepository;
import org.bankSystem.repository.UsersRepository;
import org.bankSystem.service.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class TestUserService {
    private UserService userService;
    private UsersRepository userRepository;
    private BankAccountRepository bankAccountRepository;
    private BankAccountService bankAccountService;
    private CurrencyRepository currencyRepository;
    private CurrencyService currencyService;
    private OperationService operationService;


    @BeforeEach
    void setUp() {
        bankAccountRepository = new BankAccountRepository();
        bankAccountService = new BankAccountService(bankAccountRepository, userService, currencyService, operationService);
        userRepository = new UsersRepository();
        userService = new UserService(userRepository, bankAccountRepository);
        currencyService = new CurrencyService();
    }

    //тест на поиск по емаилу
    @Test
    void getUserByEmail() {
        String name = "Test User";
        String email = "test@example.com";
        String password = "Test123!!!";
        Users user = new Users(1, name, email, password, Role.USER, new ArrayList<>());
        userRepository.addUser(user);

        Users testUser = userService.getUserByEmail(email);

        assertNotNull(testUser);
        assertEquals(name, testUser.getName());
        assertEquals(email, testUser.getEmail());
    }

    //тест на поиск по айди
    @Test
    void getUserById() {
        String name = "Test User";
        String email = "test@example.com";
        String password = "Test123!";
        Users user = new Users(55555, name, email, password, Role.USER, new ArrayList<>());
        userRepository.addUser(user);

        Users testUser = userService.getUserById(55555);

        assertNotNull(testUser);
        assertEquals(name, testUser.getName());
        assertEquals(email, testUser.getEmail());
    }

    //тест на добавление юзера
    @Test
    void testAddUser() {
        String name = "Test User";
        String email = "test@example.com";
        String password = "Test123!";
        Users user = new Users(999888, name, email, password, Role.USER, new ArrayList<>());

        userService.addUser(user);
        Users testUser = userRepository.getUserById(999888);

        assertNotNull(testUser);
        assertEquals(name, testUser.getName());
        assertEquals(email, testUser.getEmail());
    }

    //тест на реигстрацию
    @Test
    void registerUserValid() throws PasswordValidationException {
        String name = "Test User";
        String email = "test@example.com";
        String password = "Test123!!!";

        Users registeredUser = userService.registerUser(name, email, password);

        assertNotNull(registeredUser);
        assertEquals(name, registeredUser.getName());
        assertEquals(email, registeredUser.getEmail());
        assertEquals(Role.USER, registeredUser.getRole());
        assertEquals(4, userRepository.getAllUsers().size()); //три уже есть в репазитории
    }

    // тест на уникальность е-маил
    @Test
    void registerUserEmailReturnsNull() throws PasswordValidationException {
        String name1 = "Test User";
        String email1 = "test@example.com";
        String password1 = "Test123!!!";
        userService.registerUser(name1, email1, password1);

        String name2 = "test2 User";
        String email2 = "test@example.com";
        String password2 = "Test123!!!";
        Users registeredUser = userService.registerUser(name2, email2, password2);

        assertNull(registeredUser);
    }

    //тест на авторизацию
    @Test
    void authenticateUser() throws PasswordValidationException {
        String name = "Test User";
        String email = "test@example.com";
        String password = "Test123!!!";
        userService.registerUser(name, email, password);

        Users authenticatedUser = userService.authenticateUser(email, password);

        assertNotNull(authenticatedUser);
        assertEquals(name, authenticatedUser.getName());
        assertEquals(email, authenticatedUser.getEmail());
    }

    // теsт на пароль в авторизации
    @Test
    void authenticateUserPassword() throws PasswordValidationException {
        String email = "test@example.com";
        String password = "Test123!!!";
        userService.registerUser("Test User", email, password);

        Users authenticatedUser = userService.authenticateUser(email, "Test321!!");

        assertNull(authenticatedUser);
    }

    //тест на удаление Юзера
    @Test
    void deleteUserByEmail() throws PasswordValidationException {
        String name = "Test User";
        String email = "test@example.com";
        String password = "Test123!!!";
        userService.registerUser(name, email, password);

        userService.deleteUserByEmail(email);

        assertNull(userRepository.findUserByEmail(email));
    }

    @Test
    void getAllUsers() throws PasswordValidationException {
        userService.registerUser("User 1", "test1@example.com", "Test123!!!");
        userService.registerUser("User 2", "test2@example.com", "Test123!!!!");
        userService.registerUser("User 3", "test3@example.com", "Test123!!!!!");

        assertEquals(6, userService.getAllUsers().size());//3 уже есть+ 3 новых
    }

    @Test
    void testGetAllBankAccountUsers() {
        String email = "test@example.com";
        // Создаем пользователя и добавляем его в репозиторий
        Users user = new Users(9999, "Test User", email, "Password777!!!", Role.USER, null);
        userRepository.addUser(user);

        BankAccount account = new BankAccount(1000.0, 1, user, new Currency("USD", "USD"));
        BankAccount account1 = new BankAccount(1.0, 2, user, new Currency("EVRO", "EUR"));
        BankAccount account2 = new BankAccount(123.0, 3, user, new Currency("Bitkoin", "BIT"));
        bankAccountRepository.addBankAccount(account);
        bankAccountRepository.addBankAccount(account1);
        bankAccountRepository.addBankAccount(account2);


        List<BankAccount> result = userService.getAllBankAccountUsers(email);

        assertNotNull(result);
        assertEquals(3, result.size()); // Проверяем, что список содержит банковскиe счеta
    }

    @Test
    void testGetAllCurrency() {
        Set<Currency> currencies = currencyService.getAllCurrency();
        assertNotNull(currencies);
        assertFalse(currencies.isEmpty());
    }
    @Test
    void testAddNewCurrency() {
        Currency newCurrency = currencyService.addNewCurrency("JPY", "Ejena", 163.7800);
        assertNotNull(newCurrency);
        assertEquals("JPY", newCurrency.getCode());
        assertEquals("Ejena", newCurrency.getName());
        assertEquals(163.7800, currencyService.getExchangeCourse(newCurrency.getCode()));
    }



    @Test
    void testRemoveCurrency() {
        Currency removedCurrency = currencyService.removeCurrency("US Dollar", "USD");
        assertNotNull(removedCurrency);
        assertEquals("USD", removedCurrency.getCode());
        assertEquals("US Dollar", removedCurrency.getName());
        assertEquals(1.0835, currencyService.getExchangeCourse(removedCurrency.getCode()));
    }


    @Test
    void testGetCurrencyByCode() {
        Currency currency = currencyService.getCurrencyByCode("USD");
        assertNotNull(currency);
        assertEquals("USD", currency.getCode());
        assertEquals("American Dollar", currency.getName());
        assertEquals(1.0835, currencyService.getExchangeCourse(currency.getCode()));
    }

    @Test
    void testGetExchangeCourse() {
        double exchangeRate = currencyService.getExchangeCourse("CAD");
        assertEquals(1.4722, exchangeRate);
    }


}




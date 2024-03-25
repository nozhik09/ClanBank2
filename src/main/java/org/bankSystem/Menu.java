package org.bankSystem;

import org.bankSystem.model.BankAccount;
import org.bankSystem.model.Course;
import org.bankSystem.model.Users;
import org.bankSystem.network.CurrencyNetworkWorker;
import org.bankSystem.network.LatestCurrencyResponse;
import org.bankSystem.repository.BankAccountRepository;
import org.bankSystem.repository.CurrencyRepository;
import org.bankSystem.repository.UsersRepository;
import org.bankSystem.service.*;
import org.bankSystem.util.MyLinkedList;

import java.util.*;

import static org.bankSystem.service.UserService.isValidEmail;
import static org.bankSystem.service.UserService.isValidPassword;

public class Menu {
    enum MenuItem {
        AUTORIZE("Авторизация"),
        REGISTRATION("Регистрация"),
        OPEN_ACCOUNT("Открыть счёт"),
        CHECK_BALANCE("Баланс"),
        DEPOSIT("Пополнение счёта"),
        WITHDRAW("Снятие средств"),
        EXCHANGE_CURRENCY("Обмен валюты"),
        PRINT_ACCOUNT_OPERATION("История транзакций"),
        CLOSE_ACCOUNT("Закрытие счёта"),
        EXCHANGE_HISTORY("История курса валют"),
        ADD_NEW_CURRENCY("Добавить новую валюту"),
        CHECK_ALL_OPERATIONS("Просмотр всех операций"),
        CHECK_ALL_ACCOUNTS("Просмотр всех счетов"),
        CHANGES_EXCHANGE("Изменение курса валют"),
        EXIT("Выход");


        private final String title;

        MenuItem(String title) {
            this.title = title;
        }

        @Override
        public String toString() {
            return title;
        }

    }

    // Private properties
    private Users user;
    private final Scanner scanner = new Scanner(System.in);
    private MyLinkedList<MenuItem> menu = new MyLinkedList<>();
    private service.UserService userService = new service.UserService(new UsersRepository());
    private service.CurrencyService currencyService = new service.CurrencyService();
    private service.OperationService operationService = new service.OperationService();
    private BankAccountRepository bankAccountRepository = new BankAccountRepository();
    private service.BankAccountService bankAccountService = new service.BankAccountService(
            bankAccountRepository,
            userService,
            currencyService,
            operationService);

    // Public methods
    public void run() {
        createAuthMenu();
        while (true) {
            displayMenu();
            inputUserAction();
        }

    }

    // Private methods
    private void displayMenu() {
        System.out.println("\nМеню:");
        for (int i = 0; i < menu.size(); i++) {
            System.out.printf("%d. %s\n", (i + 1), menu.get(i));
        }
    }

    private void inputUserAction() {
        System.out.print("Выберите опцию: ");
        int choice = scanner.nextInt();
        scanner.nextLine();
        MenuItem menuItem = menu.get(choice - 1);
        menuAction(menuItem);
    }

    private void menuAction(MenuItem menuItem) {
        switch (menuItem) {
            case AUTORIZE:
                loginAction();
                break;
            case REGISTRATION:
                registerAction();
                break;
            case OPEN_ACCOUNT:
                openAccountAction();
                break;
            case CHECK_BALANCE:
                checkBalanceAction();
                break;
            case DEPOSIT:
                depositAction();
                break;
            case WITHDRAW:
                withdrawAction();
                break;
            case EXCHANGE_CURRENCY:
                exchangeCurrencyAction();
                break;
            case PRINT_ACCOUNT_OPERATION:
                printAccountOperationsAction();
                break;
            case CLOSE_ACCOUNT:
                closeAccountAction();
                break;
            case EXCHANGE_HISTORY:
                exchangeHistoryAction();
                break;
            case ADD_NEW_CURRENCY:

                break;
            case CHANGES_EXCHANGE:

                break;
            case CHECK_ALL_ACCOUNTS:

                break;
            case CHECK_ALL_OPERATIONS:

                break;

            case EXIT:
                break;
        }
    }

    // Private create menu methods
    private void createAuthMenu() {
        menu = new MyLinkedList<>();
        menu.addAll(
                MenuItem.AUTORIZE,
                MenuItem.REGISTRATION,
                MenuItem.EXIT);
    }

    private void createUserMenu() {
        menu = new MyLinkedList<>();
        menu.addAll(
                MenuItem.OPEN_ACCOUNT,
                MenuItem.CHECK_BALANCE,
                MenuItem.DEPOSIT,
                MenuItem.WITHDRAW,
                MenuItem.EXCHANGE_CURRENCY,
                MenuItem.EXCHANGE_HISTORY,
                MenuItem.PRINT_ACCOUNT_OPERATION,
                MenuItem.CLOSE_ACCOUNT,
                MenuItem.EXIT);
    }

    private void createAdminMenu() {
        menu = new MyLinkedList<>();
        menu.addAll(
                MenuItem.ADD_NEW_CURRENCY,
                MenuItem.CHANGES_EXCHANGE,
                MenuItem.CHECK_ALL_ACCOUNTS,
                MenuItem.CHECK_ALL_OPERATIONS,
                MenuItem.EXIT);
    }

    private void createRoleMenu() {
        switch (user.getRole()) {
            case USER:
                createUserMenu();
                break;
            case ADMIN:
                createAdminMenu();
                break;
        }
    }

    // Private menu actions
    private void loginAction() {
        System.out.print("Введите ваш Email: ");
        String email = scanner.nextLine();
        System.out.print("Введите ваш Пароль: ");
        String password = scanner.nextLine();
        user = userService.authenticateUser(email, password);
        if (user == null) {
            System.out.println("Неверный логин или пароль!");
        } else {
            System.out.println("Вход прошёл успешно!");
            createRoleMenu();
        }
    }

    private void registerAction() {
        System.out.println("Введите Имя");
        String name = scanner.nextLine();
        System.out.print("Введите ваш Email: ");
        String email = scanner.nextLine();
        System.out.print("Введите ваш Пароль: ");
        String password = scanner.nextLine();
        try {
            user = userService.registerUser(name, email, password);
            System.out.println("Вы успешно авторизировались!");
            createRoleMenu();
        } catch (service.EmailValidateException e) {
            System.out.println("Неправильный формат email: " + e.getMessage());
        } catch (service.PasswordValidationException e) {
            System.out.println("Неправильный формат пароля: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Something went wrong!");
            System.out.println(e.getMessage());
        }
    }

    private void openAccountAction() {
        System.out.print("Введите название валюты: ");
        String title = scanner.nextLine();
        //Стартовый баланс передавать не нужно, так как он всегда открывается с 0 деняг!!!
        bankAccountService.openAccount(user.getId(), title, 0);
        System.out.println("Валюта успешно добавленна!");
    }

    private void checkBalanceAction() {
        System.out.println("Ваш баланс по следующим валютам:");
        BankAccount bankAccount = choseBankAccount();
    }

    private void depositAction() {
        System.out.println("Какой счёт вы хотите пополнить?");
        BankAccount bankAccount = choseBankAccount();
        System.out.println("Сколько хотите внести денег: ");
        double amount = scanner.nextDouble();
        bankAccountService.deposit(bankAccount.getId(), amount);
        System.out.println("Счёт успешно пополнен");
    }

    private void withdrawAction() {
        System.out.println("С какого счёта вы хотите вывести деньги?");
        BankAccount bankAccount = choseBankAccount();
        System.out.println("Сколько вы хотите вывести деняг?");
        double amount = scanner.nextDouble();
        bankAccountService.withdraw(bankAccount.getId(), amount);
        System.out.println("Деньги успешно выведенны!");
    }


    private void exchangeCurrencyAction() {
        System.out.println("Какую валюту вы хотите обменять?");
        BankAccount bankAccount = choseBankAccount();
        System.out.println("Сколько вы хотите обменять??");
        double amount = scanner.nextDouble();
        bankAccountService.exchangeCurrency(bankAccount.getId(),bankAccount.getCurrency().getCode(), amount);
        System.out.println("Вы успешно обменяли деньги!");
    }

    private void printAccountOperationsAction() {
        System.out.println("Выберите счёт для просмотра транзакций: ");
        BankAccount bankAccount = choseBankAccount();
        System.out.println("История Транзацкий: ");
        bankAccountService.printAccountOperation(bankAccount.getId());
    }

    private void exchangeHistoryAction() {
        System.out.println("Курс валют");
        try {
            LatestCurrencyResponse latestCurrencyResponse = currencyService.getExchangeCourse();
            for (String key: latestCurrencyResponse.getRates().keySet()) {
                System.out.printf("%s: %.4f\n", key, latestCurrencyResponse.getRates().get(key));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void closeAccountAction() {
        System.out.println("Какой счёт вы хотите закрыть?");
        BankAccount bankAccount = choseBankAccount();
        bankAccountService.closeAccount(bankAccount.getId());
        System.out.println("Счёт успешно закрыт!");
    }

    private void checkAllAccountsAction() {
        System.out.println("Проверить транзакции всех аккаунтов");
        //Воздух

    }

    private void checkAllOperationsAction() {
        System.out.println("Проверить операции всех аккаунтов");
        //Воздух


    }

    private void addNewCurrency() {
        System.out.println("Добавить новую валюту:");
        String addCur = scanner.nextLine();
        currencyService.addCurrency(addCur);
        System.out.println("Успешно добавлена!");
    }

    private void changesExchange() {
        //Воздух
    }

    private void exitAction() {
        System.exit(0);
    }
    private BankAccount choseBankAccount() {
        List<BankAccount> bankAccounts = user.getBankAccounts();
        for (BankAccount bankAccount : bankAccounts) {
            int id = bankAccount.getId();
            double balance = bankAccount.getBalance();
            String code = bankAccount.getCurrency().getCode();
            System.out.printf("%d: %f %s\n", id, balance, code);
        }
        int accountId = scanner.nextInt();
        return bankAccounts.get(accountId);
    }
}


//    public static void main(String[] args) {
//        CurrencyNetworkWorker worker = new CurrencyNetworkWorker();
//        try {
//            LatestCurrencyResponse latestCurrencyResponse = new LatestCurrencyResponse();
//            for (String key: latestCurrencyResponse.getRates().keySet()) {
//                System.out.printf("%s: %.4f\n", key, latestCurrencyResponse.getRates().get(key));
//            }
//
//            Scanner scanner = new Scanner(System.in);
//            System.out.println("Currency code:");
//            String code = scanner.nextLine();
//            System.out.printf("%s: %.4f\n", code, latestCurrencyResponse.getRates().get(code));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

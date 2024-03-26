package org.bankSystem;


import org.bankSystem.model.BankAccount;
import org.bankSystem.model.Users;
import org.bankSystem.repository.BankAccountRepository;
import org.bankSystem.repository.CurrencyRepository;
import org.bankSystem.repository.UsersRepository;
import org.bankSystem.service.*;
import org.bankSystem.util.MyLinkedList;

import java.util.*;


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
        CHECK_ALL_ACCOUNTS("Просмотр всех счетов"),
        CHANGES_EXCHANGE("Изменение курса валют"),
        EXCHANGE_ROLE("Изменение роли"),
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
    private UserService userService = new UserService(new UsersRepository(), new BankAccountRepository());
    private CurrencyService currencyService = new CurrencyService();
    private OperationService operationService = new OperationService();
    private BankAccountRepository bankAccountRepository = new BankAccountRepository();
    private BankAccountService bankAccountService = new BankAccountService(
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
                addNewCurrency();
                break;
            case CHANGES_EXCHANGE:
                changesExchange();
                break;
            case CHECK_ALL_ACCOUNTS:

                break;
            case EXCHANGE_ROLE:

                break;

            case EXIT:
                exitAction();
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
        } catch (PasswordValidationException e) {
            System.out.println("Неправильный формат пароля: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Something went wrong!");
            System.out.println(e.getMessage());
        }
    }

    private void openAccountAction() {
        System.out.print("Введите название валюты: ");
        String title = scanner.nextLine();
        bankAccountService.openAccount(user.getId(), title, 0);
        System.out.println("Валюта успешно добавленна!");
    }

    private void checkBalanceAction() {
        System.out.println("Введите номер счета, по которому вы хотите увидеть баланс:");
        List<BankAccount> bankAccounts = user.getBankAccounts();
        if (bankAccounts.isEmpty()) {
            System.out.println("У вас нет банковских счетов.");
            return;
        }

        while (!scanner.hasNextInt()) {
            System.out.println("Пожалуйста, введите числовой номер счета.");
            scanner.next();
        }
        int accountNumber = scanner.nextInt();

        if (accountNumber < 1 || accountNumber > bankAccounts.size()) {
            System.out.println("Некорректный номер счета. Пожалуйста, попробуйте снова.");
            return;
        }

        BankAccount selectedAccount = bankAccounts.get(accountNumber - 1);
        if (selectedAccount != null) {

            System.out.printf("Доступный баланс на счете: %.2f %s\n", selectedAccount.getBalance(), selectedAccount.getCurrency().getCode());
        } else {
            System.out.println("Ошибка: Не удалось получить информацию по выбранному счету.");
        }
    }

    private void depositAction() {
        System.out.println("Какой счёт вы хотите пополнить?");
        BankAccount bankAccount = choseBankAccount();
        System.out.println("Сколько хотите внести денег: ");
        double amount = scanner.nextDouble();
        bankAccountService.deposit(bankAccount.getAccountId(), amount);
        System.out.println("Счёт успешно пополнен");
    }

    private void withdrawAction() {
        System.out.println("С какого счёта вы хотите вывести деньги?");
        BankAccount bankAccount = choseBankAccount();
        System.out.println("Сколько вы хотите вывести деняг?");
        double amount = scanner.nextDouble();
        bankAccountService.withdraw(bankAccount.getAccountId(), amount);
        System.out.println("Деньги успешно выведенны!");
    }


    private void exchangeCurrencyAction() {
        System.out.println("Выберите счет, с которого хотите обменять валюту:");
        BankAccount bankAccount = choseBankAccount();
        if (bankAccount == null) {
            System.out.println("Неверный выбор счета.");
            return;
        }

        System.out.println("Вы выбрали счет в валюте " + bankAccount.getCurrency().getCode() + ". Сколько вы хотите обменять?");
        double amount = scanner.nextDouble();


        System.out.println("На какую валюту вы хотите обменять? Введите код валюты:");
        scanner.nextLine();
        String targetCurrencyCode = scanner.nextLine();

        if (bankAccount.getCurrency().getCode().equalsIgnoreCase(targetCurrencyCode)) {
            System.out.println("Исходная и целевая валюты совпадают. Обмен не требуется.");
            return;
        }

        bankAccountService.exchangeCurrency(bankAccount.getAccountId(), bankAccount.getCurrency().getCode(), targetCurrencyCode, amount);
        System.out.println("Запрос на обмен валюты отправлен.");
    }

    private void printAccountOperationsAction() { // TODO добавить проверку на ноль.
        System.out.println("Выберите счёт для просмотра транзакций: ");
        BankAccount bankAccount = choseBankAccount();
        System.out.println("История Транзацкий: ");
        bankAccountService.printAccountOperation(bankAccount.getAccountId());
    }

    private void exchangeHistoryAction() {//TODO изменил!
        System.out.println("Курс валют за весь период");
        currencyService.checkAllHistory();
        System.out.println("Отсортировать валюту по коду или дате?");
        currencyService.checksAboutCodeOrDate(scanner.nextLine());

    }

    private void closeAccountAction() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Введите ID счета, который вы хотите закрыть:");
        while (!scanner.hasNextInt()) {
            System.out.println("Пожалуйста, введите корректный числовой ID счета.");
            scanner.next();
        }
        int accountId = scanner.nextInt();

        boolean isClosed = bankAccountService.closeAccount(accountId);
        if (isClosed) {
            System.out.println("Счет с ID " + accountId + " успешно закрыт.");
        } else {
            System.out.println("Счет с таким ID не найден.");
        }
    }

    private void checkAllAccountsAction() {
        System.out.println("Проверить транзакции всех аккаунтов");
        //Воздух

    }

    private void checkAllOperationsAction() {
        System.out.println("Проверить операции всех аккаунтов");
        //Воздух


    }

    private void addNewCurrency() { //TODO ТУТ ИЗМЕНИЛ
        System.out.println("Добавить новую валюту:");
        System.out.println("Введите наименование валюты");
        String addCur1 = scanner.nextLine();
        System.out.println("Введите код валюты");
        String addCur2 = scanner.nextLine();
        System.out.println("Введите текущий курс валюты");
        double addCur3 = scanner.nextDouble();
        currencyService.addNewCurrency(addCur1, addCur2, addCur3);
        System.out.println("Успешно добавлена!");
    }

    private void changesExchange() { //TODO Добавил метод, лучше который просто обновляет список вал
        currencyService.changeCurrencyRate(scanner.nextLine(), scanner.nextDouble());
        //Воздух
    }

    private void issueAdmin() {


    }

    private void exitAction() {
        System.exit(0);
    }

    private BankAccount choseBankAccount() {
        List<BankAccount> bankAccounts = user.getBankAccounts();
        if (bankAccounts.isEmpty()) {
            System.out.println("У вас нет банковских счетов.");
            return null;
        }

        for (int i = 0; i < bankAccounts.size(); i++) {
            BankAccount bankAccount = bankAccounts.get(i);
            System.out.printf("%d: %.2f %s\n", i + 1, bankAccount.getBalance(), bankAccount.getCurrency().getCode());
        }
        System.out.println("Введите номер счета:");

        int accountIndex = scanner.nextInt() - 1;

        if (accountIndex < 0 || accountIndex >= bankAccounts.size()) {
            System.out.println("Некорректный номер счета. Пожалуйста, попробуйте снова.");
            return null;
        }
        return bankAccounts.get(accountIndex);
    }


    public static void main(String[] args) {
        CurrencyRepository currencyRepository = new CurrencyRepository();
        currencyRepository.writeTo();
        System.out.println(currencyRepository.getCourseMap());
//        System.out.println(currencyRepository.getCourseMap());
        Menu menu1 = new Menu();
        menu1.run();
//        CurrencyNetworkWorker worker = new CurrencyNetworkWorker();
//        try {
//            LatestCurrencyResponse latestCurrencyResponse = worker.requestLatestCurrency();
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
    }

}





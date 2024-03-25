import service.BankAccountService;
import service.CurrencyService;
import service.OperationService;
import service.UserService;
import java.util.Scanner;

public class Menu {
    private UserService userService;
    private BankAccountService bankAccountService;
    private OperationService operationService;
    private CurrencyService currencyService;
    private Scanner scanner = new Scanner(System.in);

    public Menu(UserService userService, BankAccountService bankAccountService,
                OperationService operationService, CurrencyService currencyService) {
        this.userService = userService;
        this.bankAccountService = bankAccountService;
        this.operationService = operationService;
        this.currencyService = currencyService;
    }

    enum MenuItem {
        AUTHORIZE("Авторизация"),
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
        CHANGE_EXCHANGE_RATE("Изменение курса валют"),
        EXIT("Выход");

        private final String description;

        MenuItem(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }
    }

//    public void displayMenu() {
//        while (true) {
//            System.out.println("\nВыберите действие:");
//            for (MenuItem item : MenuItem.values()) {
//                System.out.println((item.ordinal() + 1) + ". " + item.getDescription());
//            }
//            System.out.print("Ваш выбор: ");
//            int choice = scanner.nextInt();
//            if (choice < 1 || choice > MenuItem.values().length) {
//                System.out.println("Неверный выбор, попробуйте снова.");
//                continue;
//            }
//            if (choice == MenuItem.EXIT.ordinal() + 1) break;
//            handleUserChoice(MenuItem.values()[choice - 1]);
//        }
//    }

//    private void handleUserChoice(MenuItem item) {
//        switch (item) {
//            case AUTHORIZE:
//                authorizeUser();
//                break;
//            case REGISTRATION:
//                registerUser();
//                break;
//            case OPEN_ACCOUNT:
//                openAccountAction();
//                break;
//            case CHECK_BALANCE:
//                checkBalanceAction();
//                break;
//            case DEPOSIT:
//                depositAction();
//                break;
//            case WITHDRAW:
//                withdrawAction();
//                break;
//            case EXCHANGE_CURRENCY:
//                exchangeCurrencyAction();
//                break;
//            case PRINT_ACCOUNT_OPERATION:
//                printAccountOperationsAction();
//                break;
//            case CLOSE_ACCOUNT:
//                closeAccountAction();
//                break;
//            case EXCHANGE_HISTORY:
//                exchangeHistoryAction();
//                break;
//            case ADD_NEW_CURRENCY:
//                addNewCurrencyAction();
//                break;
//            case CHECK_ALL_OPERATIONS:
//                checkAllOperationsAction();
//                break;
//            case CHECK_ALL_ACCOUNTS:
//                checkAllAccountsAction();
//                break;
//            case CHANGE_EXCHANGE_RATE:
//                changeExchangeRateAction();
//                break;
//            case EXIT:
//                System.out.println("Выход из системы.");
//                System.exit(0);
//                break;
//            default:
//                System.out.println("Данный пункт меню в процессе разработки.");
//        }
//    }

    private void authorizeUser() {
        System.out.println("Авторизация пользователя");
        // Логика авторизации
    }

    private void registerUser() {
        System.out.println("Регистрация пользователя...");
        // Логика регистрации
    }

    // Добавьте сюда методы для других действий...
}

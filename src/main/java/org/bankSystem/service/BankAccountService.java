
package org.bankSystem.service;

import org.bankSystem.model.BankAccount;
import org.bankSystem.model.Currency;
import org.bankSystem.model.Users;
import org.bankSystem.model.Operations;
import org.bankSystem.repository.BankAccountRepository;

import java.util.List;
import java.util.Optional;

public class BankAccountService {
    private BankAccountRepository bankAccountRepository;
    private UserService userService;
    private CurrencyService currencyService;
    private OperationService operationService;

    public BankAccountService(BankAccountRepository bankAccountRepository, UserService userService, CurrencyService currencyService, OperationService operationService) {
        this.bankAccountRepository = bankAccountRepository;
        this.userService = userService;
        this.currencyService = currencyService;
        this.operationService = operationService;
    }

    // Метод для открытия счета с уникальным ID
    public void openAccount(int userId, String currencyCode, double initialBalance) {
        Users user = userService.getUserById(userId);
        if (user == null) {
            System.out.println("Пользователь с ID " + userId + " не найден.");
            return;
        }

        Currency currency = currencyService.getCurrencyByCode(currencyCode);
        if (currency == null) {
            System.out.println("Валюта с кодом " + currencyCode + " не найдена.");
            return;
        }

        int accountId = generateUniqueAccountId();
        BankAccount newAccount = new BankAccount(initialBalance, accountId, user, currency);
        user.getBankAccounts().add(newAccount); // Привязываем счет к пользователю
        bankAccountRepository.addBankAccount(newAccount);
        System.out.println("Новый счет успешно создан для пользователя с ID: " + userId);
    }

    // Вспомогательный метод для генерации уникального ID счета
    private int generateUniqueAccountId() {
        int accountId = bankAccountRepository.getAllBankAccounts().size() + 1;
        while (bankAccountRepository.getBankAccountById(accountId).isPresent()) {
            accountId++;
        }
        return accountId;
    }

    public void deposit(int accountId, double amount) {
        Optional<BankAccount> account = bankAccountRepository.getBankAccountById(accountId);
        account.ifPresentOrElse(acc -> {
            acc.setBalance(acc.getBalance() + amount);
            bankAccountRepository.updateBankAccount(acc);
            int operationId = operationService.generateOperationId(); // Предполагается, что метод generateOperationId существует в OperationService
            operationService.recordOperation(new Operations(operationId, amount, acc.getCurrency().getCode(), accountId, Operations.TypeOperation.DEPOSIT));
            System.out.println("Счет пополнен на " + amount);
        }, () -> System.out.println("Счет не найден."));
    }

    public void withdraw(int accountId, double amount) {
        Optional<BankAccount> account = bankAccountRepository.getBankAccountById(accountId);
        account.ifPresentOrElse(acc -> {
            if (acc.getBalance() >= amount) {
                acc.setBalance(acc.getBalance() - amount);
                bankAccountRepository.updateBankAccount(acc);
                int operationId = operationService.generateOperationId();
                operationService.recordOperation(new Operations(operationId, -amount, acc.getCurrency().getCode(), accountId, Operations.TypeOperation.WITHDRAWAL));
                System.out.println("Со счета снято: " + amount);
            } else {
                System.out.println("Недостаточно средств на счете.");
            }
        }, () -> System.out.println("Счет не найден."));
    }

    public void exchangeCurrency(int accountId, String targetCurrencyCode, double amount) {
        Optional<BankAccount> accountOpt = bankAccountRepository.getBankAccountById(accountId);
        accountOpt.ifPresentOrElse(account -> {
            double exchangeRate = currencyService.getExchangeCourse(account.getCurrency().getCode(), targetCurrencyCode);
            double convertedAmount = amount * exchangeRate;

            if (account.getBalance() >= amount) {
                account.setBalance(account.getBalance() - amount); // Снимаем сумму в исходной валюте
                // Здесь может быть необходимо добавить сумму в целевой валюте, если учет ведется отдельно
                bankAccountRepository.updateBankAccount(account);
                int operationId = operationService.generateOperationId();
                operationService.recordOperation(new Operations(operationId, -amount, account.getCurrency().getCode(), accountId, Operations.TypeOperation.EXCHANGE));
                System.out.println("Обмен выполнен. Снято " + amount + " " + account.getCurrency().getCode() + ", добавлено " + convertedAmount + " " + targetCurrencyCode + ".");
            } else {
                System.out.println("Недостаточно средств для обмена.");
            }
        }, () -> System.out.println("Счет не найден."));
    }

    public void closeAccount(int accountId) {
        Optional<BankAccount> account = bankAccountRepository.getBankAccountById(accountId);
        account.ifPresentOrElse(acc -> {
            bankAccountRepository.deleteBankAccount(accountId);
            int operationId = operationService.generateOperationId();
            operationService.recordOperation(new Operations(operationId, 0, acc.getCurrency().getCode(), accountId, Operations.TypeOperation.CLOSE_ACCOUNT));
            System.out.println("Счет закрыт.");
        }, () -> System.out.println("Счет не найден."));
    }


    public void printAccountOperation(int accountId) {
        List<Operations> transactions = operationService.getOperationsByAccountId(accountId);
        if (transactions.isEmpty()) {
            System.out.println("Транзакций по данному счету не найдено.");
        } else {
            System.out.println("История транзакций для счета " + accountId + ":");
            transactions.forEach(System.out::println);
        }
    }

    // Метод для проверки баланса счета
    public double checkBalance(int accountId) {
        Optional<BankAccount> account = bankAccountRepository.getBankAccountById(accountId);
        return account.map(BankAccount::getBalance).orElseThrow(() -> new RuntimeException("Счет не найден."));
    }

    // Метод для перевода средств между счетами
    public void transfer(int fromAccountId, int toAccountId, double amount) {
        Optional<BankAccount> fromAccount = bankAccountRepository.getBankAccountById(fromAccountId);
        Optional<BankAccount> toAccount = bankAccountRepository.getBankAccountById(toAccountId);

        if (fromAccount.isPresent() && toAccount.isPresent() && fromAccount.get().getBalance() >= amount) {
            // Списание средств со счета отправителя
            fromAccount.get().setBalance(fromAccount.get().getBalance() - amount);
            bankAccountRepository.updateBankAccount(fromAccount.get());

            // Зачисление средств на счет получателя
            toAccount.get().setBalance(toAccount.get().getBalance() + amount);
            bankAccountRepository.updateBankAccount(toAccount.get());

            // Регистрация операций
            int operationIdFrom = operationService.generateOperationId();
            operationService.recordOperation(new Operations(operationIdFrom, -amount, fromAccount.get().getCurrency().getCode(), fromAccountId, Operations.TypeOperation.TRANSFER_OUT));

            int operationIdTo = operationService.generateOperationId();
            operationService.recordOperation(new Operations(operationIdTo, amount, toAccount.get().getCurrency().getCode(), toAccountId, Operations.TypeOperation.TRANSFER_IN));

            System.out.println("Перевод выполнен.");
        } else {
            System.out.println("Перевод невозможен. Проверьте баланс и корректность счетов.");
        }
    }
}

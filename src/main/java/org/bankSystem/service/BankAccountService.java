
package org.bankSystem.service;

import org.bankSystem.model.BankAccount;
import org.bankSystem.model.Currency;
import org.bankSystem.model.Users;
import org.bankSystem.model.Operations;
import org.bankSystem.repository.BankAccountRepository;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

public class BankAccountService {
    private BankAccountRepository bankAccountRepository;
    private UserService userService;
    private CurrencyService currencyService;
    private OperationService operationService;
    private final AtomicInteger currentId = new AtomicInteger(1);


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

        int accountId = bankAccountRepository.generateNewAccountId();
        BankAccount newAccount = new BankAccount(initialBalance, accountId, user, currency);
        user.getBankAccounts().add(newAccount); // Привязываем счет к пользователю
        bankAccountRepository.addBankAccount(newAccount);
        System.out.println("Новый счет успешно создан для пользователя с ID: " + userId);
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

    public void exchangeCurrency(int accountId, String sourceCurrencyCode, String targetCurrencyCode, double amount) {
        BankAccount sourceAccount = bankAccountRepository.getBankAccountById(accountId)
                .orElseThrow(() -> new RuntimeException("Счет не найден."));

        if (!sourceAccount.getCurrency().getCode().equals(sourceCurrencyCode)) {
            throw new RuntimeException("Валюта счета не соответствует указанной валюте для обмена.");
        }

        if (sourceAccount.getBalance() < amount) {
            throw new RuntimeException("Недостаточно средств на счете для обмена.");
        }

        double exchangeRate = currencyService.getExchangeCourse(sourceCurrencyCode, targetCurrencyCode);
//        if (exchangeRate <= 0) {
//            throw new RuntimeException("Невозможно получить курс обмена для указанных валют.");
//        }

        double convertedAmount = amount * exchangeRate;
        sourceAccount.setBalance(sourceAccount.getBalance() - amount);
        bankAccountRepository.updateBankAccount(sourceAccount);

        BankAccount targetAccount = bankAccountRepository.getBankAccountByUserIdAndCurrency(sourceAccount.getUsers().getId(), targetCurrencyCode)
                .orElseGet(() -> createNewBankAccount(sourceAccount, targetCurrencyCode, convertedAmount));

        targetAccount.setBalance(targetAccount.getBalance() + convertedAmount);
        bankAccountRepository.updateBankAccount(targetAccount);

        System.out.println(String.format("Обмен выполнен. Снято %f %s, добавлено %f %s.", amount, sourceCurrencyCode, convertedAmount, targetCurrencyCode));
    }

    private BankAccount createNewBankAccount(BankAccount sourceAccount, String targetCurrencyCode, double initialBalance) {
        Currency targetCurrency = currencyService.getCurrencyByCode(targetCurrencyCode);
        if (targetCurrency == null) {
            throw new RuntimeException("Валюта с кодом " + targetCurrencyCode + " не найдена.");
        }

        BankAccount newAccount = new BankAccount(initialBalance, bankAccountRepository.generateNewAccountId(), sourceAccount.getUsers(), targetCurrency);
        bankAccountRepository.addBankAccount(newAccount);
        return newAccount;
    }

    public boolean closeAccount(int accountId) {
        Optional<BankAccount> account = bankAccountRepository.getBankAccountById(accountId);
        return account.map(acc -> {
            bankAccountRepository.deleteBankAccount(accountId);
            int operationId = operationService.generateOperationId();
            operationService.recordOperation(new Operations(operationId, 0, acc.getCurrency().getCode(), accountId, Operations.TypeOperation.CLOSE_ACCOUNT));
            return true;
        }).orElse(false);
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

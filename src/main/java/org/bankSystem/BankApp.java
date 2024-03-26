package org.bankSystem;

import org.bankSystem.repository.CurrencyRepository;

public class BankApp {
    public static void main(String[] args) {
        CurrencyRepository currencyRepository = new CurrencyRepository();
        currencyRepository.writeTo();
        Menu menu1 = new Menu();
        menu1.run();
//
    }
}

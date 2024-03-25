package org.bankSystem.service;

import org.bankSystem.model.Course;
import org.bankSystem.model.Currency;
import org.bankSystem.network.CurrencyNetworkWorker;
import org.bankSystem.network.LatestCurrencyResponse;
import org.bankSystem.repository.CurrencyRepository;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;


public class CurrencyService {
    CurrencyRepository currencyRepository = new CurrencyRepository();


    // отображение всех доступных валют длля обмена
    public Set<Currency> getAllCurrency() {
        return currencyRepository.allCurencies();
    }

    // метод добаления новой валюты для АДМИНА
    public Currency addNewCurrency(String code, String name, double rate) {
        if (code == null || name == null || code.isEmpty() || name.isEmpty())
            throw new RuntimeException("Код валюты и наименование валюты должно быть заполнено");
        if (rate <= 0) throw new RuntimeException("Курс валюты не может быть меньше 0 ");
        ;
        return currencyRepository.addNewCurrency(name, code, rate);
    }

    // метод удаления существующей валюты для АДМИНА
    public Currency removeCurrency(String name, String code) {
        return currencyRepository.removeCurrency(name, code);
    }


    //    изменение курса валюты ДЛЯ АДМИНА
    public Map<Currency, Course> changeCurrencyRate(String code, Double newRate) {
        if (newRate <= 0) throw new RuntimeException("Не корректный курс ");
        return currencyRepository.changeCourseRate(code, newRate);
    }

    // истроия курса валют
    public void checkAllRate() {
        currencyRepository.readFromFile();
    }

    public Currency getCurrencyByCode(String code) {
        return currencyRepository.getCurrencyByCode(code);
    }


    public double getExchangeCourse(String currencyCode) {
        CurrencyNetworkWorker worker = new CurrencyNetworkWorker();
        try {
            LatestCurrencyResponse latestCurrencyResponse = worker.requestLatestCurrency();
            for (String key : latestCurrencyResponse.getRates().keySet()) {
                if (key.equals(currencyCode)) {

                    return latestCurrencyResponse.getRates().get(key);
                }

            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return 0;
    }

}













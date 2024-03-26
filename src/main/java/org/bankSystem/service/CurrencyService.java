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


    private CurrencyRepository currencyRepository = new CurrencyRepository();
    private CurrencyNetworkWorker currencyNetworkWorker = new CurrencyNetworkWorker();
    private boolean testMode = false;
    private static final double TEST_EXCHANGE_RATE = 0.85;


    // отображение всех доступных валют длля обмена
    public Set<Currency> getAllCurrency() {
        return currencyRepository.allCurencies();
    }
    public void setTestMode(boolean testMode) {
        this.testMode = testMode;
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
    public double getExchangeCourse(String sourceCurrencyCode, String targetCurrencyCode) {
        if (testMode) {
            return TEST_EXCHANGE_RATE;
        }try {
            LatestCurrencyResponse latestCurrencyResponse = currencyNetworkWorker.requestLatestCurrency();

            Map<String, Double> rates = latestCurrencyResponse.getRates();

            Double sourceRate = rates.getOrDefault(sourceCurrencyCode, 0.0);
            Double targetRate = rates.getOrDefault(targetCurrencyCode, 0.0);

            if (sourceRate == null || targetRate == null) {
                System.out.println("Не удалось получить курс обмена для одной из валют.");
                return 0;
            }

            return targetRate - sourceRate;

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return 0;
        }
    }
}






















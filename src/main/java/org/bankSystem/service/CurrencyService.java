package service;

import model.Currency;
import repository.CurrencyRepository;

import java.util.Set;

public class CurrencyService {

//    private Map<String,Currency> currencies = new HashMap<>();
//    public CurrencyService() {
//        currencies.put("USD",new Currency("US Dollar","USD"));
//    }
//    public double getExchangeCourse(String code, String targetCurrencyCode) {

    CurrencyRepository currencyRepository = new CurrencyRepository();

// метод добаления новой валюты
// метод удаления существующей валюты
//    метод всех доступных валют
//    изменение курса валюты

    public Set<Currency> getAllCurrency(){
        return currencyRepository.allCurencies();
    }


    public Currency addNewCurrency(String code , String name , double rate){
        if (code==null||name==null||code.isEmpty()|| name.isEmpty()) throw new RuntimeException("Код валюты и наименование валюты должно быть заполнено");
        if (rate<=0) throw new RuntimeException("Курс валюты не может быть меньше 0 ");;
        return currencyRepository.addNewCurrency(name,code,rate);
    }
    public Currency removeCurrency(String name,String code){

        return currencyRepository.removeCurrency(name,code);


    }

    public Currency getCurrencyByCode(String currencyCode) {
        return currencyRepository.getCurrencyByCode(currencyCode);
    }
//    public void addCurrency (Currency currency) {
//        currencies.put(currency.getCode(),currency);
//    }

}



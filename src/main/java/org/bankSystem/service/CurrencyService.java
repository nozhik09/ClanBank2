package org.bankSystem.service;

import org.bankSystem.model.Course;
import org.bankSystem.model.Currency;
import org.bankSystem.repository.CurrencyRepository;

import java.util.Map;
import java.util.Set;


public class CurrencyService {
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
    public Map<Currency, Course> changeCurrencyRate(String code , Double newRate){
        if (newRate<=0) throw new RuntimeException("Не корректный курс ");
        return currencyRepository.changeCourseRate(code,newRate);
    }

    public void checkAllRate(){
        currencyRepository.readFromFile();
    }




}













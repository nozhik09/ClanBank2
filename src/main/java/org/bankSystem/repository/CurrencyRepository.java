package org.bankSystem.repository;


import org.bankSystem.model.Course;
import org.bankSystem.model.Currency;

import java.io.*;
import java.util.*;

public class CurrencyRepository {

    private Map<Currency, Course> courseMap = new HashMap<>();
    private Set<Currency> setCurrency = new HashSet<>();
    String path = "src/repository/CurrencyChangeList.txt";
    String listOfAvailableCurrencies = "src/repository/listOfAvailableCurrencies.txt";


    public CurrencyRepository() {
        this.setCurrency = new HashSet<>();
        this.courseMap = new HashMap<>();
        allAvailableCurrencies();
        setCourseMap();
    }

    public void allAvailableCurrencies() {
        setCurrency.add(new Currency("Euro", "EUR"));
        setCurrency.add(new Currency("American Dollar", "USD"));
    }

    public void setCourseMap() {
        courseMap.put(new Currency("Euro", "EUR"), new Course(11.4));
        courseMap.put(new Currency("American Dollar", "USD"), new Course(14.4));

    }


    //    Сет всех доступных валют +
    public Set<Currency> allCurencies() {
        return setCurrency;
    }

    public Map<Currency, Course> getCourseMap() {
        return courseMap;
    }

    // Добавление новой валюты +//TODO Добавлять в TXT?
    public Currency addNewCurrency(String name, String code , Double rate) {
        Currency currency = new Currency(name, code);
        setCurrency.add(currency);
        courseMap.put(currency, new Course(rate));
        return currency;
    }


    //Удаление валюты из всех доступных
    public Currency removeCurrency(String name, String code) {
        Currency currency = new Currency(name, code);
        setCurrency.remove(currency);
        return currency;
    }


public Map<Currency, Course> changeCourseRate(String code, Double newRate) {
    Currency currency = null;
    for (Currency c : setCurrency) {
        if (c.getCode().equals(code)) {
            currency = c;
            break;
        }
    }
    Course course = courseMap.get(currency);
    course.setCourse(newRate);
    courseMap.put(currency, course);
    writeToFile(currency,course);
    return courseMap;
}






    private void writeToFile(Currency currency , Course course) {
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(path, true))) {
            bufferedWriter.write(currency.getCode()+": " + course.getCourse()+" Date: " + course.getLocalDate());
            bufferedWriter.newLine();


        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void readFromFile() {
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(path))) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void readFromFileByCurrency(String code){
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(path))) {
            String line;

            while ((line = bufferedReader.readLine()) != null) {
                if (line.contains(code))
                System.out.println(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
    public void readFromFileByDate(String data){
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(path))) {
            String line;

            while ((line = bufferedReader.readLine()) != null) {
                if (line.contains(data))
                    System.out.println(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
    public  Currency getCurrencyByCode (String code){
        Currency currency = new Currency("US Dolar",code);
        for (Currency c : setCurrency) {
            if (c.getCode().equals(code)) {
                currency = c;
         return currency;

            }
        }
        return null;

    }





}




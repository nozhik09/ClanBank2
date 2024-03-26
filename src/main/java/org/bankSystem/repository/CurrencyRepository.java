package org.bankSystem.repository;


import org.bankSystem.model.Course;
import org.bankSystem.model.Currency;
import org.bankSystem.network.CurrencyNetworkWorker;
import org.bankSystem.network.LatestCurrencyResponse;

import java.io.*;
import java.time.LocalDate;
import java.util.*;

public class CurrencyRepository {
    LatestCurrencyResponse latestCurrencyResponse = new LatestCurrencyResponse();

    private Map<String, Double> courseMap = new HashMap<>();
    private Set<Currency> setCurrency = new HashSet<>();

    String path = "src/main/java/org/bankSystem/repository/CurrencyChangeList.txt";
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
        setCurrency.add(new Currency("", "AUD"));
        setCurrency.add(new Currency("", "BGN"));
        setCurrency.add(new Currency("", "BRL"));
        setCurrency.add(new Currency("", "CAD"));
        setCurrency.add(new Currency("", "CHF"));

    }

    public void setCourseMap() {
        courseMap.put("EUR", 1.0);
        courseMap.put("USD", 1.08);
    }


    //    Сет всех доступных валют +
    public Set<Currency> allCurencies() {
        return setCurrency;
    }

    public Map<String, Double> getCourseMap() {
        return courseMap;
    }

    // Добавление новой валюты +//TODO Добавлять в TXT?
    public Currency addNewCurrency(String name, String code, Double rate) {
        Currency currency = new Currency(name, code);
        Course course = new Course(rate);
        setCurrency.add(currency);
        courseMap.put(code, rate);
        writeToFile(currency.getCode(), rate);
        return currency;
    }

    public void writeTo() {
        CurrencyNetworkWorker worker = new CurrencyNetworkWorker();
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(path, true))) {
            LatestCurrencyResponse latestCurrencyResponse = worker.requestLatestCurrency();
            for (String key : latestCurrencyResponse.getRates().keySet()) {
                bufferedWriter.write(key + " : " + latestCurrencyResponse.getRates().get(key) + " Date:" + LocalDate.now());
                bufferedWriter.newLine();
            }
            courseMap.putAll(latestCurrencyResponse.getRates());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    //Удаление валюты из всех доступных
    public Currency removeCurrency(String name, String code) {
        Currency currency = new Currency(name, code);
        courseMap.remove(code);
        return currency;
    }


    public Map<String, Double> changeCourseRate(String code, Double newRate) {
        if (!courseMap.keySet().contains(code)) {
            return null;
        }

        courseMap.put(code,newRate);
        writeToFile(code, newRate);
        return courseMap;


//        Course course = new Course(newRate);
//        course.setCourse(newRate);
//        courseMap.put(currency.getCode(), newRate);

//        return courseMap;
    }


    private void writeToFile(String code, Double d) {

        Currency currency1 = new Currency("", code);
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(path, true))) {
            bufferedWriter.write(currency1.getCode() + " : " + d + " Date:" + LocalDate.now());
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

    public void readFromFileByCurrency(String code) {
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

    public void readFromFileByDate(String data) {
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

    public Currency getCurrencyByCode(String code) {
        Currency currency = new Currency("US Dolar", code);
        for (Currency c : setCurrency) {
            if (c.getCode().equals(code)) {
                currency = c;
                return currency;

            }
        }
        return null;

//        courseMap.keySet().contains(code);




    }
}




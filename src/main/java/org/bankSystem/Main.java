package org.example;

import org.bankSystem.Menu;
import org.bankSystem.network.CurrencyNetworkWorker;
import org.bankSystem.network.LatestCurrencyResponse;

import java.util.Scanner;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {


    public static void main(String[] args) {
            Menu menu1 = new Menu();
            menu1.run();


//        CurrencyNetworkWorker worker = new CurrencyNetworkWorker();
//        try {
//            LatestCurrencyResponse latestCurrencyResponse = worker.requestLatestCurrency();
//            for (String key: latestCurrencyResponse.getRates().keySet()) {
//                System.out.printf("%s: %.4f\n", key, latestCurrencyResponse.getRates().get(key));
//            }
//
//            Scanner scanner = new Scanner(System.in);
//            System.out.println("Currency code:");
//            String code = scanner.nextLine();
//            System.out.printf("%s: %.4f\n", code, latestCurrencyResponse.getRates().get(code));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
   }
}
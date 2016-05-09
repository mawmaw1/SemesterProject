/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package services;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 *
 * @author Magnus
 */
public class AirlineConnector {

    private static String[] hostList = {"http://angularairline-plaul.rhcloud.com/api/flightinfo/", "http://magnustest.cloudapp.net/api/api/flights/"
    };

    
    public List<Future<String>> ConnectToAirlinesFromDatePersons(final String airport, final String date, final int passengers) throws IOException, InterruptedException, ExecutionException {

        List<Future<String>> list = new ArrayList<>();

        ExecutorService executor = Executors.newFixedThreadPool(10);

        for (int i = 0; i < hostList.length; i++) {

            final String url = hostList[i];

            Callable<String> task = new Callable<String>() {
                @Override
                public String call() throws Exception {
                    return readUrl(url + airport + "/" + date + "/" + passengers);
                }
            };
            list.add(executor.submit(task));

        }

        executor.shutdown();

        return list;
    }
    public List<Future<String>> ConnectToAirlinesFromToDatePersons(final String from, final String to, final String date, final int passengers) throws IOException, InterruptedException, ExecutionException {

        List<Future<String>> list = new ArrayList<>();

        ExecutorService executor = Executors.newFixedThreadPool(10);

        for (int i = 0; i < hostList.length; i++) {

            final String url = hostList[i];

            Callable<String> task = new Callable<String>() {
                @Override
                public String call() throws Exception {
                    return readUrl(url + from + "/" + to + "/" + date + "/" + passengers);
                }
            };
            list.add(executor.submit(task));

        }

        executor.shutdown();

        return list;
    }
    

    private String readUrl(String urlString) throws Exception {
        BufferedReader reader = null;
        try {
            URL url = new URL(urlString);
            reader = new BufferedReader(new InputStreamReader(url.openStream()));
            StringBuffer buffer = new StringBuffer();
            int read;
            char[] chars = new char[1024];
            while ((read = reader.read(chars)) != -1) {
                buffer.append(chars, 0, read);
            }

            return buffer.toString();
        } finally {
            if (reader != null) {
                reader.close();
            }
        }
        

    }

}

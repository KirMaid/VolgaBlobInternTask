package org.example;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.opensearch.plugins.ActionPlugin;
import org.opensearch.plugins.Plugin;

import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class TestPlugin extends Plugin implements ActionPlugin {
    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Usage: java Main <path to JSON file>");
            return;
        }

        String filePath = args[0];
        Gson gson = new Gson();

        try (FileReader reader = new FileReader(filePath)) {
            Type listType = new TypeToken<ArrayList<Ups>>(){}.getType();
            List<Ups> statuses = gson.fromJson(reader, listType);

            double avgRunTime = calculateAverageRunTime(statuses);
            int maxOutputVoltage = findMaxOutputVoltage(statuses);
            List<String> hosts = extractHosts(statuses);

            // Вывод результатов в консоль
            System.out.println("Average Run Time Remaining: " + avgRunTime);
            System.out.println("Max Output Voltage: " + maxOutputVoltage);
            System.out.println("Hosts: " + hosts);
        } catch (IOException e) {
            System.err.println("Error reading the JSON file: " + e.getMessage());
        }
    }

    /**
     *
     * @param statuses
     * @return
     */
    private static double calculateAverageRunTime(List<Ups> statuses) {
        return statuses.stream()
                .mapToInt(Ups::getUpsAdvBatteryRunTimeRemaining)
                .average()
                .orElse(0.0);
    }

    /**
     *
     * @param statuses
     * @return
     */
    private static int findMaxOutputVoltage(List<Ups> statuses) {
        return statuses.stream()
                .mapToInt(Ups::getUpsAdvOutputVoltage)
                .max()
                .orElse(0);
    }

    /**
     *
     * @param statuses
     * @return
     */
    private static List<String> extractHosts(List<Ups> statuses) {
        List<String> hosts = new ArrayList<>();
        for (Ups status : statuses) {
            hosts.add(status.getHost());
        }
        return hosts;
    }
}
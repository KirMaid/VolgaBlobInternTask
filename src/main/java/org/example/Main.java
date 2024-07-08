package org.example;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;

import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
class Test {
    @SerializedName("ups_adv_output_load")
    private int upsAdvOutputLoad;

    @SerializedName("ups_adv_battery_temperature")
    private int upsAdvBatteryTemperature;

    @SerializedName("@timestamp")
    private String timestamp;

    private String host;

    @SerializedName("ups_adv_battery_run_time_remaining")
    private int upsAdvBatteryRunTimeRemaining;

    @SerializedName("ups_adv_output_voltage")
    private int upsAdvOutputVoltage;

    // Геттеры и сеттеры
    public int getUpsAdvOutputLoad() {
        return upsAdvOutputLoad;
    }

    public void setUpsAdvOutputLoad(int upsAdvOutputLoad) {
        this.upsAdvOutputLoad = upsAdvOutputLoad;
    }

    public int getUpsAdvBatteryTemperature() {
        return upsAdvBatteryTemperature;
    }

    public void setUpsAdvBatteryTemperature(int upsAdvBatteryTemperature) {
        this.upsAdvBatteryTemperature = upsAdvBatteryTemperature;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getUpsAdvBatteryRunTimeRemaining() {
        return upsAdvBatteryRunTimeRemaining;
    }

    public void setUpsAdvBatteryRunTimeRemaining(int upsAdvBatteryRunTimeRemaining) {
        this.upsAdvBatteryRunTimeRemaining = upsAdvBatteryRunTimeRemaining;
    }

    public int getUpsAdvOutputVoltage() {
        return upsAdvOutputVoltage;
    }

    public void setUpsAdvOutputVoltage(int upsAdvOutputVoltage) {
        this.upsAdvOutputVoltage = upsAdvOutputVoltage;
    }
}

public class Main {
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
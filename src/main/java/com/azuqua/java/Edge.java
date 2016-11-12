package com.azuqua.java;

import com.azuqua.java.callbacks.AsyncRequest;
import com.azuqua.java.callbacks.OrgFLOsRequest;
import com.azuqua.java.models.AzuquaError;
import com.azuqua.java.models.Data;
import com.azuqua.java.models.FLO;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.zeromq.ZMQ;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * Created by SASi on 26-Aug-16.
 */
public class Edge {

    private static Scanner scanner;
    private static String accessKey, accessSecret;
    private static Azuqua azuqua;
    private static List<FLO> floList = new ArrayList<>();
    private static String apiHost = "", apiProtocol = "";
    private static int apiPort;
    private static long orgId;
    private static List<Double> nQueue = new ArrayList<>();
    private static FLO activeFLO;

    public static void main(String[] args) {

        JsonParser parser = new JsonParser();
        try {
            JsonObject jsonObject = (JsonObject) parser.parse(new FileReader("azuqua.config"));

            JsonObject configObject = jsonObject.getAsJsonObject("config");
            JsonObject accountObject = configObject.getAsJsonObject("account");

            accessKey = accountObject.get("access_key").getAsString();
            accessSecret = accountObject.get("access_secret").getAsString();
            orgId = accountObject.get("org_id").getAsLong();

            if (configObject.has("api")) {
                JsonObject apiObject = configObject.getAsJsonObject("api");
                apiHost = apiObject.get("host").getAsString();
                apiProtocol = apiObject.get("protocol").getAsString();
                apiPort = apiObject.get("port").getAsInt();
            }

        } catch (FileNotFoundException e) {
            System.out.println("Missing 'azuqua.config' file in root directory (" + System.getProperty("user.dir") + ")");
            System.exit(0);
        }

        scanner = new Scanner(System.in);

//        readConfig();
        init();
        getFLOs();

    }

    private static void getFLOs() {
        if (azuqua == null)
            return;

        System.out.println("\n\nGetting FLO list. Please wait.....");

        azuqua.getFLOs(orgId, new OrgFLOsRequest() {
            @Override
            public void onResponse(List<FLO> flos) {
                System.out.println("Done.\n\n");

                if (flos.isEmpty()) {
                    handleEmptyFLOList();
                } else {
                    floList.addAll(flos.stream().filter(FLO::isActive).collect(Collectors.toList()));
                    activeFLO = floList.get(0);
                    getFLOInputs();
                }
            }

            @Override
            public void onError(AzuquaError error) {
                System.out.println("\n\nFailed to get FLO list.");
                System.out.println("\nError: " + error.getErrorMessage() + "\n\n");
            }
        });
    }

    private static void handleEmptyFLOList() {
        System.out.println("\n\nNo FLOs found user account.");
        boolean refreshList = false;

        while (!refreshList) {
            System.out.println("\nR. Reload FLO list\nQ. Quit");
            System.out.print("Choose an option: ");
            char option = scanner.next().charAt(0);


            refreshList = option == 'r' || option == 'R';

            if (option == 'q' || option == 'Q') System.exit(0);

//            System.out.println("\nInvalid Option");
        }

        getFLOs();
    }

    private static void generateFLOMenu() {

        System.out.println(" -----------------------------------------------------------------------");
        System.out.println("|\t\t\t\t\t\t\t\t\t|");
        System.out.println("|\t\t\t\tFLO LIST\t\t\t\t|");
        System.out.println("|\t\t\t\t\t\t\t\t\t|");
        System.out.println(" -----------------------------------------------------------------------");
        System.out.println("\n\n");

        for (int i = 0; i < floList.size(); i++) {
            System.out.println("\t" + (i + 1) + ". " + floList.get(i).getName());
        }

//        System.out.println("<---------------------------------------------------------------------->");

        while (true) {
            System.out.print("\n\nEnter option to browse FLO: ");
            int option = scanner.nextInt();

            if (option > floList.size()) {
                System.out.println("\nPlease enter a valid option");
                System.out.println("Value must be in between 0 to " + floList.size());
            } else {
                activeFLO = floList.get(option - 1);
                getFLOInputs();
                break;
            }
        }
    }

    private static void runFLO(String frequency, String baseValue, String units, String filter) {


        Double finalBaseValue = Double.parseDouble(baseValue);
        Long finalFrequency = Long.parseLong(frequency);

        if (finalFrequency <= 0) {
            System.out.println("Frequency Value must be greater than 0 milliseconds.");
            return;
        }

        ZMQ.Context zmqContext = ZMQ.context(1);
        System.out.println("\n\nCollecting updates from weather server");
        ZMQ.Socket subscriberSocket = zmqContext.socket(2);
        subscriberSocket.connect("tcp://localhost:5555");

        assert (filter != null);
        subscriberSocket.subscribe("azuqua".getBytes());
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(new AzuquaEdge(), 0, finalFrequency, TimeUnit.MILLISECONDS);

        do {
            String string = subscriberSocket.recvStr(0).trim();
            StringTokenizer sscanf = new StringTokenizer(string, " ");
            sscanf.nextToken();
            double temperature = Integer.valueOf(sscanf.nextToken());
            if (units.equalsIgnoreCase("F")) {
                temperature = (temperature * 9 / 5.0) + 32;
            }
            switch (filter.toLowerCase()) {
                case "=": {
                    if (temperature != finalBaseValue) break;
                    nQueue.add(temperature);
                    break;
                }
                case ">": {
                    if (temperature <= finalBaseValue) break;
                    nQueue.add(temperature);
                    break;
                }
                case "<": {
                    if (temperature >= finalBaseValue) break;
                    nQueue.add(temperature);
                    break;
                }
                case "<=": {
                    if (temperature > finalBaseValue) break;
                    nQueue.add(temperature);
                    break;
                }
                case ">=": {
                    if (temperature < finalBaseValue) break;
                    nQueue.add(temperature);
                }
            }
        } while (true);

    }

    private static void init() {

        if (accessKey.isEmpty() || accessSecret.isEmpty()) {
            System.out.println("\n\nNo access key or access secret found in 'azuqua.config' file.");
            return;
        }

        if (!apiHost.isEmpty() && !apiProtocol.isEmpty())
            azuqua = new Azuqua(accessKey, accessSecret, apiProtocol, apiHost, apiPort);
        else
            azuqua = new Azuqua(accessKey, accessSecret);
    }

    private static void getFLOInputs() {

        azuqua.getFLOInputs(activeFLO.getAlias(), new AsyncRequest() {
            @Override
            public void onResponse(String response) {

                String frequency = null;
                String baseValue = null;
                String units = null;
                String filter = null;


                JsonParser jsonParser = new JsonParser();
                JsonObject jsonObject = jsonParser.parse(response).getAsJsonObject();
                JsonArray jsonArray = jsonObject.get("data").getAsJsonArray();


                for (int i = 0; i < jsonArray.size(); i++) {
                    JsonObject jsonObjectData = jsonArray.get(i).getAsJsonObject();

                    if (jsonObjectData.get("defaultValue").isJsonObject())
                        continue;

                    Data data = new Gson().fromJson(jsonObjectData, Data.class);

                    switch (data.getKey().toLowerCase()) {
                        case "frequency":
                            frequency = data.getDefaultValue();
                            break;
                        case "basevalue":
                            baseValue = data.getDefaultValue();
                            break;
                        case "units":
                            units = data.getDefaultValue();
                            break;
                        case "filter":
                            filter = data.getDefaultValue();
                            break;
                    }
                }
                runFLO(frequency, baseValue, units, filter);
            }

            @Override
            public void onError(AzuquaError error) {
                System.out.println(error.getErrorMessage());
            }
        });
    }

    private static class AzuquaEdge implements Runnable {


        @Override
        public void run() {
            System.out.println("Processed Data Queue Size : " + nQueue.size());
            int i = 0;
            while (i < nQueue.size()) {
                final int position = i++;
                Double temperature = (Double) nQueue.get(position);
                HashMap<String, String> shapes = new HashMap<String, String>();
                shapes.put("Temperature", "" + temperature);
                HashMap<String, HashMap<String, String>> shapesGroup = new HashMap<String, HashMap<String, String>>();
                shapesGroup.put("Data", shapes);
                azuqua.invokeFLO(activeFLO.getAlias(), new Gson().toJson(shapesGroup), new AsyncRequest() {

                    @Override
                    public void onResponse(String response) {
                        System.out.println("FLO ran Successfully. " + response + "\n\n");
                        nQueue.remove(position);
                    }

                    @Override
                    public void onError(AzuquaError error) {
                        System.out.println("FLO failed to run. " + error.getErrorMessage() + "\n\n");
                    }
                });
            }
        }
    }
}

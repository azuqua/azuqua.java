package com.azuqua.java;

import azuqua.flo.builder.Flo;
import azuqua.flo.builder.FloBuilder;
import azuqua.flo.builder.Method;
import azuqua.flo.builder.exceptions.BadPublisherRequestException;
import azuqua.flo.builder.exceptions.FloNotFoundException;
import azuqua.flo.builder.exceptions.NamespaceNotFoundException;
import azuqua.flo.builder.exceptions.SignDataException;
import azuqua.flo.builder.models.Input;
import azuqua.flo.builder.models.Output;
import com.azuqua.java.callbacks.AsyncRequest;
import com.azuqua.java.callbacks.OrgFLOsRequest;
import com.azuqua.java.models.AzuquaError;
import com.azuqua.java.models.FLO;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.concurrent.TimedSemaphore;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

/**
 * Created by SASi on 26-Aug-16.
 */
public class Edge {

    static Scanner scanner;
    private static String accessKey, accessSecret;
    private static Azuqua azuqua;
    private static FloBuilder floBuilder;
    private static List<FLO> floList = new ArrayList<>();
    private static String apiHost = "", apiProtocol = "", publisherHost = "", publisherProtocol = "";
    private static int apiPort, publisherPort;

    public static void main(String[] args) {

        JsonParser parser = new JsonParser();
        try {
            JsonObject jsonObject = (JsonObject) parser.parse(new FileReader("azuqua.config"));

            JsonObject configObject = jsonObject.getAsJsonObject("config");
            JsonObject accountObject = configObject.getAsJsonObject("account");

            accessKey = accountObject.get("access_key").getAsString();
            accessSecret = accountObject.get("access_secret").getAsString();

            if (configObject.has("api")) {
                JsonObject apiObject = configObject.getAsJsonObject("api");
                apiHost = apiObject.get("host").getAsString();
                apiProtocol = apiObject.get("protocol").getAsString();
                apiPort = apiObject.get("port").getAsInt();
            }

            if (configObject.has("publisher")) {
                JsonObject publisherObject = configObject.getAsJsonObject("publisher");
                publisherHost = publisherObject.get("host").getAsString();
                publisherProtocol = publisherObject.get("protocol").getAsString();
                publisherPort = publisherObject.get("port").getAsInt();
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

        azuqua.getFLOs(new OrgFLOsRequest() {
            @Override
            public void onResponse(List<FLO> flos) {
                System.out.println("Done.\n\n");

                if (flos.size() == 0) {
                    handleEmptyFLOList();
                } else {
                    floList.addAll(flos.stream().filter(FLO::isActive).collect(Collectors.toList()));
                    //generateFLOMenu();
                    try {
                        runFLO(floList.get(0));
                    } catch (BadPublisherRequestException e) {
                        e.printStackTrace();
                    }
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
                try {
                    runFLO(floList.get(option - 1));
                } catch (BadPublisherRequestException e) {
                    e.printStackTrace();
                }
                break;
            }
        }
    }

    private static void runFLO(FLO flo) throws BadPublisherRequestException {

        int floID = flo.getId();

        String filter = null;
        long freequency = 0;
        double baseValue = 0;
        String units = "C";

        try {
            Flo _flo = floBuilder.getFloInstance("" + floID);

            List<Method> methodList = _flo.methods();
            Method method = methodList.get(0);

            Map<String, Input> inputs = method.inputs();
            Set<String> inputKeys = inputs.keySet();

            for (String key : inputKeys) {

                String value = StringUtils.substringsBetween(inputs.get(key).getDefaultValue().toString(), "\"", "\"")[0];

                switch (inputs.get(key).getKey().toLowerCase()) {
                    case "filter":
                        filter = value;
                        break;
                    case "basevalue":
                        baseValue = Double.parseDouble(value);
                        break;
                    case "frequency":
                        freequency = Long.parseLong(value);
                        break;
                    case "units":
                        units = value;
                        break;
                }
            }

//            Map<String, Output> outputs = method.outputs();
//            Set<String> outputKeys = outputs.keySet();
//
//            for (String key : outputKeys) {
//                Output output = outputs.get(key);
//                if (!output.getGroup().equalsIgnoreCase("context"))
//                    System.out.println("\t" + output.getKey() + " \t " + output.getGroup());
//            }

        } catch (SignDataException | IOException | FloNotFoundException | NamespaceNotFoundException e) {
            e.printStackTrace();
        }


        final String finalUnits = units;
        final String finalFilter = filter;
        final double finalBaseValue = baseValue;

        Runnable runnable = () -> {

            int min = 0;
            int max = 50;

            Random random = new Random();
            double randomNum = random.nextInt((max - min) + 1) + min;

            if (finalUnits.equalsIgnoreCase("F")) {
                randomNum = randomNum * 1.8 + 32;
            }

//            System.out.println("Generated Value \t\t Base Value");
//            System.out.println(randomNum + "\t\t\t\t\t\t\t" + finalBaseValue);

            assert finalFilter != null;
            switch (finalFilter.toLowerCase()) {
                case "=":
                    if (randomNum != finalBaseValue) {
                        return;
                    }
                    break;
                case ">":
                    if (randomNum < finalBaseValue) {
                        return;
                    }
                    break;
                case "<":
                    if (randomNum < finalBaseValue) {
                        return;
                    }
                    break;
                case "<=":
                    if (randomNum >= finalBaseValue) {
                        return;
                    }
                    break;
                case ">=":
                    if (randomNum <= finalBaseValue) {
                        return;
                    }
                    break;
            }


            Map<String, String> shapes = new HashMap<>();
            shapes.put("Temperature", "");

            Map<String, Map> shapesGroup = new HashMap<>();
            shapesGroup.put("Temperature", shapes);

            azuqua.runFLO(flo.getAlias(), new Gson().toJson(shapesGroup), new AsyncRequest() {
                @Override
                public void onResponse(String response) {
                    System.out.println("FLO ran Successfully. " + response + "\n\n");
                }

                @Override
                public void onError(AzuquaError error) {
                    System.out.println("FLO failed to run. " + error.getErrorMessage() + "\n\n");
                }
            });
        };

        final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(runnable, 0, freequency, TimeUnit.MILLISECONDS);

//        final ScheduledFuture<?> executionHandle = scheduler.scheduleAtFixedRate(runnable, 10, freequency, TimeUnit.MILLISECONDS);
//        scheduler.schedule((Runnable) () -> executionHandle.cancel(true), 6 * freequency, TimeUnit.MILLISECONDS);

        System.out.println("\n\n" + flo.getName() + " started Monitoring Temperature \n");
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

        if (!publisherHost.isEmpty() && !publisherProtocol.isEmpty())
            floBuilder = new FloBuilder(accessKey, accessSecret, publisherHost, publisherProtocol, publisherPort);
        else
            floBuilder = new FloBuilder(accessKey, accessSecret);
    }

    private static void readConfig() {
        System.out.println("\nEnter Access Key : ");
        accessKey = scanner.nextLine();
        System.out.println("\nEnter Access Secret : ");
        accessSecret = scanner.nextLine();
    }
}

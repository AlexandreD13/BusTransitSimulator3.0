package Entities;

import Entities.Sockets.StopClient;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class Passenger implements Runnable {
    private final int id;
    private final int tripId;
    private final StopClient destinationStop;
    private final String name;

    public Passenger(int id, int tripId, StopClient destinationStop) {
        this.id = id;
        this.tripId = tripId;
        this.destinationStop = destinationStop;
        this.name = generateName();
    }

    @Override
    public void run() {

    }

    private static String generateName() {
        StringBuilder nameBuilder = new StringBuilder();
        findFirstName(nameBuilder);
        findLastName(nameBuilder);

        return nameBuilder.toString();
    }

    private static void findFirstName(StringBuilder nameBuilder) {
        ArrayList<String> first_names = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader("./src/input/russian_first_names.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                first_names.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (!first_names.isEmpty()) {
            Random random = new Random();
            int index = random.nextInt(first_names.size());
            nameBuilder.append(first_names.get(index)).append(" ");
        } else {
            nameBuilder.append("John").append(" ");
        }
    }

    private static void findLastName(StringBuilder nameBuilder) {
        ArrayList<String> last_names = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader("./src/input/russian_last_names.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                last_names.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (!last_names.isEmpty()) {
            Random random = new Random();
            int index = random.nextInt(last_names.size());
            nameBuilder.append(last_names.get(index));
        } else {
            nameBuilder.append("Doe");
        }
    }

    public int getId() {
        return id;
    }

    public int getTripId() {
        return tripId;
    }

    public String getName() {
        return name;
    }
}
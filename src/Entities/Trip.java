package Entities;

import Entities.Sockets.BusClient;
import Entities.Sockets.StopClient;

import java.util.ArrayList;

public class Trip implements Runnable {
    private final int id;
    private final ArrayList<BusClient> busClients;
    private final ArrayList<StopClient> stopClients;

    public Trip(int id, ArrayList<BusClient> busClients, ArrayList<StopClient> stopClients) {
        this.id = id;
        this.busClients = busClients;
        this.stopClients = stopClients;
    }

    @Override
    public void run() {

    }

    public ArrayList<StopClient> getStopClients() {
        return stopClients;
    }

    public int getId() {
        return id;
    }

    public ArrayList<BusClient> getBusClients() {
        return busClients;
    }

    public int getPassengerCount() {
        int count = 0;

        for (BusClient busClient: busClients) {
            count += busClient.getPassengers().size();
        }

        for (StopClient stopClient: stopClients) {
            for (Passenger passenger: stopClient.getPassengers()) {
                if (passenger.getTripId() == id) {
                    count++;
                }
            }        }

        return count;
    }
}
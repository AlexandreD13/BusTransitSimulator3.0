package Entities;

import Entities.Sockets.StopClient;

import java.util.ArrayList;

public class Display {
    public static void showHeader(String headerText) {
        int lowerDelimiter = (int) Math.floor((double) (100 - 4 - headerText.length()) / 2);
        int upperDelimiter = (int) Math.ceil((double) (100 - 4 - headerText.length()) / 2);
        System.out.println("\n|" + "=".repeat(lowerDelimiter) + " " + headerText + " " + "=".repeat(upperDelimiter) + "|\n");
    }

    public static void showInNetwork(String firstItem, String secondItem) {
        System.out.println(
                "Total passengers in Network from " + firstItem + ": " + secondItem
        );
    }

    public static void showTotalInNetwork(int total) {
        System.out.println(
                "\nTotal passengers in Network: " + total + " / " + Globals.MAX_PASSENGERS
        );
    }

    public static void showPassengersAtStops(ArrayList<StopClient> stopClients) {
        showHeader("Show Passengers at each Stop");

        var total = 0;
        for (StopClient stopClient : stopClients) {
            showInNetwork(String.format("Stop " + stopClient.getId()), String.valueOf(stopClient.getPassengers().size()));
            total += stopClient.getPassengers().size();
        }

        showTotalInNetwork(total);
    }

    public static void showPassengersInBuses(ArrayList<Trip> trips) {
        showHeader("Show Passengers in each Bus");

        var total = 0;
        for (Trip trip : trips) {
            for (int busIndex = 0; busIndex < trip.getBusClients().size(); busIndex++) {
                int count = trip.getBusClients().get(busIndex).getPassengers().size();
                showInNetwork(String.format("Bus " + trip.getBusClients().get(busIndex).getBusId()), String.valueOf(count));
                total += count;
            }
        }

        showTotalInNetwork(total);
    }

    public static void showPassengersInTrips(ArrayList<Trip> trips) {
        showHeader("Show Passengers in each Trip");

        var total = 0;
        for (Trip trip : trips) {
            int passengerCount = trip.getPassengerCount();

            showInNetwork(String.format("Trip " + trip.getId()), String.valueOf(passengerCount));
            total += passengerCount;
        }

        showTotalInNetwork(total);
    }
}
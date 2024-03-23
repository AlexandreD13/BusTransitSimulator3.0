package Entities;

public class Display {
//    public static void showHeader(String headerText) {
//        int lowerDelimiter = (int) Math.floor((double) (100 - 4 - headerText.length()) / 2);
//        int upperDelimiter = (int) Math.ceil((double) (100 - 4 - headerText.length()) / 2);
//        System.out.println("\n|" + "=".repeat(lowerDelimiter) + " " + headerText + " " + "=".repeat(upperDelimiter) + "|\n");
//    }
//
//    public static void showInNetwork(String firstItem, String secondItem) {
//        System.out.println(
//                "Total passengers in Network from " + firstItem + ": " + secondItem
//        );
//    }
//
//    public static void showTotalInNetwork(int total) {
//        System.out.println(
//                "\nTotal passengers in Network: " + total + " / " + Globals.MAX_PASSENGERS
//        );
//    }
//
//    public static void showStopsInTrips(ArrayList<Trip> trips) {
//        showHeader("Show Trips Stop List");
//
//        for (Trip trip : trips) {
//            StringBuilder stopsStringBuilder = new StringBuilder();
//            stopsStringBuilder.append("Trip ").append(trip.getTripId() + 1).append(":\n");
//
//            for (int stopIndex = 0; stopIndex < trip.getStopList().size(); stopIndex++) {
//                Stop stop = (Stop) trip.getStopList().toArray()[stopIndex];
//                if (stopIndex == 0) {
//                    stopsStringBuilder.append("Stop ").append(stop.getStopId()).append(" ");
//                } else {
//                    stopsStringBuilder.append("- Stop ").append(stop.getStopId()).append(" ");
//                }
//            }
//
//            System.out.println("\n" + stopsStringBuilder.toString().trim());
//        }
//    }
//
//    public static void showPassengersAtStops(ArrayList<Stop> stops) {
//        showHeader("Show Passengers at each Stop");
//
//        var total = 0;
//        for (Stop stop : stops) {
//            showInNetwork(String.format("Stop " + stop.getStopId()), String.valueOf(stop.getPassengers().size()));
//            total += stop.getPassengers().size();
//        }
//
//        showTotalInNetwork(total);
//    }
//
//    public static void showPassengersInBuses(ArrayList<Trip> trips) {
//        showHeader("Show Passengers in each Bus");
//
//        var total = 0;
//        for (Trip trip : trips) {
//            for (int busIndex = 0; busIndex < trip.getBusList().size(); busIndex++) {
//                int count = trip.getBusList().get(busIndex).getPassengers().size();
//                showInNetwork(String.format("Bus " + trip.getBusList().get(busIndex).getBusId()), String.valueOf(count));
//                total += count;
//            }
//        }
//
//        showTotalInNetwork(total);
//    }
//
//    public static void showPassengersInTrips(ArrayList<Trip> trips) {
//        showHeader("Show Passengers in each Trip");
//
//        var total = 0;
//        for (Trip trip : trips) {
//            showInNetwork(String.format("Trip " + trip.getTripId()), String.valueOf(trip.getPassengerCount()));
//            total += trip.getPassengerCount();
//        }
//
//        showTotalInNetwork(total);
//    }
}
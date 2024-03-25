import Entities.Display;
import Entities.Passenger;
import Entities.Sockets.BusClient;
import Entities.Globals;
import Entities.Sockets.StopClient;
import Entities.Sockets.TransitHubServer;
import Entities.Trip;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class Main {
    private static ArrayList<BusClient> busClients = new ArrayList<>();
    private static ArrayList<StopClient> stopClients = new ArrayList<>();
    private static ArrayList<Trip> trips = new ArrayList<>();
    private static ArrayList<Passenger> passengers = new ArrayList<>();

    public static void main(String[] args) throws InterruptedException, IOException {
        Globals.setArguments(args);

        TransitHubServer hubServer = new TransitHubServer();

        createStopClients();
        createTrips();
        distributePassengersToInitialStops();

        if (Globals.arguments.verbose()) {
            Display.showPassengersAtStops(stopClients);
            Display.showPassengersInBuses(trips);
            Display.showPassengersInTrips(trips);
        }

        Display.showHeader("Starting Bus runs");
        hubServer.broadcastMessage(Globals.SERVER_REQUEST + Globals.START_RUN);

        int total = getTotalPassengersInNetwork();
        while (total != 0) {
            total -= 10;
            System.out.println(total);
            // total = getTotalPassengersInNetwork();
        }

        Display.showHeader("Shutting down server");
        hubServer.shutdown();

        // String fichierZipDestination = "desa2341_remise_projet2_IFT630_H2024.zip";
        // MakeZipFile.creerFichierZip(fichierZipDestination);
    }

    private static void createStopClients() throws InterruptedException {
        Display.showHeader("Creating Stops");

        for (int index = 0; index < Globals.NB_STOPS; index++) {
            StopClient stopClient = new StopClient(index + 1);
            stopClient.startStopThread();
            stopClients.add(stopClient);
        }

        Thread.sleep(3000);
    }

    private static void createTrips() throws InterruptedException {
        Display.showHeader("Creating Trips");

        for (int index = 0; index < Globals.NB_TRIPS; index++) {
            // Buses
            BusClient bus1 = new BusClient(1, index + 1);
            bus1.startBusThread();
            busClients.add(bus1);

            BusClient bus2 = new BusClient(2, index + 1);
            bus2.startBusThread();
            busClients.add(bus2);

            ArrayList<BusClient> temp = new ArrayList<>();
            temp.add(bus1);
            temp.add(bus2);

            // Stops
            ArrayList<StopClient> stops = new ArrayList<>();
            ArrayList<Integer> uniqueStops = generateRandomUniqueStops();

            for (int stopIndex = 0; stopIndex < Globals.MAX_STOPS_PER_TRIP; stopIndex++) {
                stops.add(stopClients.get(uniqueStops.get(stopIndex) - 1));
            }

            trips.add(new Trip(index + 1, temp, stops));
        }

        Thread.sleep(3000);
    }

    private static void distributePassengersToInitialStops() {
        Display.showHeader("Distributing Passengers in Network");

        int nb_passenger = 0;
        while (nb_passenger < Globals.MAX_PASSENGERS) {
            PassengerInitialStopsAndTrip passengerStopsAndTrip = findPassengerInitialStopsAndTrip();
            Passenger passenger = new Passenger(
                    nb_passenger + 1,
                    trips.get(passengerStopsAndTrip.tripIndex).getId(),
                    passengerStopsAndTrip.destinationStop
            );

            stopClients.get(passengerStopsAndTrip.initialStop.getId() - 1).addPassenger(passenger);

            if (stopClients.get(passengerStopsAndTrip.initialStop.getId() - 1).getPassengers().contains(passenger)) {
                nb_passenger++;
                passengers.add(passenger);
            }
        }
    }

    private static PassengerInitialStopsAndTrip findPassengerInitialStopsAndTrip() {
        Random random = new Random();
        int randomTripIndex = random.nextInt(trips.size());
        int randomInitialStopIndex = random.nextInt(trips.get(randomTripIndex).getStopClients().size());
        int randomDestinationStopIndex = random.nextInt(trips.get(randomTripIndex).getStopClients().size());

        while (randomInitialStopIndex == randomDestinationStopIndex &&
                stopClients.get(randomInitialStopIndex).getPassengers().size() < Globals.MAX_PASSENGERS_PER_STOP &&
                stopClients.get(randomDestinationStopIndex).getPassengers().size() < Globals.MAX_PASSENGERS_PER_STOP) {
            randomInitialStopIndex = random.nextInt(trips.get(randomTripIndex).getStopClients().size());
            randomDestinationStopIndex = random.nextInt(trips.get(randomTripIndex).getStopClients().size());
        }

        StopClient initialStop = (StopClient) trips.get(randomTripIndex).getStopClients().toArray()[randomInitialStopIndex];
        StopClient destinationStop = (StopClient) trips.get(randomTripIndex).getStopClients().toArray()[randomDestinationStopIndex];

        return new PassengerInitialStopsAndTrip(randomTripIndex, initialStop, destinationStop);
    }

    private static ArrayList<Integer> generateRandomUniqueStops() {
        Random random = new Random();
        ArrayList<Integer> stopsId = new ArrayList<>();
        ArrayList<Boolean> used = new ArrayList<>();
        for (int i = 0; i < Globals.NB_STOPS; i++) {
            used.add(false);
        }

        for (int index = 0; index < Globals.MAX_STOPS_PER_TRIP; index++) {
            int randomNumber;
            stopsId.add(-1);
            do {
                randomNumber = random.nextInt(Globals.NB_STOPS) + 1;
            } while (used.get(randomNumber - 1));

            stopsId.set(index, randomNumber);
            used.set(randomNumber - 1, true);
        }

        return stopsId;
    }

    private static int getTotalPassengersInNetwork() {
        int total = 0;

        for (Trip t: trips) {
            total += t.getPassengerCount();
        }

        return total;
    }

    private record PassengerInitialStopsAndTrip(int tripIndex, StopClient initialStop, StopClient destinationStop) { }
}
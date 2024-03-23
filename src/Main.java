import Entities.Sockets.BusClient;
import Entities.Globals;
import Entities.Sockets.TransitHubServer;

public class Main {
    public static void main(String[] args) {
        Globals.setArguments(args);

        // TODO: The hub is probably the best location to create the trips, stops and buses
        startHub();

        // TODO: Figure out where would be the best location to initialize buses
        //  and don't forget to remove this code
        for (int index = 0; index < Globals.NB_TRIPS; index++) {
            startBuses(index + 1);
        }

        // String fichierZipDestination = "desa2341_remise_projet2_IFT630_H2024.zip";
        // MakeZipFile.creerFichierZip(fichierZipDestination);
    }

    private static void startHub() {
        Thread hubThread = new Thread(() -> {
            try {
                TransitHubServer hubServer = new TransitHubServer();
                hubServer.run();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        hubThread.start();
    }

    private static void startBuses(int id) {
        Thread busThread = new Thread(() -> {
            try {
                BusClient busClient = new BusClient(id);
                busClient.run();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        busThread.start();
    }
}
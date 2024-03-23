package Entities;

public class Globals {
    public record Arguments(boolean verbose, boolean heartbeat) { }

    public static Arguments arguments;

    public static void setArguments(String[] args) {
        boolean verbose = false;
        boolean heartbeat = false;

        for (String arg: args) {
            if (arg.equals("-v")) {
                verbose = true;
            }
            if (arg.equals("-hb")) {
                heartbeat = true;
            }
        }

        arguments = new Globals.Arguments(verbose, heartbeat);
    }

    public static final int MAX_PASSENGERS_PER_BUS = 20;
    public static final int MAX_PASSENGERS_PER_STOP = 40;
    public static final int MAX_BUSES_PER_TRIP = 3;
    public static final int MAX_STOPS_PER_TRIP = 10;
    public static final int NB_TRIPS = 20;
    public static final int NB_STOPS = 30;
    public static final int MAX_PASSENGERS = 1000;
    public static final String SERVER_IP = "127.0.0.1";
    public static final int SERVER_PORT = 8080;
    public static final int THREAD_POOL_SIZE = 20;
    public static final String HEARTBEAT = "HEARTBEAT: ";
    public static final String BUS_MESSAGE = "BUS_MESSAGE: ";
    public static final String STOP_MESSAGE = "STOP_MESSAGE: ";
    public static final String CONNECTION = "CONNECTION: ";
}
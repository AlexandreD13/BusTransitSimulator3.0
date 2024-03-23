package Entities;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class BusClient {

    public static void main(String[] args) {
        try (Socket socket = new Socket(Globals.SERVER_IP, Globals.SERVER_PORT);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            System.out.println("\nConnected to Transit Hub Server...\n");

            out.println(Globals.CONNECTION + "Bus XX");

            startHeartbeatThread(out);

            String response;
            while (true) {
                if ((response = in.readLine()) != null) {
                    // Handle different messages from the hub here (i.e. request for
                    // passengerCount / currentStop / nextStop / tripId / etc...)
                    System.out.println(response);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void startHeartbeatThread(PrintWriter out) {
        Thread heartbeatThread = new Thread(() -> {
            try {
                while (true) {
                    Thread.sleep(5000);
                    out.println(Globals.HEARTBEAT + "BUS XX");
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        heartbeatThread.start();
    }
}
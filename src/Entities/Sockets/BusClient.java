package Entities.Sockets;

import Entities.Globals;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class BusClient implements Runnable {
    private final int id;

    public BusClient(int id) {
        this.id = id;
    }
    @Override
    public void run() {
        try (Socket socket = new Socket(Globals.SERVER_IP, Globals.SERVER_PORT);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            Thread.sleep(2000);

            System.out.println("Bus " + id + " connected to Transit Hub Server...");

            out.println(Globals.CONNECTION + "Bus " + id);

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
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private void startHeartbeatThread(PrintWriter out) {
        Thread heartbeatThread = new Thread(() -> {
            try {
                while (true) {
                    Thread.sleep(10000);

                    if (Globals.arguments.heartbeat()) {
                        out.println(Globals.HEARTBEAT + "BUS " + id);
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        heartbeatThread.start();
    }
}
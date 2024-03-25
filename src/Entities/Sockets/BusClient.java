package Entities.Sockets;

import Entities.Globals;
import Entities.Passenger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

public class BusClient implements Runnable {
    private int id;
    private int tripId;
    private ArrayList<Passenger> passengers = new ArrayList<>();

    public BusClient(int id, int tripId) {
        this.id = id;
        this.tripId = tripId;
    }

    public void startBusThread() {
        Thread busThread = new Thread(() -> {
            try {
                run();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, "Bus" + getBusId() + "_Thread");
        busThread.start();
    }

    @Override
    public void run() {
        try (Socket socket = new Socket(Globals.SERVER_IP, Globals.SERVER_PORT);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            Thread.sleep(2000);
            out.println(Globals.BUS_MESSAGE + "Bus " + getBusId() + " connected");

            startHeartbeatThread(out);

            String response;
            while (true) {
                if ((response = in.readLine()) != null) {
                    if (response.endsWith(Globals.START_RUN)) {
                        out.println(Globals.BUS_MESSAGE + "BUS " + getBusId() + " starting run");
                    }
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
                    Thread.sleep(5000);

                    if (Globals.arguments.heartbeat()) {
                        out.println(Globals.HEARTBEAT + "BUS " + getBusId());
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        heartbeatThread.start();
    }

    public String getBusId() {
        return String.format("%d%d", tripId, id);
    }

    public ArrayList<Passenger> getPassengers() {
        return passengers;
    }
}
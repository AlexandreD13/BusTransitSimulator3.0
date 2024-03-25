package Entities.Sockets;

import Entities.Globals;
import Entities.Passenger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class StopClient implements Runnable {
    private int id;
    private ArrayList<Passenger> passengers = new ArrayList<>();
    private ReentrantLock lock = new ReentrantLock();

    public StopClient(int id) {
        this.id = id;
    }

    public void startStopThread() {
        Thread stopThread = new Thread(() -> {
            try {
                run();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, "Stop" + id + "_Thread");
        stopThread.start();
    }
    @Override
    public void run() {
        try (Socket socket = new Socket(Globals.SERVER_IP, Globals.SERVER_PORT);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            Thread.sleep(2000);
            out.println(Globals.STOP_MESSAGE + "Stop " + id + " connected");

            startHeartbeatThread(out);

            String response;
            while (true) {
                if ((response = in.readLine()) != null) {
                    if (response.endsWith(Globals.START_RUN)) {
                        // Do nothing
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
                        out.println(Globals.HEARTBEAT + "STOP " + id);
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        heartbeatThread.start();
    }

    public void addPassenger(Passenger p) {
        lock.lock();

        try {
            if (passengers.size() < Globals.MAX_PASSENGERS_PER_STOP) {
                System.out.println(
                        "Adding " + p.getName() + " (Passenger " + p.getId() + ") to Stop " + id + "."
                );
                passengers.add(p);
            } else {
                System.out.println(
                        "Stop " + id + " is full. Cannot add " + p.getName() + " (Passenger " + p.getId() + ")."
                );
            }
        } catch (Exception e) {
            System.out.println(
                    "An error occured while trying to add Passenger " + p.getId() + " to Stop " + id + "."
            );
        }

        lock.unlock();
    }

    public ArrayList<Passenger> getPassengers() {
        return passengers;
    }

    public int getId() {
        return id;
    }
}
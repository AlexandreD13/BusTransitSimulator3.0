package Entities.Sockets;

import Entities.Globals;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TransitHubServer implements Runnable {
    private final ArrayList<ClientHandler> clientHandlers = new ArrayList<>();
    private ServerSocket serverSocket = new ServerSocket(Globals.SERVER_PORT);

    public TransitHubServer() throws IOException {
        startHub();
    }

    private void startHub() {
        Thread hubThread = new Thread(() -> {
            try {
                run();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, "TransitHub_Thread");
        hubThread.start();
    }

    @Override
    public void run() {
        ExecutorService executorService = Executors.newFixedThreadPool(Globals.THREAD_POOL_SIZE);

        try {
            System.out.println("\nTransit Hub Server has started...");
            startHeartbeatThread();

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("New connection: " + clientSocket);

                ClientHandler clientHandler = new ClientHandler(clientSocket);
                clientHandlers.add(clientHandler);
                executorService.execute(clientHandler);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void broadcastMessage(String message) throws InterruptedException {
        synchronized (clientHandlers) {
            for (ClientHandler handler : clientHandlers) {
                handler.sendMessage(message);
            }

            Thread.sleep(1000);
        }
    }

    private void startHeartbeatThread() {
        Thread heartbeatThread = new Thread(() -> {
            try {
                while (true) {
                    Thread.sleep(10000);

                    if (Globals.arguments.heartbeat()) {
                        broadcastMessage(Globals.HEARTBEAT + "SERVER");
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        heartbeatThread.start();
    }

    public void shutdown() {
        try {
            for (ClientHandler handler : clientHandlers) {
                handler.disconnect();
            }
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
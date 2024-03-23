package Entities;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TransitHubServer {
    public static Globals.Arguments arguments;
    private static final ArrayList<ClientHandler> clientHandlers = new ArrayList<>();

    public static void main(String[] args) {
        arguments = Globals.setArguments(args);

        ExecutorService executorService = Executors.newFixedThreadPool(Globals.THREAD_POOL_SIZE);
        try (ServerSocket serverSocket = new ServerSocket(Globals.SERVER_PORT)) {

            System.out.println("\nTransit Hub Server has started...\n");

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

    public static void broadcastMessage(String message) {
        synchronized (clientHandlers) {
            for (ClientHandler handler : clientHandlers) {
                handler.sendMessage(message);
            }
        }
    }

    private static void startHeartbeatThread() {
        Thread heartbeatThread = new Thread(() -> {
            try {
                while (true) {
                    Thread.sleep(5000);
                    broadcastMessage(Globals.HEARTBEAT + "SERVER");
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        heartbeatThread.start();
    }
}
package Entities.Sockets;

import Entities.Globals;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientHandler implements Runnable {
    private final Socket clientSocket;
    private final PrintWriter out;

    public ClientHandler(Socket clientSocket) throws IOException {
        this.clientSocket = clientSocket;
        this.out = new PrintWriter(clientSocket.getOutputStream(), true);
    }

    @Override
    public void run() {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {

            while (true) {
                String clientIdentifier = in.readLine();

                if (clientIdentifier == null) {
                    break;
                }

                if (clientIdentifier.startsWith(Globals.BUS_MESSAGE)) {
                    handleBusMessage(clientIdentifier, in);
                } else if (clientIdentifier.startsWith(Globals.STOP_MESSAGE)) {
                    handleStopMessage(clientIdentifier, in);
                } else if (clientIdentifier.startsWith(Globals.HEARTBEAT)) {
                    handleHeartBeat(clientIdentifier, in);
                } else if (clientIdentifier.startsWith(Globals.CONNECTION)) {
                    handleConnection(clientIdentifier, in);
                } else {
                    System.err.println("Unknown client identifier: " + clientIdentifier);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleBusMessage(String clientId, BufferedReader in) throws IOException {
        synchronized (System.out) {
            System.out.println(clientId);
        }
    }

    private void handleStopMessage(String clientId, BufferedReader in) throws IOException {
        synchronized (System.out) {
            System.out.println(clientId);
        }
    }

    private void handleHeartBeat(String clientId, BufferedReader in) throws IOException {
        if (Globals.arguments.verbose()) {
            synchronized (System.out) {
                System.out.println(clientId);
            }
        }
    }

    private void handleConnection(String clientId, BufferedReader in) throws IOException {
        if (Globals.arguments.verbose()) {
            System.out.println(clientId);
        }
    }

    public void sendMessage(String message) {
        out.println(message);
    }
}
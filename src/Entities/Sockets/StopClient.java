package Entities.Sockets;

import Entities.Globals;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class StopClient implements Runnable {
    @Override
    public void run() {
        try (Socket socket = new Socket(Globals.SERVER_IP, Globals.SERVER_PORT);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            System.out.println("\nConnected to Transit Hub Server...\n");
            out.println(Globals.STOP_MESSAGE + " XX");

            String response;
            while ((response = in.readLine()) != null) {
                System.out.println("Received response from server: " + response);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
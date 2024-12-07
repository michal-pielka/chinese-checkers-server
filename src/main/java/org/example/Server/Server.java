package org.example.Server;

import java.io.*;
import java.net.ServerSocket;
import java.io.IOException;
import java.net.Socket;

public class Server {
    public static void main(String[] args) throws IOException {
        int port = 12345;
        try(ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server is listening on port " + port);

            while(true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Connected client: " + clientSocket.getInetAddress());

                InputStream input = clientSocket.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                OutputStream output = clientSocket.getOutputStream();
                PrintWriter writer = new PrintWriter(output, true);

                String message = reader.readLine();
                System.out.println(message);

                String response = "Spoko.";
                writer.println(response);

                clientSocket.close();
                System.out.println("Connection closed.");
            }
        }
        catch(IOException e) {
            e.printStackTrace();
        }
    }
}

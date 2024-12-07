package org.example.Client;

import java.io.*;
import java.net.Socket;

public class Client {
    public static void main(String[] args) throws IOException {
        String host = "localhost";
        int port = 12345;

        try(Socket socket = new Socket(host, port);
            OutputStream output = socket.getOutputStream();
            PrintWriter writer = new PrintWriter(output, true);
            InputStream input = socket.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));
            BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in))
            ) {
            String message;
            String response;
            while(true) {
                response = reader.readLine();
                while(response != null) {
                    System.out.println(response);
                    response = reader.readLine();
                }
                message = consoleReader.readLine();
                writer.println(message);
                if(message.equalsIgnoreCase("exit")) {
                    break;
                }
            }
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }
}

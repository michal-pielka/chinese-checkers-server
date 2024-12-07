package org.example.Client;

import java.io.*;
import java.net.Socket;

public class Client {
    public static void main(String[] args) throws IOException {
        String host = "localhost";
        int port = 12345;

        try(Socket socket = new Socket(host, port)) {
            OutputStream output = socket.getOutputStream();
            PrintWriter writer = new PrintWriter(output, true);
            InputStream input = socket.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));

            writer.println("Działa.");

            String response = reader.readLine();
            System.out.println("Rsponse: " + response);
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }
}

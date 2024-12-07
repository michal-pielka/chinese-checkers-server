package org.example.MyThread;

import java.io.*;
import java.net.Socket;

public class MyThread extends Thread{
    private Socket clientSocket;

    public MyThread(Socket socket) {
        this.clientSocket = socket;
    }

    @Override
    public void run() {
        try(
            InputStream input = clientSocket.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));
            OutputStream output = clientSocket.getOutputStream();
            PrintWriter writer = new PrintWriter(output, true);
        ){
            writer.println("Select option: ");
            writer.println("create");
            writer.println("create LOBBY_NAME MAX_PLAYER_SIZE");
            writer.println("join LOBBY_NAME");

            String message;
            message = reader.readLine();
            System.out.println(message);


        }
        catch(IOException e) {
            e.printStackTrace();
        }
        finally {
            try{
                clientSocket.close();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

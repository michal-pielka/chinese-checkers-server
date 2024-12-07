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

            String line;
            line = reader.readLine();
            String[] message = line.split(" ");
            if(message.length == 1 && message[0].equalsIgnoreCase("create")) {

            }
            else if(message.length == 2 && message[0].equalsIgnoreCase("join")){

            }
            else if(message.length == 3 && message[0].equalsIgnoreCase("create")){

            }
            else {

            }
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

package org.example.Server;

import org.example.MyThread.MyThread;

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
                MyThread thread = new MyThread(clientSocket);
                thread.start();
            }
        }
        catch(IOException e) {
            e.printStackTrace();
        }
    }
}

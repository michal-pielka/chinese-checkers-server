package org.example.Server;

import org.example.Game.Game;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Encapsulates the core server logic.
 */
public class Server {
    private int port;
    private int threadPoolSize;
    private ExecutorService pool;

    /**
     * Constructor to initialize server settings.
     *
     * @param port            The port on which the server listens.
     * @param threadPoolSize  The number of threads in the pool.
     */
    public Server(int port, int threadPoolSize) {
        this.port = port;
        this.threadPoolSize = threadPoolSize;
        this.pool = Executors.newFixedThreadPool(threadPoolSize);
    }

    /**
     * Starts the server to listen for client connections.
     *
     * @throws IOException If an I/O error occurs when opening the socket.
     */
    public void start() throws IOException {
        System.out.println("Server is running on port " + port + "...");
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("New client connected: " + clientSocket);
                pool.execute(new UserThread(clientSocket));
            }
        } finally {
            shutdownPool();
        }
    }

    /**
     * Shuts down the thread pool gracefully.
     */
    private void shutdownPool() {
        if (pool != null && !pool.isShutdown()) {
            pool.shutdown();
            System.out.println("Server thread pool shut down.");
        }
    }
}

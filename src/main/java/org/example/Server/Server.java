package org.example.Server;

import org.example.Game.Game;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Encapsulates the core server logic.
 */
public class Server {
    private int port;
    private int threadPoolSize;
    private ExecutorService pool;
    private final List<Game> games;

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
        // Initialize a thread-safe list for games
        this.games = Collections.synchronizedList(new ArrayList<>());
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

                // Pass the server reference to UserThread
                UserThread user = new UserThread(clientSocket, this);
                pool.execute(user);
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

    /**
     * Finds a game by its lobby name (case-insensitive).
     *
     * @param lobbyName The name of the lobby to find.
     * @return The Game object if found; otherwise, null.
     */
    public Game findGameByName(String lobbyName) {
        synchronized (games) {
            for (Game game : games) {
                if (game.getLobbyName().equalsIgnoreCase(lobbyName)) {
                    return game;
                }
            }
        }
        return null;
    }

    /**
     * Adds a new game to the list.
     *
     * @param game The Game object to add.
     */
    public void addGame(Game game) {
        synchronized (games) {
            games.add(game);
            System.out.println("Game added: " + game.getLobbyName());
        }
    }

    /**
     * Retrieves an unmodifiable list of current games.
     *
     * @return A list of Game objects.
     */
    public List<Game> getGames() {
        synchronized (games) {
            return Collections.unmodifiableList(new ArrayList<>(games));
        }
    }

    /**
     * Removes a game from the list.
     *
     * @param game The Game object to remove.
     */
    public void removeGame(Game game) {
        synchronized (games) {
            games.remove(game);
            System.out.println("Game removed: " + game.getLobbyName());
        }
    }
}

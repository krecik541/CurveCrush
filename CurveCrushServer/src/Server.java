import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Server {
    /**
     * Służy do wypisywania stanu serwera
     */
    private static final Logger logger = Logger.getLogger(Server.class.getName());
    /**
     * Przechowuje wątki graczy
     */
    private final Vector<Thread> threads = new Vector<>();
    /**
     * Lista przechowująca historię czatu
     * Pierwszy string w parze jest nickiem gracza, a drugi wiadomością, którą wysłał
     */
    private final List<Pair<String, String>> chat = Collections.synchronizedList(new LinkedList<>());// Collections.synchronizedList(new LinkedList<>());
    /**
     * Referencja do obiektu odpowiadającego za stan gry
     */
    private final Game game;

    /**
     * Startuje działanie serwera, odpala osobne wątki dla gry oraz każdego klienta
     *
     * @param port port na którym będzie nasłuchiwac serwer
     */
    public Server(int port) {
        try (ServerSocket server = new ServerSocket(port)) {
            logger.log(Level.INFO, "Server started");

            logger.log(Level.INFO, "Waiting for client ...");

            game = new Game();
            Thread gameThread = new Thread(game);
            gameThread.start();

            while (true) {
                // 60 s nieaktywności gameThread
                try {
                    server.setSoTimeout(1000);
                    Socket socket = server.accept();
                    logger.log(Level.INFO, "Client accepted " + socket);

                    Thread thread = new Thread(new ClientHandler(socket, game, chat));
                    thread.setPriority(Thread.MAX_PRIORITY);
                    threads.add(thread);
                    thread.start();
                }
                catch (SocketTimeoutException e)
                {
                    if(game.isGameOver())
                        break;
                }
            }

            for (Thread thread : threads) {
                thread.join();
            }
        } catch (InterruptedException | IOException e) {
            throw new RuntimeException(e);
        }

    }
    public static void main(String[] args) {

        Server server = new Server(5000);

    }
}

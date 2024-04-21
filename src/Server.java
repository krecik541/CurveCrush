import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Server {
    private static final Logger logger = Logger.getLogger(Server.class.getName());
    List<Pair<String, String>> chat = new LinkedList<>();
    public Server(int port)
    {
        try{
            ServerSocket server = new ServerSocket(port);
            logger.log(Level.INFO, "Server started");

            logger.log(Level.INFO, "Waiting for client ...");

            Game game = new Game(this);

            while (true)
            {
                Socket socket = server.accept();
                logger.log(Level.INFO, "Client accepted " + socket);

                Thread thread = new Thread(new ClientHandler(socket, game, chat));
                thread.start();
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

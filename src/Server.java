import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Server {
    private static final Logger logger = Logger.getLogger(Server.class.getName());
    public Server(int port)
    {
        try{
            ServerSocket server = new ServerSocket(port);
            logger.log(Level.INFO, "Server started");

            logger.log(Level.INFO, "Waiting for client ...");

            while (true)
            {
                Socket socket = server.accept();
                logger.log(Level.INFO, "Client accepted " + socket);

                Thread thread = new Thread(new ClientHandler(socket));
                thread.start();
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

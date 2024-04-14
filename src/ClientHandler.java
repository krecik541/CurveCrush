import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;
import java.util.logging.Logger;

public class ClientHandler implements Runnable{
    private final Socket socket;
    private static final Logger logger = Logger.getLogger(Server.class.getName());
    public ClientHandler(Socket socket)
    {
        this.socket = socket;
    }

    @Override
    public void run()
    {

        try {
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
            out.writeObject("ready");
            //Scanner scanner = new Scanner(System.in);
            //wczytywanie chatu
            //wysyłanie chatu
            //pobieranie i wysyłanie stanu gry
            //pobieranie i wysyłanie ruchu
            //Pair<String, Message>message = new Pair<String, Message>("", );
//            while(true)
//            {
//                message.x = (String) in.readObject();
//                message.y = (Message) in.readObject();
//                out.writeObject(message);
//            }


        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}

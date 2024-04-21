import java.awt.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.logging.Logger;

public class ClientHandler implements Runnable{
    private final Socket socket;
    private String name;
    private Game game;
    private List<Pair<String, String>> chat;
    Player player;
    private static final Logger logger = Logger.getLogger(Server.class.getName());
    public ClientHandler(Socket socket, Game game, List<Pair<String, String>> chat)
    {
        this.socket = socket;
        this.game = game;
        this.chat = chat;
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

            String type, communicate;
            boolean isQuitting = false;

            name = in.readObject().toString();
            Random random = new Random(System.currentTimeMillis());
            player = new Player(name, 0, false, new Color(random.nextInt(255), random.nextInt(255), random.nextInt(255)), 0);

            game.addToPlayerList(player);

            while(true)
            {
                type = (String) in.readObject();
                switch(type)
                {
                    case "CHAT":
                        // obsługa chatu
                        communicate = (String) in.readObject();
                        chat.add(new Pair<>(name, communicate));
                        break;
                    case "POSITION":
                        // obsługa wysyłania pozycji
//                        int value = (int) in.readObject();
//                        player.updateAngle();
//                        value = (int) in.readObject();
//                        player.updatePosition();
                        break;
                    case "QUIT":
                        isQuitting = true;
                        break;
                    default:
                        break;
                }


                // DOZMIANY
                // wysyłanie tylko wiadomości, które zostały wysłane zamiast całej historii chatu

                // wysyłanie chatu
                out.writeObject(chat.size());
                for(Pair<String, String>message: chat)
                {
                    out.writeObject(message);
                }

                // wysyłanie pozycji graczy
                // wysłanie scoru graczy
                List<Player>playerList = game.getPlayerList();
                out.writeObject(playerList.size());
                for(Player pl : playerList)
                {
                    out.writeObject(pl);
                }

                if(isQuitting)
                    break;
            }

            out.close();
            in.close();
            socket.close();
        } catch (ClassNotFoundException | IOException e) {
            throw new RuntimeException(e);
        }
    }

}

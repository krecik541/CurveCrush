import java.awt.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Queue;
import java.util.Random;
import java.util.Scanner;
import java.util.logging.Logger;

public class ClientHandler implements Runnable
{
    // Uchwyt gniazda
    private final Socket socket;
    // Uchwyt gry
    private final Game game;
    // Uchywt chatu
    private final List<Pair<String, String>> chat;
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
            //out.writeObject("ready");
            //Scanner scanner = new Scanner(System.in);
            //wczytywanie chatu
            //wysyłanie chatu
            //pobieranie i wysyłanie stanu gry
            //pobieranie i wysyłanie ruchu

            String type;
            boolean isQuitting = false;

            String name = in.readObject().toString();
            Random random = new Random(System.currentTimeMillis());
            Player player = new Player(name, 0, false, new Color(random.nextInt(255), random.nextInt(255), random.nextInt(255)), random.nextInt(0, 360));

            game.addToPlayerList(player);

            String string;
            do {
                string = (String)in.readObject();
            } while (string == null || !string.equalsIgnoreCase("READY"));
            player.setReady(true);

            // sleep do czasu, aż gra się nie rozpocznie lub nie wysłanie zostany czat

            while(game.isGameOver())
            {
                out.writeObject(true);
            }
            out.writeObject(false);

            while(!isQuitting)
            {
//                String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss.SSS").format(new java.util.Date());
//                System.out.println(timeStamp);
                if(game.isRoundOver() && !game.isGameOver())
                    out.writeObject("newround");
                else if(game.isRoundOver() && game.isGameOver()) {
                    out.writeObject("endgame");
                    if(in.readObject().toString().equalsIgnoreCase("end"))
                    {
                        out.close();
                        in.close();
                        socket.close();
                        return;
                    }
                }
                else
                    out.writeObject("none");

                synchronized (game) {
                    game.wait();

                    type = in.readObject().toString();
                    if(type.equalsIgnoreCase("angle"))
                    {
                        player.updateAngle((Integer) in.readObject());
                    }
                    else if(type.equalsIgnoreCase("quit"))
                    {
                        isQuitting = true;
                        break;
                    }
                    else if (type.equalsIgnoreCase("chat"))
                    {
                        synchronized (chat) {
                            chat.add(new Pair<>(name, in.readObject().toString()));/*  000 000 000 000 000 000 000   */
                        }
                        player.updateAngle((Integer) in.readObject());
                    }

                    // wysyłanie chatu
                    out.writeObject(chat.size());
                    synchronized (chat) {
                        for (Pair<String, String> message : chat) {
                            boolean correct = false;
                            while (!correct) {
                                out.writeObject(message.toString());
                                correct = (boolean) in.readObject();
                            }
                        }
                    }

                    // wysyłanie pozycji graczy
                    // wysłanie scoru graczy
                    Queue<Player> playerList = game.getPlayerList();
                    out.writeObject(playerList.size());
                    for (Player pl : playerList) {
                        boolean correct = false;
                        while (!correct) {
                            out.writeObject(pl.getName());
                            out.writeObject(pl.getScore());
                            if(pl.getTrailBreak().x > 0) {
                                out.writeObject(true);
                                out.writeObject(pl.getLastPosition().x);
                                out.writeObject(pl.getLastPosition().y);
                            }
                            else
                                out.writeObject(false);


                            out.writeObject(pl.getPosition().x);
                            out.writeObject(pl.getPosition().y);
                            out.writeObject(pl.getColor());
                            correct = (boolean) in.readObject();
                        }
                    }
                }

//                timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss.SSS").format(new java.util.Date());
//                System.out.println(timeStamp);
            }

            out.close();
            in.close();
            socket.close();
        } catch (InterruptedException | ClassNotFoundException | IOException e) {
            throw new RuntimeException(e);
        }
    }
}

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
    /**
     * Uchwyt gniazda
     */
    private final Socket socket;
    /**
     * Uchwyt gry
     */
    private final Game game;
    /**
     * Uchywt chatu
     */
    private final List<Pair<String, String>> chat;
    public ClientHandler(Socket socket, Game game, List<Pair<String, String>> chat)
    {
        this.socket = socket;
        this.game = game;
        this.chat = chat;
    }

    /**
     * Umożliwia obsługę komunikacji na linii serwer-klient, tworzy także obiekt gracza
     */
    @Override
    public void run()
    {

        try {
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

            String type;

            // Pobranie nicku gracza, stworzenie obiektu gracza
            String name = in.readObject().toString();
            Random random = new Random(System.currentTimeMillis());
            Player player = new Player(name, 0, false, new Color(random.nextInt(255), random.nextInt(255), random.nextInt(255)), random.nextInt(0, 360));

            // Dodanie obiektu gracza do listy graczy
            game.addToPlayerList(player);

            // Oczekiwanie na zgłoszenie przez gracza gotowości
            String string;
            do {
                string = (String)in.readObject();
            } while (string == null || !string.equalsIgnoreCase("READY"));
            player.setReady(true);

            // Sleep do czasu, aż gra się nie rozpocznie (każdy gracz nie zgłosi gotowości)
            while(game.isGameOver())
            {
                out.writeObject(true);
            }
            out.writeObject(false);

            while(true)
            {
                // Wysłanie znacznika nowej rundy
                if(game.isRoundOver() && !game.isGameOver())
                    out.writeObject("newround");
                // Wysłanie znacznika zakończenia gry, a także zakończenie działania wątku
                else if(game.isRoundOver() && game.isGameOver()) {
                    out.writeObject("endgame");
                    // Odebranie potwierdzenia przyjęcia znacznika
                    if(in.readObject().toString().equalsIgnoreCase("end"))
                    {
                        out.close();
                        in.close();
                        socket.close();
                        return;
                    }
                }
                // Znacznik neutralny
                else
                    out.writeObject("none");


                synchronized (game) {
                    game.wait();

                    type = in.readObject().toString();
                    // Odebranie aktualizacji kąta gracza
                    if(type.equalsIgnoreCase("angle"))
                    {
                        player.updateAngle((Integer) in.readObject());
                    }
                    // Odebranie informacji o zakończeniu przez gracza rozgrywki
                    else if(type.equalsIgnoreCase("quit"))
                    {
                        break;
                    }
                    // Odebranie czatu oraz aktualizacji kąta
                    else if (type.equalsIgnoreCase("chat"))
                    {
                        synchronized (chat) {
                            chat.add(new Pair<>(name, in.readObject().toString()));
                        }
                        player.updateAngle((Integer) in.readObject());
                    }

                    // Wysyłanie wiadomości zawartych na czacie
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

                    Queue<Player> playerList = game.getPlayerList();
                    out.writeObject(playerList.size());
                    for (Player pl : playerList) {
                        boolean correct = false;
                        // Wysyłanie jest ponawiane do momentu, aż nie odebrany zostanie komunikat o poprawnym
                        // odebraniu i przetworzeniu
                        while (!correct) {
                            // Wysłanie nicku gracza, którego informacje są aktualnie wysyłane
                            out.writeObject(pl.getName());
                            // Wysyłanie wyniku gracza
                            out.writeObject(pl.getScore());
                            // Wysyłanie pozycji gracza w momencie, gdy ma wystąpić przerwa w śladzie danego gracza
                            if(pl.getTrailBreak().x > 0) {
                                out.writeObject(true);
                                out.writeObject(pl.getLastPosition().x);
                                out.writeObject(pl.getLastPosition().y);
                            }
                            else
                                out.writeObject(false);

                            // Wysyłanie pozycji danego gracza
                            out.writeObject(pl.getPosition().x);
                            out.writeObject(pl.getPosition().y);
                            // Wysyłanie koloru danego gracza
                            out.writeObject(pl.getColor());
                            // Odebranie komunikatu potwierdzającego poprawne/niepoprawne odebranie i przetworzenie
                            // po stronie klienta
                            correct = (boolean) in.readObject();
                        }
                    }
                }
            }

            out.close();
            in.close();
            socket.close();
        } catch (InterruptedException | ClassNotFoundException | IOException e) {
            throw new RuntimeException(e);
        }
    }
}

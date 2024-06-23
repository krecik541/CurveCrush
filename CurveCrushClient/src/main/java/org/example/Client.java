package org.example;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Client {
    public static void main(String[] args) {
        try {
            // IP i port serwera
            Socket socket = new Socket("192.168.0.31", 5000);

            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

            // Pobieranie nicku użytkownika
            String name = JOptionPane.showInputDialog("Enter your name:");
            Window window = new Window();
            Chat chat = window.getChat();
            List<Pair<String, Integer>> playerList = new LinkedList<>();
            List<Pair<Pair<Integer, Integer>, Color>> playerMove = new ArrayList<>();

            // Wysyłanie nicku
            out.writeObject(name);
            boolean isQuitting = false;

            // Oczekiwanie na potwierdzenie przez gracza gotowości
            String string;
            do {
                string = chat.getMessage();
                out.writeObject(string);
            } while (string == null || !string.equalsIgnoreCase("READY"));

            // Sleep do czasu aż gra się nie rozpocznie
            boolean q = true;
            do {
                q = (boolean) in.readObject();
            } while(q);

            while(true)
            {
                String special = in.readObject().toString();
                switch(special)
                {
                    // Odebranie znacznika nowej rundy
                    case "newround": {
                        //czyszczenie planszy
                        window.getDrawingPanel().cleanGameBoard();
                        playerMove.clear();
                        for (int i = 5; i > 0; i--) {
                            Thread.sleep(1000);
                            chat.add("Gra rozpocznie sie za " + i + " sekund");
                            window.draw(playerList, playerMove.stream().toList());
                        }
                        break;
                    }
                    // Odebranie znacznika zakończenia gry, a także zakończenie działania wątku
                    case "endgame": {
                        // Wysłanie potwierdzenia przyjęcia znacznika
                        out.writeObject("end");
                        out.close();
                        in.close();
                        socket.close();
                        chat.add("Gra się zakończyła!");
                        for(Pair<String, Integer>pl:playerList)
                        {
                            if(pl.y >= 10)
                            {
                                chat.add("Wygrywa " + pl.x + "!");
                            }
                        }
                        chat.add("Za 5 sekund okno zostanie zamknięte!");
                        window.draw(playerList, playerMove.stream().toList());
                        Thread.sleep(5000);
                        window.dispose();
                        //zakończenie okna
                        System.exit(0);
                    }

                }

                // Wysyłanie danych
                String line = chat.getMessage();
                chat.setMessage(null);
                // Wysłanie aktualizacji kąta gracza
                if(line == null)
                {
                    out.writeObject("ANGLE");
                    out.writeObject(window.getAngle());
                }
                // Wysłanie informacji o zakończeniu przez gracza rozgrywki
                else if (line.equals("QUIT"))
                {
                    out.writeObject("QUIT");
                    isQuitting = true;
                }
                // Wysłanie czatu oraz aktualizacji kąta
                else
                {
                    out.writeObject("CHAT");
                    out.writeObject(line);
                    out.writeObject(window.getAngle());
                }

                if(isQuitting) {
                    break;
                }
                playerList.clear();



                // Odbieranie chatu
                int size = (int) in.readObject();
                chat.clear();
                for(int i=0; i<size; i++) {
                    try {
                        String pog = in.readObject().toString();
                        chat.add(pog);
                        out.writeObject(true);
                    }
                    catch (ClassCastException e)
                    {
                        out.writeObject(false);
                        i--;
                    }
                }

                // Odebranie jest ponawiane do momentu, aż nie odebrany zostanie komunikat o poprawnym
                // odebraniu i przetworzeniu dla każdego gracza
                size = (int) in.readObject();
                for(int i=0; i<size; i++) {
                    String s;
                    int score, posx, posy;
                    Color color;

                    try {
                        // Odebranie nicku gracza, którego informacje są aktualnie wysyłane
                        s = (String) in.readObject();
                        // Odebranie wyniku gracza
                        score = (Integer) in.readObject();

                        // Odebranie pozycji gracza w momencie, gdy ma wystąpić przerwa w śladzie danego gracza
                        if((boolean) in.readObject())
                        {
                            posx = (Integer)in.readObject();
                            posy = (Integer)in.readObject();
                            synchronized(playerMove) {
                                playerMove.add(new Pair<>(new Pair<>(posx, posy), Color.WHITE));
                            }
                        }

                        // Odebranie pozycji danego gracza
                        posx = (Integer)in.readObject();
                        posy = (Integer)in.readObject();
                        // Odebranie koloru danego gracza
                        color = (Color) in.readObject();

                        playerList.add(new Pair<>(s, score));
                        synchronized(playerMove) {
                            playerMove.add(new Pair<>(new Pair<>(posx, posy), color));
                        }
                        // Odebranie komunikatu potwierdzającego poprawne odebranie i przetworzenie
                        out.writeObject(true);
                    }
                    catch (ClassCastException e)
                    {
                        // Wysyłanie komunikatu potwierdzającego niepoprawne odebranie i przetworzenie
                        // a następnie ponowienie całej operacji
                        out.writeObject(false);
                        i--;
                    }
                }

                window.draw(playerList, playerMove.stream().toList());
            }

            out.close();
            in.close();
            socket.close();


        } catch (InterruptedException | ClassNotFoundException | IOException e) {
            throw new RuntimeException(e);
        }
    }
}
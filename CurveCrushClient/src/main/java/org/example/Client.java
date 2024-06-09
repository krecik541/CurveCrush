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
            Socket socket = new Socket("localhost", 5000);
            Logger logger = Logger.getLogger("");

            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

            String name = JOptionPane.showInputDialog("Enter your name:");
            Window window = new Window();
            Chat chat = window.getChat();
            List<Pair<String, Integer>> playerList = new LinkedList<>();
            List<Pair<Pair<Integer, Integer>, Color>> playerMove = new ArrayList<>();

            // wysyłanie imienia
            out.writeObject(name);
            boolean isQuitting = false;

            String string;
            do {
                string = chat.getMessage();
                out.writeObject(string);
            } while (string == null || !string.equalsIgnoreCase("READY"));

            // sleep do czasu aż gra się nie rozpocznie lub nie zostanie wysłany chat

            boolean q = true;
            do {
                q = (boolean) in.readObject();
            } while(q);

            while(true)
            {
//                String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss.SSS").format(new java.util.Date());
//                System.out.println(timeStamp);
                // jeśli się skończyła runda
                String special = in.readObject().toString();
                switch(special)
                {
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
                    case "endgame": {
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

                // wysyłanie danych
                String line = chat.getMessage();
                chat.setMessage(null);
                if(line == null)
                {
                    //wysyła samą pozycję
                    out.writeObject("ANGLE");
                    out.writeObject(window.getAngle());
                }
                else if (line.equals("QUIT"))
                {
                    out.writeObject("QUIT");
                    isQuitting = true;
                }
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



                // odbieranie chatu
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

                // odbieranie name + score + position + color gracza
                size = (int) in.readObject();
                for(int i=0; i<size; i++) {
                    String s;
                    int score, posx, posy;
                    Color color;

                    try {
                        s = (String) in.readObject();
                        score = (Integer) in.readObject();

                        boolean p = (boolean) in.readObject();

                        if(p)
                        {
                            posx = (Integer)in.readObject();
                            posy = (Integer)in.readObject();
                            synchronized(playerMove) {
                                playerMove.add(new Pair<>(new Pair<>(posx, posy), Color.WHITE));
                            }
                        }

                        posx = (Integer)in.readObject();
                        posy = (Integer)in.readObject();
                        color = (Color) in.readObject();

                        playerList.add(new Pair<>(s, score));
                        synchronized(playerMove) {
                            playerMove.add(new Pair<>(new Pair<>(posx, posy), color));
                        }
                        out.writeObject(true);
                    }
                    catch (ClassCastException e)
                    {
                        out.writeObject(false);
                        i--;
                    }
                }

//                logger.log(Level.INFO, "REPAINTED");
                window.draw(playerList, playerMove.stream().toList());



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
package org.example;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.security.KeyPair;
import java.util.ArrayList;
import java.util.EventListener;
import java.lang.Math;
import java.util.List;


public class Window extends JFrame implements ActionListener{
    private final static Color DEFAULT = new Color(0x1C, 0x1C, 0x1C);
    private final static Color DARK_GREY = new Color(0x25, 0x25, 0x25);
    private JPanel main_panel = new JPanel();

    private JPanel game_panel = new JPanel();
    /**
     * Zmienne odpowiedzialne za wyświetlanie czatu
     */

    private JPanel score_panel = new JPanel();
    private JTextArea scoreTextArea = new JTextArea();

    /**
     * Zmienne odpowiedzialne za wyświetlanie czatu
     */
    private JPanel chat_panel = new JPanel();
    private JTextArea chatLabel = new JTextArea();
    private  JTextField chatTextField = new JTextField();
    private JButton chatExitButton = new JButton("EXIT");
    private JButton chatSendButton = new JButton("Send");
    private DrawingPanel drawingPanel = new DrawingPanel();
    private JPanel left_panel = new JPanel();//chat_panel + score_panel

    private final Chat chat = new Chat();

    private final Pair<Integer, Integer> windowSize = new Pair<Integer, Integer>(1280, 800);
    private final Pair<Double, Double> windowMargin = new Pair<Double, Double>(0.05, 0.05);
    public Window()
    {
        main_panel.setBackground(DEFAULT);
        chat_panel.setBackground(DARK_GREY);
        game_panel.setBackground(Color.WHITE);
        score_panel.setBackground(Color.BLACK);

        main_panel.setLayout(new GridLayout(1, 2, (int)(windowMargin.x * windowSize.x), (int)(windowMargin.y * windowSize.y)));

        left_panel.setLayout(new BoxLayout(left_panel, BoxLayout.Y_AXIS));


        int padding = 30;
        main_panel.setBorder(new EmptyBorder(padding, padding, padding, padding));
        padding = 20;
        chat_panel.setBorder(new EmptyBorder(padding, padding, padding, padding));
        score_panel.setBorder(new EmptyBorder(padding, padding, padding, padding));


        // Ustawienia okienka chatu
        chat_panel.setLayout(new BorderLayout());
        chatSendButton.addActionListener(this);
        chatExitButton.addActionListener(this);

        chatLabel.setForeground(Color.WHITE);
        JScrollPane scrollPane = new JScrollPane(chatLabel);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        chat_panel.add(scrollPane, BorderLayout.CENTER);
        chatLabel.setBackground(DARK_GREY);
        scrollPane.setBackground(DARK_GREY);

        JPanel buttonPanel = new JPanel();
        chatExitButton.setPreferredSize(new Dimension(100, 50));
        chatSendButton.setPreferredSize(new Dimension(100, 50));
        chatTextField.setPreferredSize(new Dimension(571-200-60, 50));
        buttonPanel.add(chatExitButton);
        buttonPanel.add(chatTextField);
        buttonPanel.add(chatSendButton);
        chat_panel.add(buttonPanel, BorderLayout.SOUTH);


        scoreTextArea.setForeground(Color.WHITE);
        scoreTextArea.setBackground(Color.BLACK);
        JScrollPane scoreScrollPane = new JScrollPane(scoreTextArea);
        scoreScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scoreScrollPane.setPreferredSize(null); // Ustawienie rozmiaru na null
        score_panel.setLayout(new BorderLayout()); // Ustawienie layoutu na BorderLayout dla panelu wyników
        score_panel.add(scoreScrollPane, BorderLayout.CENTER); // Dodanie JScrollPane do panelu wyników


        // Ustawienia okienka gry
        game_panel.setLayout(new BorderLayout());
         // Utworzenie obiektu klasy DrawingPanel
        game_panel.add(drawingPanel, BorderLayout.CENTER);


        chat_panel.setPreferredSize(new Dimension(571, 500));
        score_panel.setPreferredSize(new Dimension(100, 500));

        left_panel.add(score_panel, BorderLayout.CENTER);
        left_panel.add(chat_panel, BorderLayout.CENTER);
        main_panel.add(left_panel);
        main_panel.add(game_panel, BorderLayout.CENTER);
        this.add(main_panel, BorderLayout.CENTER);

        this.setVisible(true);
        this.setSize(windowSize.x, windowSize.y);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.getContentPane().setBackground(DEFAULT);
        this.setTitle("SZALONE ZAKRĘTY");
        drawingPanel.setFocusable(true);
        drawingPanel.requestFocusInWindow();
    }

    public DrawingPanel getDrawingPanel() {
        return drawingPanel;
    }
    public Chat getChat(){return chat;}

    /**
     * Wyrysowywanie całego okna przy zaktualizowanym stanie
     * @param playerList lista graczy zawierająca nicki oraz wyniki graczy, wyświetlana w score_panel
     * @param playerMove lista graczy zawierająca pozycje oraz kolory graczy, wyrysowywana w oknie gry
     */
    public void draw(List<Pair<String, Integer>>playerList, List<Pair<Pair<Integer, Integer>, Color>> playerMove)
    {
        // wyświetlanie czatu
        chatLabel.setText("");
        chatLabel.append(chat.getChat());

        //score_panel
        scoreTextArea.setText("");
        String score = "";
        for(Pair<String, Integer> player:playerList){
            score = score.concat(player.x + "\t" + player.y + "\n");
        }
        scoreTextArea.append(score);

        // wyrysowanie okna gry
        drawingPanel.setThingsToDraw(playerMove);

    }

    /**
     * Obsługa przycisków
     * @param e the event to be processed
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getActionCommand().equals("Send")) {
            chat.setMessage(chatTextField.getText());
            chatTextField.setText("");
            // chat.add(message);
            drawingPanel.setFocusable(true);
            drawingPanel.requestFocusInWindow();
        } else if (e.getActionCommand().equals("EXIT")) {
            chat.setMessage("QUIT");
        }
    }

    public int getAngle()
    {
        return drawingPanel.getAngle();
    }
}

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


public class Window extends JFrame implements ActionListener, KeyListener{
    final static Color DEFAULT = new Color(0x1C, 0x1C, 0x1C);
    final static Color DARK_GREY = new Color(0x25, 0x25, 0x25);
    JPanel main_panel = new JPanel();

    JPanel game_panel = new JPanel();
    // Zmienne odpowiedzialne za wyświetlanie czatu
    JPanel score_panel = new JPanel();
    JTextArea scoreTextArea = new JTextArea();

    // Zmienne odpowiedzialne za wyświetlanie czatu
    JPanel chat_panel = new JPanel();
    JTextArea chatLabel = new JTextArea();
    JTextField chatTextField = new JTextField();
    JButton chatExitButton = new JButton("EXIT");
    JButton chatSendButton = new JButton("Send");
    Chat chat = new Chat();


    Game game;

    JPanel left_panel = new JPanel();//chat_panel + score_panel

    Pair<Integer, Integer> windowSize = new Pair<Integer, Integer>(1280, 800);
    Pair<Double, Double> windowMargin = new Pair<Double, Double>(0.05, 0.05);
    private int x = 650; // Początkowa pozycja x
    private int y = 400; // Początkowa pozycja y
    private List<Point> trail = new ArrayList<>();
    public Window()
    {
        Pair<Integer, Integer> margin = new Pair<Integer, Integer>((int)(windowSize.x * windowMargin.x), (int)(windowSize.y * windowMargin.y));

        main_panel.setBackground(DEFAULT);
        chat_panel.setBackground(DARK_GREY);
        game_panel.setBackground(Color.WHITE);
        score_panel.setBackground(Color.BLACK);

        main_panel.setLayout(new GridLayout(1, 2, (int)(windowMargin.x * windowSize.x), (int)(windowMargin.y * windowSize.y)));

        left_panel.setLayout(new BoxLayout(left_panel, BoxLayout.Y_AXIS));


        int padding = 30; // możesz dostosować wielkość paddingu według potrzeb
        main_panel.setBorder(new EmptyBorder(padding, padding, padding, padding));
        padding = 20;
        chat_panel.setBorder(new EmptyBorder(padding, padding, padding, padding));
        score_panel.setBorder(new EmptyBorder(padding, padding, padding, padding));


        // Ustawienia okienka chatu
        chat_panel.setLayout(new BorderLayout());
        chatSendButton.addActionListener(this);

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


        // Ustawienia okienka wyników
        // wersja ze scrollem
        scoreTextArea.setForeground(Color.WHITE);
        scoreTextArea.setBackground(Color.BLACK);
        JScrollPane scoreScrollPane = new JScrollPane(scoreTextArea);
        scoreScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scoreScrollPane.setPreferredSize(null); // Ustawienie rozmiaru na null
        score_panel.setLayout(new BorderLayout()); // Ustawienie layoutu na BorderLayout dla panelu wyników
        score_panel.add(scoreScrollPane, BorderLayout.CENTER); // Dodanie JScrollPane do panelu wyników

        // wersja bez scrolla
//        scoreTextArea.setForeground(Color.WHITE);
//        scoreTextArea.setBackground(Color.BLACK);
//        score_panel.setLayout(new BorderLayout()); // Ustawienie layoutu na BorderLayout dla panelu wyników
//        score_panel.add(scoreTextArea, BorderLayout.CENTER); // Dodanie JScrollPane do panelu wyników



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
        this.addKeyListener(this);
        this.setFocusable(true);
        this.requestFocusInWindow();
    }

    public void setGame(Game game){
        this.game = game;
    }
    public void draw()
    {
        // wyświetlanie czatu
        chatLabel.setText("");
        chatLabel.append(chat.getChat());

        //score_panel
        scoreTextArea.setText("");
        String score = "";
        for(Player player:game.getPlayerList()){
            String playername = player.getName();
            score = score.concat(player.getName() + "\t" + player.getScore() + "\n");
        }
        scoreTextArea.append(score);

        //game_panel
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getActionCommand().equals("Send")) {
            String message = chatTextField.getText();
            chatTextField.setText("");
            chat.add(message);
            this.setFocusable(true);
            this.requestFocusInWindow();
        } else if (e.getActionCommand().equals("EXIT")) {
            //
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        // Zapamiętanie aktualnej pozycji kropki przed przesunięciem
        trail.add(new Point(x, y));
        System.out.println(1);

        // Przesunięcie pozycji na podstawie naciśniętego klawisza strzałki
        switch (e.getKeyCode()) {
            case KeyEvent.VK_LEFT:
                x -= 1;
                System.out.println(x);
                break;
            case KeyEvent.VK_RIGHT:
                x += 1;
                System.out.println(x);
                break;
            case KeyEvent.VK_UP:
                y -= 1;
                System.out.println(y);
                break;
            case KeyEvent.VK_DOWN:
                y += 1;
                System.out.println(y);
                break;
        }

        // Odświeżenie panelu, aby pokazać nową pozycję
        repaint();
    }

    @Override
    public void keyTyped(KeyEvent e) {System.out.println(2);}

    @Override
    public void keyReleased(KeyEvent e) {System.out.println(3);}
    public void paintComponent(Graphics g) {
        super.paintComponents(g);

        // Ustawienie koloru rysowania
        g.setColor(Color.BLACK);

        // Narysowanie kropki na panelu na podstawie aktualnej pozycji x i y
        g.fillOval(x, y, 10, 10);

        // Narysowanie śladu
        /*for (Point point : trail) {
            g.fillOval(point.x, point.y, 10, 10);
        }*/
    }
}

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.security.KeyPair;
import java.util.EventListener;
import java.lang.Math;
public class Window extends JFrame implements ActionListener{
    final static Color DEFAULT = new Color(0x1C, 0x1C, 0x1C);
    final static Color DARK_GREY = new Color(0x25, 0x25, 0x25);
    JPanel main_panel = new JPanel();
    JPanel chat_panel = new JPanel();
    JPanel game_panel = new JPanel();
    JPanel score_panel = new JPanel();
    JTextArea chatLabel = new JTextArea();
    JTextField chatTextField = new JTextField();
    JButton chatExitButton = new JButton("EXIT");
    JButton chatSendButton = new JButton("Send");
    JPanel left_panel = new JPanel();//chat_panel + score_panel
    Pair<Integer, Integer> windowSize = new Pair<Integer, Integer>(1280, 800);
    Pair<Double, Double> windowMargin = new Pair<Double, Double>(0.05, 0.05);
    Chat chat = new Chat();
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
        chatLabel.setBackground(Color.BLUE);

        JPanel buttonPanel = new JPanel();
        chatExitButton.setPreferredSize(new Dimension(100, 50));
        chatSendButton.setPreferredSize(new Dimension(100, 50));
        chatTextField.setPreferredSize(new Dimension(571-200-60, 50));
        buttonPanel.add(chatExitButton);
        buttonPanel.add(chatTextField);
        buttonPanel.add(chatSendButton);
        chat_panel.add(buttonPanel, BorderLayout.SOUTH);



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
    }

    public void draw()
    {
        //chat_panel

        //score_panel

        //game_panel
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        int width = chat_panel.getWidth();
        width = chat_panel.getHeight();
        String message = chatTextField.getText();
        chatTextField.setText("");
        chatLabel.setText("");
        chat.add(message);
        chatLabel.append(chat.getChat());
    }
}

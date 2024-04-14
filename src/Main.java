import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Main extends JPanel {
    private int x = 150; // Początkowa pozycja x
    private int y = 150; // Początkowa pozycja y
    private List<Point> trail = new ArrayList<>(); // Lista punktów śladu

    public Main() {
        setBackground(Color.WHITE); // Ustawienie tła panelu
        setPreferredSize(new Dimension(300, 300)); // Ustawienie preferowanej wielkości panelu

        // Dodanie nasłuchiwacza dla naciśnięcia klawiszy strzałek
        addKeyListener(new KeyListener() {
            @Override
            public void keyPressed(KeyEvent e) {
                // Zapamiętanie aktualnej pozycji kropki przed przesunięciem
                trail.add(new Point(x, y));

                // Przesunięcie pozycji na podstawie naciśniętego klawisza strzałki
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_LEFT:
                        x -= 1;
                        break;
                    case KeyEvent.VK_RIGHT:
                        x += 1;
                        break;
                    case KeyEvent.VK_UP:
                        y -= 1;
                        break;
                    case KeyEvent.VK_DOWN:
                        y += 1;
                        break;
                }

                // Odświeżenie panelu, aby pokazać nową pozycję
                repaint();
            }

            @Override
            public void keyTyped(KeyEvent e) {}

            @Override
            public void keyReleased(KeyEvent e) {}
        });

        // Ustawienie panelu jako aktywny, aby mógł otrzymywać zdarzenia klawiatury
        setFocusable(true);
        requestFocusInWindow();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Ustawienie koloru rysowania
        g.setColor(Color.BLACK);

        // Narysowanie kropki na panelu na podstawie aktualnej pozycji x i y
        g.fillOval(x, y, 10, 10);

        // Narysowanie śladu
        /*for (Point point : trail) {
            g.fillOval(point.x, point.y, 10, 10);
        }*/
    }

    public static void main(String[] args) {
        // Utworzenie ramki
        JFrame frame = new JFrame("Moving Dot Panel");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Dodanie panelu do ramki
        Main panel = new Main();
        frame.getContentPane().add(panel);

        // Ustawienie wielkości ramki i jej widoczność
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;

public class DrawingPanel extends JPanel implements KeyListener {
    private int x = 0;
    private int y = 0;
    private double angle = 0; // Początkowy kąt
    private double radius = 50; // Promień okręgu
    private List<Point> trail = new ArrayList<>();

    public DrawingPanel() {
        setPreferredSize(new Dimension(800, 600)); // Ustawienie preferowanego rozmiaru panelu rysowania
        setBackground(Color.WHITE); // Ustawienie tła panelu rysowania
        setFocusable(true); // Ustawienie panelu jako focusable, aby mógł otrzymywać zdarzenia klawiatury
        addKeyListener(this); // Dodanie KeyListenera do panelu
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Obliczenie nowych współrzędnych na podstawie kąta i promienia
        int newX = (int) (x + radius * Math.cos(angle));
        int newY = (int) (y + radius * Math.sin(angle));

        // Narysowanie kropki na panelu na podstawie nowych współrzędnych
        g.setColor(Color.BLACK);
        g.fillOval(newX, newY, 10, 10);

        // Zapamiętanie poprzednich współrzędnych jako ślad
        trail.add(new Point(newX, newY));
        for (Point point : trail) {
            g.fillOval(point.x, point.y, 10, 10);
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        // Przesunięcie kąta na podstawie naciśniętej strzałki
        switch (e.getKeyCode()) {
            case KeyEvent.VK_LEFT:
                angle -= Math.PI / 32; // Obrót w lewo (zmniejszenie kąta)
                break;
            case KeyEvent.VK_RIGHT:
                angle += Math.PI / 32; // Obrót w prawo (zwiększenie kąta)
                break;
            case KeyEvent.VK_UP:
                x = (int) (x + 5 * Math.cos(angle));
                y = (int) (y + 5 * Math.sin(angle));
                break;
        }

        // Odświeżenie panelu, aby pokazać nową pozycję
        repaint();
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyReleased(KeyEvent e) {}
}

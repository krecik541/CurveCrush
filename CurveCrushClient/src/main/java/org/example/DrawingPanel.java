package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.List;

public class DrawingPanel extends JPanel implements KeyListener {
    private int angle = 0;
    private Image offscreenImage;
    private Graphics offscreenGraphics;

    public DrawingPanel() {
        setPreferredSize(new Dimension(800, 600));
        setBackground(Color.WHITE);
        setFocusable(true);
        addKeyListener(this);
    }

    /**
     * Wyrysowuje aktualne pozycje graczy na planszy
     * @param playerMove lista zawierająca kolory graczy oraz ich pozycje do wyrysowania
     */
    public void setThingsToDraw(List<Pair<Pair<Integer, Integer>, Color>> playerMove) {
        if (offscreenGraphics == null) {
            return;
        }

        for (Pair<Pair<Integer, Integer>, Color> player : playerMove) {
            offscreenGraphics.setColor(player.y);
            offscreenGraphics.fillOval(player.x.x, player.x.y, 8, 8);
        }

        repaint();
    }

    /**
     * Czyszczenie planszy
     */
    public void cleanGameBoard() {
        if (offscreenGraphics != null) {
            offscreenGraphics.setColor(Color.WHITE);
            offscreenGraphics.fillRect(0, 0, getWidth(), getHeight());
        }
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (offscreenImage == null) {
            offscreenImage = createImage(getWidth(), getHeight());
            offscreenGraphics = offscreenImage.getGraphics();
            offscreenGraphics.setColor(getBackground());
            offscreenGraphics.fillRect(0, 0, getWidth(), getHeight());
        }

        g.drawImage(offscreenImage, 0, 0, this);
    }

    /**
     * Obsługuje poruszanie zmianę kąta ruchu gracza
     * @param e the event to be processed
     */
    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_LEFT:
                angle = -1;
                break;
            case KeyEvent.VK_RIGHT:
                angle = 1;
                break;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // not used
    }

    @Override
    public void keyReleased(KeyEvent e) {
        angle = 0;
    }

    public int getAngle() {
        return angle;
    }
}

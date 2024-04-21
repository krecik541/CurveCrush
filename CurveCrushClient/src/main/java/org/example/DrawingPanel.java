package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

public class DrawingPanel extends JPanel implements KeyListener {
    private double angle = 0;
    private double radius = 50;
    Player player;
    private List<Pair<Integer, Integer>> trail = new ArrayList<>();
    public DrawingPanel() {
        setPreferredSize(new Dimension(800, 600));
        setBackground(Color.WHITE);
        setFocusable(true);
        addKeyListener(this);
    }

    public void addTrail(Pair<Integer, Integer> point)
    {
        trail.add(point);
        repaint();
    }

    public void setPlayer(Player player)
    {
        this.player = player;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Pair<Integer, Integer>pos = player.getPosition();
        //pos.x = (int) (pos.x + radius * Math.cos(angle));
        //pos.y = (int) (pos.y + radius * Math.sin(angle));

        g.setColor(Color.BLACK);
        g.fillOval(pos.x, pos.y, 10, 10);

        trail.add(new Pair<>(pos.x, pos.y));
        ListIterator<Pair<Integer, Integer>> iterator = trail.listIterator();
        while (iterator.hasNext()) {
            Pair<Integer, Integer> point = iterator.next();
            g.fillOval(point.x, point.y, 10, 10);
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_LEFT:
                player.updateAngle(-Math.PI / 32);
                break;
            case KeyEvent.VK_RIGHT:
                player.updateAngle(Math.PI / 32);
                break;
            case KeyEvent.VK_UP:
                player.updatePosition();
                break;
        }

        repaint();
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {}
}

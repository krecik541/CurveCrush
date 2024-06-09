package org.example;

import java.awt.*;
import java.io.Serializable;
import java.util.Random;

public class Player implements Serializable {
    private static final long serialVersionUID = 1L; // Zaleca się dodanie serialVersionUID dla klas serializowanych

    private String name;
    private int score;
    private boolean isAlive;
    private Pair<Integer, Integer> position;
    private double angle;
    private Color color;
    private double speed = 1.5;
    private transient double x = 0; // Nie jest konieczne do serializacji, więc oznaczamy jako transient
    private transient double y = 0; // Nie jest konieczne do serializacji, więc oznaczamy jako transient
    private boolean isReady = false;

    public Player(String name) {
        this.name = name;
        reset();
    }

    public Player(String name, int score, boolean isAlive, Color color, double angle) {
        this.name = name;
        this.score = score;
        this.isAlive = isAlive;
        this.color = color;
        this.angle = angle;
        this.position = new Pair<>(0, 0);
    }

    public void reset() {
        Random random = new Random(System.currentTimeMillis());
        score = 0;
        isAlive = false;
        color = new Color(random.nextInt(255), random.nextInt(255), random.nextInt(255));
        angle = 0;
        position = new Pair<>(0, 0); // Resetujemy również pozycję
        x = 0; // Resetujemy pozycję x
        y = 0; // Resetujemy pozycję y
    }

    public void updatePosition() {
        x += Math.cos(Math.abs(angle)) * speed;
        y += Math.sin(Math.abs(angle)) * speed;
        position.x = (int) (position.x + (int) x);
        position.y = (int) (position.y + (int) y);
        if (x >= 1 || x <= -1)
            x %= 1;
        if (y >= 1 || y <= -1)
            y %= 1;
        angle %= (Math.PI * 2);
        System.out.println(name + " pos: " + x + " " + y);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isAlive() {
        return isAlive;
    }

    public void setAlive(boolean alive) {
        isAlive = alive;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public int getScore() {
        return score;
    }

    public void increaseScore() {
        score++;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public Pair<Integer, Integer> getPosition() {
        return position;
    }

    public void setPosition(Pair<Integer, Integer> position) {
        this.position = position;
    }

    public double getAngle() {
        return angle;
    }

    public void setAngle(double angle) {
        this.angle = angle;
    }

    public void updateAngle(double angle) {
        this.angle += angle;
    }

    public void setReady(boolean ready) {
        isReady = ready;
    }

    public boolean isReady() {
        return isReady;
    }
}

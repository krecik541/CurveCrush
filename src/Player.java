import java.awt.*;
import java.awt.event.KeyListener;
import java.util.Random;

public class Player{
    private Pair<Integer, Integer>position;
    private boolean isAlive;
    //powerupy
    private int score;
    private double angle;
    private Color color;// w przyszłości zmienne kolory
    private String name;
    Player(String name)
    {
        this.name = name;
        reset();
    }

    public void reset()
    {
        Random random = new Random(System.currentTimeMillis());
        score = 0;
        isAlive = false;
        color = new Color(random.nextInt(255), random.nextInt(255), random.nextInt(255));
        angle = 0;
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
}

import java.awt.*;
import java.util.Random;

public class Player{
    Random random = new Random(System.currentTimeMillis());
    private String name;
    private int score;
    private boolean isAlive;
    private Pair<Integer, Integer>position;
    private Pair<Integer, Integer>lastPosition;
    private double angle;
    private Color color;
    private final double change = Math.PI / 54;
    private double speed = 3;
    /**
     * x opisuje ile trwa przerwa w śladzie
     *  y opisuje ile do następnej przerwy
     */
    private final Pair<Integer, Integer>trailBreak= new Pair<>(random.nextInt(10, 18), random.nextInt(50, 150));// ile trwa przerwa, za ile ma nastąpić następna przerwa
    double x = 0;
    double y = 0;
    private boolean isReady = false;


    Player(String name)
    {
        this.name = name;
        reset();
    }

    Player(String name, int score, boolean isAlive, Color color, double angle)
    {
        this.name = name;
        this.score = score;
        this.isAlive = isAlive;
        this.color = color;
        this.angle = angle;
        this.position = new Pair<>(random.nextInt(100, 460), random.nextInt(100, 592));
        this.lastPosition = new Pair<>(500, 600);
    }
    public void reset()
    {
        this.isAlive = true;
        this.color = new Color(random.nextInt(255), random.nextInt(255), random.nextInt(255));
        this.angle = random.nextInt(0, 360);
        this.position = new Pair<>(random.nextInt(100, 460), random.nextInt(100, 592));
        this.lastPosition = new Pair<>(500, 600);
        this.trailBreak.x = random.nextInt(10, 18);
        this.trailBreak.y = random.nextInt(50, 150);
    }

    public void updatePosition()
    {
        lastPosition.x = position.x;
        lastPosition.y = position.y;
        x += Math.cos(angle) * speed;
        y += Math.sin(angle) * speed;
        position.x = (int) (position.x + (int) x);
        position.y = (int) (position.y + (int) y);
        if(x >= 1 || x <= -1)
            x %= 1;
        if(y >= 1 || y <= -1)
            y %= 1;
        if(position.x < 0 || position.x > 560 || position.y < 0 || position.y > 692)
            isAlive = false;


        if(trailBreak.y <= 0) {
            trailBreak.x = random.nextInt(10, 18);
            trailBreak.y = random.nextInt(50, 150) + trailBreak.x;
        }
        trailBreak.x--;
        trailBreak.y--;

//        angle %= (Math.PI * 2);
//        System.out.println(angle);
//        System.out.println(name + " pos: " + position.x + " " + position.y);
    }

    public void updateLastPosition()
    {
        lastPosition.x = position.x;
        lastPosition.y = position.y;
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
    public void increaseScore(){score++;}

    public void setScore(int score) {
        this.score = score;
    }

    public Pair<Integer, Integer> getPosition() {
        return position;
    }
    public Pair<Integer, Integer> getLastPosition() {
        return lastPosition;
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
    public void updateAngle(int ang) {

        switch (ang)
        {
            case 1:
                angle += change;
                break;
            case -1:
                angle -= change;
                break;
            default:
                break;
        }
    }

    public void setReady(boolean ready) {
        isReady = ready;
    }

    public boolean isReady() {
        return isReady;
    }
    public Pair<Integer, Integer> getTrailBreak()
    {
        return trailBreak;
    }
}

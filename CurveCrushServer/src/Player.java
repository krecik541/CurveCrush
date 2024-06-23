import java.awt.*;
import java.util.Random;

public class Player{
    Random random = new Random(System.currentTimeMillis());
    /**
     * Nick gracza
     */
    private String name;
    /**
     * Wynik gracza
     */
    private int score;
    /**
     * Czy gracz jest żywy w obecnej rundzie
     */
    private boolean isAlive;
    /**
     * Aktualna pozycja gracza
     */
    private Pair<Integer, Integer>position;
    /**
     * Pozycja gracza jedną aktualizację wstecz,
     * używane do zakolorywyania pozycji gracza w momencie, gdy ma nastąpić przerwa w śladzie
     */
    private Pair<Integer, Integer>lastPosition;
    /**
     * Kąt, w stronę którego patrzy ślad
     */
    private double angle;
    /**
     * Kolor gracza
     */
    private Color color;
    /**
     * Zmiana kąta przy pojedynczej aktualizacji
     */
    private final double change = Math.PI / 54;
    /**
     * Prędkość poruszania się
     */
    private final double speed = 3;
    /**
     * x opisuje ile trwa przerwa w śladzie
     *  y opisuje ile do następnej przerwy
     */
    private final Pair<Integer, Integer>trailBreak= new Pair<>(random.nextInt(10, 18), random.nextInt(50, 150));// ile trwa przerwa, za ile ma nastąpić następna przerwa
    /**
     * Dopełnienie do zmiany pozycji x, pozwala na dokładniejsze prowadzenie śladu
     */
    double x = 0;
    /**
     * Dopełnienie do zmiany pozycji y, pozwala na dokładniejsze prowadzenie śladu
     */
    double y = 0;
    /**
     * Flaga oznaczająca gotowość gracza
     */
    private boolean isReady = false;


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

    /**
     * Resetowanie atrybutów gracza, wykorzystywane przy nowej rundzie
     */
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

    /**
     * Aktualizacja pozycji gracza
     */
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
    }

    public String getName() {
        return name;
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

    public int getScore() {
        return score;
    }
    public void increaseScore(){score++;}


    public Pair<Integer, Integer> getPosition() {
        return position;
    }
    public Pair<Integer, Integer> getLastPosition() {
        return lastPosition;
    }

    /**
     * Aktualizacja kąta
     * @param ang  1 jeśli zmiana kąta ma nastąpić w prawo, 0 jeśli brak zmiany, -1 jeśli w lewo
     */
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

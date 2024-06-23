import java.util.*;

public class Game extends Thread {
    /**
     * Lista graczy
     */
    private final Queue<Player>playerList = new LinkedList<Player>();
    /**
     * Liczba żywych graczy przy poprzedniej aktualizacji
     */
    private int playersLastAlive;
    /**
     * Flaga oznaczająca koniec gry
     */
    private boolean isGameOver = false;
    /**
     * Flaga oznaczająca koniec rundy
     */
    private boolean isRoundOver = true;
    /**
     * Liczba odświeżeń na sekunde -> częstotliwość odświeżania stanu gry = 1000 / refresh(ms)
     */
    final static int refresh = 25;
    /**
     * Wynik punktowy, zapewniający wygraną danego gracza
     */
    final static int winingCondition = 10;
    /**
     * Plansza, na której toczy się rozgrywka
     */
    private boolean [][]board = new boolean[650][800];

    /**
     * Funkcja, która sprawdza, czy aktualna pozycja gracza przecina się z wcześniej pozostawionym śladem
     * @param pos rozpatrywana pozycja
     * @param trail jeśli gracz aktualnie nie ma zostawiać śladu, to ten slad nie będzie zapisywany na planszy
     * @return true jeśli ślad może zostać poprawnie postawiony, false w przeciwnym wypadku
     */
    public boolean placeColor(Pair<Integer, Integer> pos, boolean trail)
    {
        int count = 0;
        for(int i=0; i<11; i++)
        {
            for(int j=0; j<11; j++)
            {
                if((i == 0 && (j == 0 || j == 1 || j == 9 || j == 10)) || (i == 1 && (j == 0 || j == 1 || j == 9 || j == 10)) || (i == 9 && (j == 0 || j == 1 || j == 9 || j == 10)) || (i == 10 && (j == 0 || j == 1 || j == 9 || j == 10)))
                    continue;
                if(!board[i + pos.x+5][j + pos.y+5])
                {
                    if(trail)
                        board[i + pos.x+5][j + pos.y+5] = true;

                }
                else
                    count++;
            }
        }

        return count <= 85;
    }

    /**
     * Funkcja jest odpowiedzialna za aktualizację stanu gry, w tym: pozycje
     * graczy oraz tabela z wynikami
     */
    public synchronized void update()
    {
        // Licznik obecnie żywych graczy
        int playersAlive = 0;
        synchronized (playerList) {
            // Aktualizacja pozycji graczy
            for (Player player : playerList) {
                if (player.isAlive()) {
                    playersAlive++;
                    // Aktualizacja pozycji
                    player.updatePosition();
                    // Jeśli ślad nie może być poprawnie postawiony, oznacza to, że gracz wszedł w ścianę lub
                    // w inny ślad, a zatem gracz umiera
                    if(!placeColor(player.getPosition(), player.getTrailBreak().x <= 0))
                    {
                        player.setAlive(false);
                    }
                }
            }
        }

        // Jeśli któryś z graczy umarł dodawane są punkty reszcie graczy
        if(playersAlive != playersLastAlive) {
            for(Player player:playerList)
            {
                if (player.isAlive())
                    player.increaseScore();
                // Jeśli osiągnięty zostanie próg zwycięstwa gra się kończy
                if(player.getScore() >= winingCondition)
                {
                    isGameOver = true;
                }

            }
            // Aktualizacja liczby żywych graczy
            playersLastAlive = playersAlive;
        }
        if(playersAlive <= 1)
            isRoundOver = true;


        try {
            Thread.sleep(10);
        }
        catch (InterruptedException e)
        {
            throw new RuntimeException(e);
        }

        // Wybudzenie wątków obsługujących komunikację
        notifyAll();
    }

    /**
     * Gra nie powinna startować przed, tym jak jest co najmniej 2 graczy
     * oraz każdy z nich jest gotowy
     *
     * @return zwracana jest prawda, jeśli jest co najmniej 2 graczy i każdy z nich zgłosił gotowość
     */
    public synchronized boolean isEveryoneReady()
    {
        synchronized (playerList) {
            if(playerList.size() < 2)
                return false;
            for (Player player : playerList) {
                if (!player.isReady())
                    return false;
            }
        }
        System.out.println("Everyone is ready!");

        isGameOver = false;
        isRoundOver = false;
        return true;
    }

    /**
     * Główna pętla gry, jedno przejście przez funkcje jest równoznaczne z jedną rundą
     */
    public void gameLoop()
    {
        while (!isRoundOver) {
            try {
                Thread.sleep(refresh);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            synchronized (this) {
                update();
            }
        }
    }

    /**
     * Funkcja odpowiadająca za rozpoczęcie rozgrywki zgłoszeniu gotowości przez każdego z graczy
     * a także zresetowanie stanu gry przed każdą nową rundą
     */
    @Override
    public void run() {
        // Oczekiwanie na gotowość graczy
        while(!isEveryoneReady());

        // Wywołanie pętli gry
        while (!isGameOver) {
            // Reset stanu gry
            isRoundOver = false;
            board = new boolean[650][800];
            playersLastAlive = playerList.size();
            synchronized (playerList) {
                for (Player pl : playerList)
                    pl.reset();
            }

            gameLoop();

            try{
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public boolean isGameOver() {
        return isGameOver;
    }
    public boolean isRoundOver() {
        return isRoundOver;
    }

    public Queue<Player>getPlayerList()
    {
        return playerList;
    }

    public synchronized void addToPlayerList(Player player)
    {
        playerList.add(player);
    }
}

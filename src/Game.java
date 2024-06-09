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
     * Uchwyt chatu
     */
    private final List<Pair<String, String>> chat;
    /**
     * Flaga oznaczająca koniec gry
     */
    private boolean isGameOver = false;
    private boolean isRoundOver = true;
    /**
     * Liczba odświeżeń na sekunde -> częstotliwość odświeżania stanu gry = refresh(ms)
     */
    final static int refresh = 25;

    final static int winingCondition = 1;
    private boolean [][]board = new boolean[650][800];
    public Game(List<Pair<String, String>> chat)
    {
        this.chat = chat;
    }

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
     *
     * TODO
     * ilość rund
     * zapisywanie wyników do bazy danych
     * próg punktowy do zakończenia gry
     */
    public synchronized void update()
    {
        int playersAlive = 0;
        synchronized (playerList) {
            for (Player player : playerList) {
                if (player.isAlive()) {
                    playersAlive++;
                    player.updatePosition();
                    if(!placeColor(player.getPosition(), player.getTrailBreak().x <= 0))
                    {
                        player.setAlive(false);
                    }
                }
            }
        }

        if(playersAlive != playersLastAlive) {
            // wyniki
            // połącznenie z bazą danych, która będzie przechowywać wyniki
            for(Player player:playerList)
            {
                if (player.isAlive())
                    player.increaseScore();
                if(player.getScore() >= winingCondition)
                {
                    isGameOver = true;
                }

            }
            playersLastAlive = playersAlive;
        }
        if(playersAlive == 1)
            isRoundOver = true;
//            isGameOver = true;
        // Czyszczenie chatu tymczasowego
        try {
            Thread.sleep(10);
        }
        catch (InterruptedException e)
        {
            throw new RuntimeException(e);
        }
//        synchronized (chat) {
//
//            chat.clear();
//        }

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
//            if (playerList.size() < 1)
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

    // Pętla gry
    public void gameLoop()
    {
        synchronized (playerList) {
            for (Player pl : playerList)
                pl.setAlive(true);
        }
        try {
            while (!isGameOver) {
                playersLastAlive = playerList.size();
                while (!isRoundOver) {
                    Thread.sleep(refresh);
                    synchronized (this) {
                        update();
                    }
                }
                for(Player player:playerList)
                {
                    player.reset();
                }
                for(int i=5; i>0; i--) {
                    Thread.sleep(1000);
//                    chat.add(new Pair<>("Gra rozpocznie sie za ", i + " sekund"));
                }

                isRoundOver = false;
                board = new boolean[650][800];
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void run() {
        // Na razie działa tylko jednorazowo, dodaj później także mechanizm nowej rundy
        while(!isEveryoneReady());

        gameLoop();
    }

    public synchronized void setGameOver(boolean isGameOver)
    {
        this.isGameOver = isGameOver;
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

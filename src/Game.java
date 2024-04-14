import java.util.*;

public class Game {
    Server server;
    Chat chat;
    Window window = new Window();
    List<Player>playerList = new LinkedList<Player>();
    boolean isGameOver = false;

    public Game(Server server){
        this.server = server;
    }

    public List<Player>getPlayerList()
    {
        return playerList;
    }

    public void addToPlayerList(Player player)
    {
        playerList.add(player);
    }

    public void update()
    {
        // stan planszy

        // chat

        // wyniki
    }

    public void loop()
    {
        while(!isGameOver)
        {
            update();
            window.draw();
        }
    }

}

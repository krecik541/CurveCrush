import java.util.*;

public class Game {
    Server server;
    Chat chat;
    Window window;
    List<Player>playerList = new LinkedList<Player>();
    boolean isGameOver = false;

    /*public Game(Server server, Window window){
        this.server = server;
        this.window = window;
    }*/
    public Game(Window window){
        this.window = window;
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
        int i = 0;
        while(!isGameOver)
        {
            i++;
            try{
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            for(Player player:playerList)
            {
                if (player.isAlive()){
                    Pair<Integer, Integer> position = player.getPosition();
                    Double angle = player.getAngle();

                    position.x = (int) (position.x + 5 * Math.cos(angle));
                    position.y = (int) (position.y + 5 * Math.sin(angle));
                    player.setPosition(position);
                }
            }
            update();
            window.draw();
        }
    }

}

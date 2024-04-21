import java.util.*;

public class Game {
    Server server;
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
        for(Player player:playerList)
        {
            if (player.isAlive()){
                player.updatePosition();
                System.out.println(player.getPosition() + " " + player.getAngle());
            }
        }

        // wyniki
    }

    public void loop()
    {
        int i = 0;
        while(!isGameOver)
        {
            i++;
            try{
                Thread.sleep(1000/60);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            update();
        }
    }

}

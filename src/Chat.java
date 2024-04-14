import java.sql.Array;
import java.util.*;
public class Chat {
    Queue<String> chat = new LinkedList<>();

    public void add(String message)
    {
        /*if(chat.size() == 10)
            chat.remove(chat.peek());*/
        chat.add(message);
    }

    public String getChat(){
        String s = "";
        for(String ss : chat)
            s = s.concat(ss + "\n");
        return s;
    }
}
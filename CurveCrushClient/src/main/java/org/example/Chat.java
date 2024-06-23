package org.example;

import java.sql.Array;
import java.util.*;
public class Chat {
    /**
     * Struktura przechowująca czat
     */
    private final Queue<String> chat = new LinkedList<>();
    /**
     * Wysyłany komunikat/wiadomość
     */
    private String message;

    public void add(String message)
    {
        chat.add(message);
    }

    public void clear()
    {
        chat.clear();
    }

    public String getChat(){
        String s = "";
        for(String ss : chat)
            s = s.concat(ss + "\n");
        return s;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
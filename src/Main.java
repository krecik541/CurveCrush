import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Main {

    public static void main(String[] args) {
        Window window = new Window();

        Game game = new Game(window);
        game.addToPlayerList(new Player("koń"));
        game.addToPlayerList(new Player("słoń"));
        game.addToPlayerList(new Player("pies"));
        game.addToPlayerList(new Player("ja"));

        window.setGame(game);
        game.loop();
    }
}
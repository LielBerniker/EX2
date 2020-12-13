package gameClient;

import javax.swing.*;
import java.awt.*;

public class frame extends JFrame {
    Panel panel;

    public frame(String a){
        super(a);
        this.setTitle("pokemon game");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setBackground(Color.pink);
        panel = new Panel();
        this.add(panel);

        this.setVisible(true);


    }
}

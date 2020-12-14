package gameClient;

import javax.swing.*;
import java.awt.*;

public class frame extends JFrame {
    Panel panel;

    public frame(String a){
        super(a);
        this.setTitle("liel & rivka");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setBackground(new Color(171, 212, 210));
        panel = new Panel();
        this.add(panel);

        this.setVisible(true);


    }
}

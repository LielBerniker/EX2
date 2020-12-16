package gameClient;

import javax.swing.*;
import java.awt.*;


public class frame extends JFrame {
    Panel panel;


    public frame(String a){
        super(a);
        this.setTitle("liel & rivka");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setBackground(Color.BLACK);
       ImageIcon image =new ImageIcon("./src/resources/pocadoor.jpg");
        this.setIconImage(image.getImage());

        panel = new Panel();
        this.add(panel);
        this.setVisible(true);

    }


}

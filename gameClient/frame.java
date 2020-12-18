package gameClient;

import javax.swing.*;
import java.awt.*;


public class frame extends JFrame {
    enteryPanel Epanel;
    Panel panel;

    /**
     * making gui frame and adding panel to it
     *
     */
    public frame(){
        super();
        this.setTitle("liel&rivka");

        //closing the program when closing gui window.
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.setBackground(Color.BLACK);
       ImageIcon image =new ImageIcon("./src/resources/pocadoor.jpg");
        this.setIconImage(image.getImage());

        this.setVisible(true);

    }


}

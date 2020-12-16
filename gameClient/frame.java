package gameClient;

import javax.swing.*;
import java.awt.*;


public class frame extends JFrame {
    Panel panel;

    /**
     * making gui frame and adding panel to it
     * @param a
     */
    public frame(String a){
        super(a);
        this.setTitle("liel&rivka");

        //closing the program when closing gui window.
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.setBackground(Color.BLACK);
       ImageIcon image =new ImageIcon("./src/resources/pocadoor.jpg");
        this.setIconImage(image.getImage());

        //adding the panel to the frame.
        panel = new Panel();
        this.add(panel);
        this.setVisible(true);

    }


}

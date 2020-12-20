package gameClient;

import javax.swing.*;
import java.awt.*;

/**
 * making gui frame
 */
public class frame extends JFrame {

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

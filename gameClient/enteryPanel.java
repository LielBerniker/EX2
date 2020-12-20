package gameClient;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;


public class enteryPanel extends JPanel implements ActionListener {

    JButton boton;
    JLabel L_level;
    JTextField T_level;
    JLabel L_id;
    JTextField T_id;
    JLabel title;

    /**
     * this class represent a log in panel to the game which will be added  to the frame
     *
     */
    public enteryPanel() {
        super();
        this.setLayout(null);
        this.setBackground(new Color(218, 222, 248));
        title=new JLabel("the pokemons game");
        title.setBounds(360,5,700,350);
        this.add(title);

        boton = new JButton( "start game");
        boton.setBounds(100, 300, 100, 50);
        this.add(boton);
        boton.addActionListener(this);

        L_level = new JLabel("enter level");
        L_level.setBounds(330, 330, 130, 80);
        this.add(L_level);
        T_level = new JTextField();
        T_level.setBounds(300, 300, 100, 50);
        T_level.setBackground(Color.GRAY);
        T_level.setCaretColor(Color.white);
        this.add(T_level);

        L_id = new JLabel("enter id");
        L_id.setBounds(570, 330, 130, 80);
        this.add(L_id);

        T_id = new JTextField();
        T_id.setBounds(530, 300, 100, 50);
        T_id.setBackground(Color.GRAY);
        T_id.setCaretColor(Color.black);
        this.add(T_id);


    }


    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource()==boton){

            try {
                Ex2.scenario = Integer.parseInt(T_level.getText());
                Ex2.id = Integer.parseInt(T_id.getText());

                Ex2.user.start();
            }catch (NumberFormatException ne){
                System.out.println("invalid id");

            }

        }

    }








}

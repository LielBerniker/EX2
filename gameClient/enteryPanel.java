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


    public enteryPanel() {
        super();
        this.setLayout(null);

        boton = new JButton( "start game");
        boton.setBounds(100, 100, 100, 50);
        this.add(boton);
        boton.addActionListener(this);

        L_level = new JLabel("enter level");
        L_level.setBounds(330, 130, 130, 80);
        this.add(L_level);
        T_level = new JTextField();
        T_level.setBounds(300, 100, 100, 50);
        T_level.setBackground(Color.GRAY);
        T_level.setCaretColor(Color.black);
        this.add(T_level);

        L_id = new JLabel("enter id");
        L_id.setBounds(570, 130, 130, 80);
        this.add(L_id);

        T_id = new JTextField();
        T_id.setBounds(530, 100, 100, 50);
        T_id.setBackground(Color.GRAY);
        T_id.setCaretColor(Color.black);
        this.add(T_id);


    }


    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource()==boton){

          Ex2.scenario=Integer.parseInt(T_level.getText());
          Ex2.id=Integer.parseInt(T_id.getText());
          Ex2.user.start();

        }

    }








}

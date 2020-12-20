package gameClient;

import Server.Game_Server_Ex2;
import api.*;
import gameClient.util.Point3D;
import gameClient.util.Range;
import gameClient.util.Range2D;
import gameClient.util.Range2Range;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ImageConsumer;
import java.awt.image.ImageObserver;
import java.awt.image.ImageProducer;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * This class represents a very simple GUI class to present a
 * game on a graph
 * bases on the code of boaz.benmoshe
 *
 */
public class Panel extends JPanel{
    private int _ind;
    private Arena ar;
    private Range2Range _w2f;



    public  Panel() {
        super();
        this.setLayout(null);
        int _ind = 0;

    }

    /**
     * updating the arena by calling updateFrame()
     * @param  ar
     */
    public void update(Arena ar) {
        this.ar = ar;
        updateFrame();

    }

    /**
     * setting panel range
     * making adjustment between world range and frame range
     */
    private void updateFrame() {
        Range rx = new Range(20,this.getWidth()-20);
        Range ry = new Range(this.getHeight()-10,150);
        Range2D frame = new Range2D(rx,ry);
        directed_weighted_graph g = this.ar.getGraph();
        _w2f = Arena.w2f(g,frame);
    }

    /**
     *  build the arena
     * @param g
     */
    public void paint(Graphics g) {

        updateFrame();

        drawGraph(g);
        drawPokemons(g);
        drawAgants(g);
        drawInfo(g);
        drawClock(g);
        drawTitle(g);
        drawlevel(g);


    }

    /**
     *
     * @param g
     */
    private void drawInfo(Graphics g) {
        List<String> info = this.ar.get_info();
        String dt = "none";
        for(int i=0;i<info.size();i++) {
           // g.drawString(info.get(i)+" dt: "+dt,100,60+i*20);
            g.drawString(""+info.get(i),100,60+i*20);


        }

    }

    /**
     * drawing the graph with drawNode and drawEdge
     * @param g
     */
    private void drawGraph(Graphics g) {
        directed_weighted_graph gg = this.ar.getGraph();
        Iterator<node_data> node_iter = gg.getV().iterator();
        while(node_iter.hasNext()) {
            node_data n = node_iter.next();
            g.setColor(Color.white);
            drawNode(n,6,g);
            Iterator<edge_data> edge_iter = gg.getE(n.getKey()).iterator();
            while(edge_iter.hasNext()) {
                edge_data e = edge_iter.next();
                g.setColor(Color.white);
                drawEdge(e, g);

            }
        }
    }

    /**
     * drawing the Pokemons and their value on the graph
     * draws different types of pokemons according to their position on the graph edges.
     * @param g
     */
    private void drawPokemons(Graphics g) {
        List<CL_Pokemon> pokemons = this.ar.getPokemons();
        if(pokemons!=null) {
            Iterator<CL_Pokemon> itr = pokemons.iterator();

            while(itr.hasNext()) {

                CL_Pokemon poki = itr.next();
                Point3D c = poki.getLocation();
                int r=20;
                if(c!=null) {
                    geo_location location = this._w2f.world2frame(c);
                    //if src of pokemon edge is grater then the dest
                    if (poki.getType() < 0) {
                        try {
                            BufferedImage img = ImageIO.read(new File("./src/resources/pica.png"));
                            g.drawImage(img, (int) location.x()-r , (int) location.y() - r, 25, 25, null);
                            g.setColor( new Color(0xE5E5EA));
                            g.setFont(new Font("Wide Latin",Font.BOLD,10));
                            g.drawString("val-"+poki.getValue(),(int)location.x()-20, (int)location.y()-22 );
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                        //if dest grater then src
                    }else{
                        try {
                            BufferedImage img = ImageIO.read(new File("./src/resources/balvazor.jpg"));
                            g.drawImage(img, (int) location.x()-r , (int) location.y()-r , 25, 25, null);
                            g.setColor( new Color(0xE5E5EA));
                            g.setFont(new Font("Wide Latin",Font.BOLD,10));
                            g.drawString("val-"+poki.getValue(),(int)location.x()-20, (int)location.y()-22 );
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }

                    }
                }

                }
            }
        }

    /**
     * drawing the agents and their id on the graph
     * @param g
     */
    private void drawAgants(Graphics g) {
        HashMap<Integer,CL_Agent> agents_colection = this.ar.get_Agents_info();

        if(agents_colection!=null) {
            for (CL_Agent agent : agents_colection.values()) {

                geo_location gl = agent.getLocation();
                int r = 8;

                if (gl != null) {
                    //operating world_to_frame on the agent geo_location
                    geo_location agent_loc = this._w2f.world2frame(gl);

                    try {
                        BufferedImage img = ImageIO.read(new File("./src/resources/ash face.gif"));
                        g.drawImage(img, (int) agent_loc.x() - r, (int) agent_loc.y() - r, 30, 30, null);

                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                    g.setColor(new Color(0xDEECEA));
                    g.drawString("" + agent.getValue(), (int) agent_loc.x() - r, (int) agent_loc.y() - r);
                }
            }
        }
    }

    /**
     * drawing node of the graph
     * @param n
     * @param r
     * @param g
     */
    private void drawNode(node_data n, int r, Graphics g) {
        geo_location pos = n.getLocation();
        geo_location gl = this._w2f.world2frame(pos);
        g.fillOval((int)gl.x()-r, (int)gl.y()-r, 2*r, 2*r);
        g.drawString(""+n.getKey(), (int)gl.x(), (int)gl.y()-4*r);
    }

    /**
     * drawing edge of the graph
     * @param e
     * @param g
     */
    private void drawEdge(edge_data e, Graphics g) {
        directed_weighted_graph gg = this.ar.getGraph();
        geo_location s = gg.getNode(e.getSrc()).getLocation();
        geo_location d = gg.getNode(e.getDest()).getLocation();
        geo_location s0 = this._w2f.world2frame(s);
        geo_location d0 = this._w2f.world2frame(d);
        g.drawLine((int)s0.x(), (int)s0.y(), (int)d0.x(), (int)d0.y());

    }

    /**
     * add clock to arena showing the remaining time to the game
     * @param g
     */
    private void drawClock(Graphics g){

        g.setColor(new Color(65, 173, 69));
        g.drawOval(20,20,60,60);
        g.setColor(new Color(246, 242, 242));
        g.setFont(new Font("Wide Latin", Font.BOLD, 20) );
        g.drawString(""+ar.getTime(),38,57);

    }

    /**
     * adding title to arena
     * @param g
     */
    private void drawTitle(Graphics g)  {

    try {
        BufferedImage img = ImageIO.read(new File("./src/resources/Pokemon-Symbol.jpg"));

        g.drawImage(img,300,10,180,100,null);
    }
    catch (IOException ex){
        ex.printStackTrace();
    }

}
private void drawlevel(Graphics g){
    g.setColor(new Color(246, 242, 242));
    g.setFont(new Font("Wide Latin", Font.BOLD, 18) );
    g.drawString("level: "+Ex2.scenario,this.getWidth()-100,57);
}

}

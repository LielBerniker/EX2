package gameClient;

import Server.Game_Server_Ex2;
import api.*;
import gameClient.util.Point3D;
import gameClient.util.Range;
import gameClient.util.Range2D;
import gameClient.util.Range2Range;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.Iterator;
import java.util.List;

/**
 * This class represents a very simple GUI class to present a
 * game on a graph - you are welcome to use this class - yet keep in mind
 * that the code is not well written in order to force you improve the
 * code and not to take it "as is".
 *
 */
public class Panel extends JPanel{
    private int _ind;
    private Arena ar;
    private Range2Range _w2f;
    TitledBorder border = new TitledBorder("This is my title");


    public  Panel() {
        int _ind = 0;

    }


    public void update(Arena ar) {
        this.ar = ar;
        updateFrame();


    }

    private void updateFrame() {
        Range rx = new Range(20,this.getWidth()-20);
        Range ry = new Range(this.getHeight()-10,150);
        Range2D frame = new Range2D(rx,ry);
        directed_weighted_graph g = this.ar.getGraph();
        _w2f = Arena.w2f(g,frame);
    }


    public void paint(Graphics g) {
        int w = this.getWidth();
        int h = this.getHeight();

        updateFrame();
        drawPokemons(g);
        drawGraph(g);
        drawAgants(g);
        drawInfo(g);
        clock(g);
        drawTitle(g);


    }
    private void drawInfo(Graphics g) {
        List<String> info = this.ar.get_info();
        String dt = "none";
        for(int i=0;i<info.size();i++) {
           // g.drawString(info.get(i)+" dt: "+dt,100,60+i*20);
            g.drawString(""+info.get(i),100,60+i*20);

            g.setColor(new Color(194, 53, 53));
            g.drawString("pokemon game",20,-20);

        }

    }
    private void drawGraph(Graphics g) {
        directed_weighted_graph gg = this.ar.getGraph();
        Iterator<node_data> iter = gg.getV().iterator();
        while(iter.hasNext()) {
            node_data n = iter.next();
            g.setColor(Color.white;
            drawNode(n,5,g);
            Iterator<edge_data> itr = gg.getE(n.getKey()).iterator();
            while(itr.hasNext()) {
                edge_data e = itr.next();
                g.setColor(Color.white);
                drawEdge(e, g);

            }
        }
    }
    private void drawPokemons(Graphics g) {
        List<CL_Pokemon> pokemons = this.ar.getPokemons();
        if(pokemons!=null) {
            Iterator<CL_Pokemon> itr = pokemons.iterator();

            while(itr.hasNext()) {

                CL_Pokemon poki = itr.next();
                Point3D c = poki.getLocation();
                int r=10;
                g.setColor(Color.green);
                if(poki.getType()<0) {g.setColor(Color.orange);}
                if(c!=null) {

                    geo_location location = this._w2f.world2frame(c);
                    g.fillOval((int)location.x()-r, (int)location.y()-r, 2*r, 2*r);
                    g.setColor( new Color(0x0C0C10));
                    g.drawString("val-"+poki.getValue(),(int)location.x()-2*r, (int)location.y()-r );
                }
            }
        }
    }
    private void drawAgants(Graphics g) {
        List<CL_Agent> rs = this.ar.getAgents();
        //	Iterator<OOP_Point3D> itr = rs.iterator();

        int i=0;
        while(rs!=null && i<rs.size()) {
            g.setColor(Color.red);
            CL_Agent agent=rs.get(i);
            geo_location gl = agent.getLocation();
            int r=8;

            if(gl!=null) {

                geo_location fp = this._w2f.world2frame(gl);
                g.fillOval((int)fp.x()-r, (int)fp.y()-r, 2*r, 2*r);
                g.setColor( new Color(0x10C6B0));
                g.drawString(""+agent.getID(),(int)fp.x()-5, (int)fp.y()+5);
            }
            i++;
        }
    }
    private void drawNode(node_data n, int r, Graphics g) {
        geo_location pos = n.getLocation();
        geo_location fp = this._w2f.world2frame(pos);
        g.fillOval((int)fp.x()-r, (int)fp.y()-r, 2*r, 2*r);
        g.drawString(""+n.getKey(), (int)fp.x(), (int)fp.y()-4*r);
    }

    private void drawEdge(edge_data e, Graphics g) {
        directed_weighted_graph gg = this.ar.getGraph();
        geo_location s = gg.getNode(e.getSrc()).getLocation();
        geo_location d = gg.getNode(e.getDest()).getLocation();
        geo_location s0 = this._w2f.world2frame(s);
        geo_location d0 = this._w2f.world2frame(d);
        g.drawLine((int)s0.x(), (int)s0.y(), (int)d0.x(), (int)d0.y());

       // g.drawString(""+e.getWeight(),);
        //	g.drawString(""+n.getKey(), fp.ix(), fp.iy()-4*r);
    }

    private void clock(Graphics g){

        g.setColor(new Color(65, 173, 69));
        g.drawOval(20,20,50,50);
        g.fillOval(20,20,50,50);
        g.setColor(new Color(3, 3, 3));
        g.setFont(new Font("Wide Latin", Font.BOLD, 20) );
        g.drawString(""+ar.getTime(),35,50);

    }
private void drawTitle(Graphics g){
    g.setColor(new Color(5, 5, 5));
    g.setFont(new Font("Wide Latin", Font.BOLD, 25) );
    g.drawString("-pokemon game-",100,50);
}

}

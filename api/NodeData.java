package api;

import api.geo_location;

public class NodeData implements node_data {
    private int key;
    private geo_location Node_Location;
    private double Node_Weight;
    private String Node_Info;
    private int Node_Tag;
    private static int Key_Counter = 0;

    public NodeData(double x,double y,double z)
    {
        this.key = Key_Counter;
        Key_Counter++;
        this.Node_Location = new Geo_Location(x,y,z);
        this.Node_Weight = 0;
        this.Node_Info = "";
        this.Node_Tag = 0;
    }
    public NodeData(int key,double x,double y,double z)
    {
        this.key = key;
        Key_Counter++;
        this.Node_Location = new Geo_Location(x,y,z);
        this.Node_Weight = 0;
        this.Node_Info = "";
        this.Node_Tag = 0;
    }
    public NodeData(node_data n)
    {this.key = n.getKey();
        this.Node_Location = new Geo_Location(n.getLocation().x(),n.getLocation().y(),n.getLocation().z());
        this.Node_Weight = n.getWeight();
        this.Node_Info = n.getInfo();
        this.Node_Tag = n.getTag();

    }
    /**
     * Returns the key (id) associated with this node.
     * @return
     */
    @Override
    public int getKey() {
        return this.key;
    }
    /** Returns the location of this node, if
     * none return null.
     * @return
     */
    @Override
    public geo_location getLocation() {
        return this.Node_Location;
    }
    /** Allows changing this node's location.
     * @param p - new new location  (position) of this node.
     */
    @Override
    public void setLocation(geo_location p) {
        this.Node_Location=new Geo_Location(p.x(),p.y(),p.z());
    }
    /**
     * Returns the weight associated with this node.
     * @return
     */
    @Override
    public double getWeight() {
        return this.Node_Weight;
    }
    /**
     * Allows changing this node's weight.
     * @param w - the new weight
     */
    @Override
    public void setWeight(double w) {
        this.Node_Weight=w;
    }
    /**
     * Returns the remark (meta data) associated with this node.
     * @return
     */
    @Override
    public String getInfo() {
        return this.Node_Info;
    }
    /**
     * Allows changing the remark (meta data) associated with this node.
     * @param s
     */
    @Override
    public void setInfo(String s) {
        this.Node_Info=s;
    }
    /**
     * Temporal data (aka color: e,g, white, gray, black)
     * which can be used be algorithms
     * @return
     */
    @Override
    public int getTag() {
        return this.Node_Tag;
    }
    /**
     * Allows setting the "tag" value for temporal marking an node - common
     * practice for marking by algorithms.
     * @param t - the new value of the tag
     */
    @Override
    public void setTag(int t) {
        this.Node_Tag=t;
    }

    private class Geo_Location implements geo_location{
        private double x,y,z;

        public Geo_Location(double x, double y, double z)
        {
            this.x = x;
            this.y = y;
            this.z = z;
        }
        @Override
        public double x() {
            return this.x;
        }

        @Override
        public double y() {
            return this.y;
        }

        @Override
        public double z() {
            return this.z;
        }

        @Override
        public double distance(geo_location g) {
            double d= Math.sqrt((Math.pow(x-g.x(),2)+Math.pow(y-g.y(),2)+Math.pow(z-g.z(),2)));
            return d;
        }
    }
}
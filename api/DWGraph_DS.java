package api;


import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;

public class DWGraph_DS implements directed_weighted_graph {

    private HashMap<Integer, node_data> Graph_nodes;// collection of the vertices in the graph
    private HashMap<Integer,HashMap<Integer, edge_data>> Graph_Edges_go_out;// collection of the edges going out from a node in the graph
    private HashMap<Integer,HashMap<Integer, edge_data>> Graph_Edges_go_in;// collection of the edges going in a node in the graph
    private int MC; // holds the number of changes in the graph
    private int Edge_count;// counter for the number of edges

    /**
     * constructor to the graph
     */
    public DWGraph_DS()
    {
        this.Graph_nodes = new HashMap<Integer, node_data>();
        this.Graph_Edges_go_out = new HashMap<Integer,HashMap<Integer, edge_data>>();
        this.Graph_Edges_go_in = new HashMap<Integer,HashMap<Integer, edge_data>>();
        this.Edge_count = 0;
        this.MC = 0;
    }
    /**
     * returns the node_data by the node_id,
     * @param key - the node_id
     * @return the node_data by the node_id, null if none.
     */
    @Override
    public node_data getNode(int key) {
        return Graph_nodes.get(key);
    }
    /**
     * returns the data of the edge (src,dest), null if none.
     * @param src
     * @param dest
     * @return
     */
    @Override
    public edge_data getEdge(int src, int dest) {
        if(Graph_Edges_go_out.get(src)!=null)
        {
            if(Graph_Edges_go_out.get(src).get(dest)!=null)
                return Graph_Edges_go_out.get(src).get(dest);
        }
        return null;
    }
    /**
     * adds a new node to the graph with the given node_data.
     * @param n
     */
    @Override
    public void addNode(node_data n) {
        if(getNode(n.getKey())==null)
        {
            node_data node1 = new NodeData(n);
          Graph_nodes.put(n.getKey(),node1);
          MC++;
        }
    }
    /**
     * Connects an edge with weight w between node src to node dest.
     * @param src - the source of the edge.
     * @param dest - the destination of the edge.
     * @param w - positive weight representing the cost (aka time, price, etc) between src-->dest.
     */
    @Override
    public void connect(int src, int dest, double w) {
        HashMap<Integer, edge_data>graph_out = new HashMap<Integer, edge_data>();
        HashMap<Integer, edge_data> graph_in = new HashMap<Integer, edge_data>();
        boolean mc_check=true,edge_check=true;
        edge_data temp_edge_out, temp_edge_in;
        try {
            if(w<0)
                throw  new ArithmeticException();
        }
        catch (Exception e)
        {
            System.out.println("the size w is a negative size, edge can't have a negative size");
            return;
        }
   if(src==dest || (getNode(src)== null) || (getNode(dest) == null))
       return ;
        temp_edge_out = new EdgeData(src,dest,w);
        if(Graph_Edges_go_out.get(src)!=null)
        { // if src already connect to dest
            if(Graph_Edges_go_out.get(src).get(dest)!=null) {
                // if the size of edge is different from w
                if (Graph_Edges_go_out.get(src).get(dest).getWeight() != w)
                { // copy the info and tag and replace the current edge_data with the updated one
                   temp_edge_out.setInfo(Graph_Edges_go_out.get(src).get(dest).getInfo());
                    temp_edge_out.setTag(Graph_Edges_go_out.get(src).get(dest).getTag());
                    Graph_Edges_go_out.get(src).remove(dest);
                    Graph_Edges_go_out.get(src).put(dest,temp_edge_out);}
                else
                { mc_check=false;}
                edge_check= false;
            }
            else
                //add the edge if not exist
                Graph_Edges_go_out.get(src).put(dest,temp_edge_out);
        }
        else
        {// if there is no previous edges create a collection of edges
            graph_out.put(dest,temp_edge_out);
            Graph_Edges_go_out.put(src,graph_out);
        }

        temp_edge_in = new EdgeData(src,dest,w);
        // if dest got edges connect to it already
        if(Graph_Edges_go_in.get(dest)!=null)
        { // if src already connected to dest
            if(Graph_Edges_go_in.get(dest).get(src)!=null) {
                // if the size of edge is different from w
                if (Graph_Edges_go_in.get(dest).get(src).getWeight() != w)
                { // copy the info and tag and replace the current edge_data with the updated one
                    temp_edge_in.setInfo(Graph_Edges_go_in.get(dest).get(src).getInfo());
                    temp_edge_in.setTag(Graph_Edges_go_in.get(dest).get(src).getTag());
                   Graph_Edges_go_in.get(dest).remove(src);
                   Graph_Edges_go_in.get(dest).put(src,temp_edge_in);}
                else
                    mc_check=false;
               edge_check= false;
            }
            else
                //add the edge if not exist
               Graph_Edges_go_in.get(dest).put(src,temp_edge_in);
        }
        else
        {// if there is no previous edges create a collection of edges
            graph_in.put(src,temp_edge_in);
            Graph_Edges_go_in.put(dest,graph_in);
        }
        if(edge_check)
            Edge_count++;
        if(mc_check)
            MC++;
    }
    /**
     * This method returns a pointer (shallow copy) for the
     * collection representing all the nodes in the graph.
     * @return Collection<node_data>
     */
    @Override
    public Collection<node_data> getV() {
        return Graph_nodes.values();
    }
    /**
     * This method returns a pointer (shallow copy) for the
     * collection representing all the edges getting out of
     * the given node (all the edges starting (source) at the given node).
     * @return Collection<edge_data>
     */
    @Override
    public Collection<edge_data> getE(int node_id) {
        HashSet<edge_data> temp_collection = new HashSet<edge_data>();
        if(Graph_Edges_go_out.get(node_id)!=null)
        {return Graph_Edges_go_out.get(node_id).values();}
        return temp_collection;
    }
    /**
     * Deletes the node (with the given ID) from the graph -
     * and removes all edges which starts or ends at this node.
     * @return the data of the removed node (null if none).
     * @param key
     */
    @Override
    public node_data removeNode(int key) {
        int connector_key,connect_to_key;
        if(getNode(key)!=null) {
            if(this.Graph_Edges_go_in.get(key)!=null) {
                for (edge_data current_connector : Graph_Edges_go_in.get(key).values()) {
                    connector_key = current_connector.getSrc();
                    Graph_Edges_go_out.get(connector_key).remove(key);
                    Edge_count--;
                }
            }
            if(this.getE(key).size()>0) {
                for (edge_data current_connect_to : Graph_Edges_go_out.get(key).values()) {
                    connect_to_key = current_connect_to.getDest();
                    Graph_Edges_go_in.get(connect_to_key).remove(key);
                    Edge_count--;
                }
            }
            Graph_Edges_go_out.remove(key);
            Graph_Edges_go_in.remove(key);
            MC++;
            return Graph_nodes.remove(key);
        }
        return null;
    }
    /**
     * Deletes the edge from the graph,
     * @param src
     * @param dest
     * @return the data of the removed edge (null if none).
     */
    @Override
    public edge_data removeEdge(int src, int dest) {
        if(src==dest || (getNode(src)==null) || (getNode(dest)==null))
            return null;
        if(Graph_Edges_go_out.get(src)!=null)
        {
            if(Graph_Edges_go_out.get(src).get(dest)!=null)
            {Graph_Edges_go_in.get(dest).remove(src);
            Edge_count--;
            this.MC ++;
                return Graph_Edges_go_out.get(src).remove(dest);}
        }
        return null;

    }
    /** Returns the number of vertices (nodes) in the graph.
     * @return
     */
    @Override
    public int nodeSize() {
        return Graph_nodes.size();
    }
    /** Returns the number of edges (assume directional graph).
     * @return
     */
    @Override
    public int edgeSize() {
        return Edge_count;
    }
    /**
     * Returns the Mode Count - for testing changes in the graph.
     * @return
     */
    @Override
    public int getMC() {
        return MC;
    }

    /**
     * return true if the graphs are equal
     * the function compare first the number of vertices and edges in the graphs
     * if one of them is not equal return false.
     * than checks that each vertex contained in the other_g graph,
     * and that each vertex contain the same edges go from it
     * this function used by the assert equals in the junit test
     * and can be used in any equal check between two graphs
     * @param other_g
     * @return - true if the graphs are equal, if not false.
     */
    @Override
    public boolean equals (Object other_g) {
        int current_key, neighbor_key;
        // if other is from a different object
        if (!(other_g instanceof directed_weighted_graph))
            return false;
        directed_weighted_graph graph_other = (directed_weighted_graph) other_g;
        // if one or more of the following is not equal : number of edges,of vertices .
        if (graph_other.nodeSize() != this.nodeSize() || graph_other.edgeSize() != this.edgeSize())
            return false;
        // go over the graph and check if other graph had the same vertices
        for (node_data current_node : this.getV()) {
            current_key = current_node.getKey();
            if (graph_other.getNode(current_key) == null)
                return false;
            if(this.getE(current_key).size()>0) {
                // go over the current vertex edges and checks if it had the same neighbors as the vertex in other graph
                for (edge_data neighbor_edge : this.getE(current_key)) {
                    neighbor_key = neighbor_edge.getDest();
                    if (graph_other.getNode(neighbor_key) == null)
                        return false;
                    if(graph_other.getEdge(current_key,neighbor_key)==null)
                        return false;
                    // if the size of the edge is different
                    if (this.getEdge(current_key, neighbor_key).getWeight() != graph_other.getEdge(current_key, neighbor_key).getWeight())
                        return false;
                }
            }
        }
        return true;
    }
}

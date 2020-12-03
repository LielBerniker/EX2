import java.util.*;

public class DWGraph_Algo implements dw_graph_algorithms {
    private directed_weighted_graph Graph1;

    public DWGraph_Algo()
    {
        Graph1 = new DWGraph_DS();
    }
    /**
     * Init the graph on which this set of algorithms operates on.
     * @param g
     */
    @Override
    public void init(directed_weighted_graph g) {
        Graph1 = g;
    }
    /**
     * Return the underlying graph of which this class works.
     * @return
     */
    @Override
    public directed_weighted_graph getGraph() {
        return Graph1;
    }
    /**
     * Compute a deep copy of this weighted graph.
     * @return
     */
    @Override
    public directed_weighted_graph copy() {
        int node_key,edge_key;
        // crate a new graph
        directed_weighted_graph cop_graph = new DWGraph_DS();
        if (Graph1 == null)
            return cop_graph;
        // go over the graph vertices
        for (node_data current_node : Graph1.getV()) {
            node_key = current_node.getKey();
            // add each vertex to the new graph , if it already in it do nothing
            cop_graph.addNode(current_node);
            // go over each vertex neighbors
            for (edge_data current_edge :Graph1.getE(node_key)) {
                edge_key = current_edge.getDest();
                // add the neighbor to the graph, if already in the graph do nothing
                cop_graph.addNode(Graph1.getNode(edge_key));
                // connect between a vertex and it neighbor int new graph
                cop_graph.connect(node_key,edge_key, current_edge.getWeight());
            }
        }
        return cop_graph;
    }
    /**
     * Returns true if and only if (iff) there is a valid path from each node to each
     * other node. NOTE: assume directional graph (all n*(n-1) ordered pairs).
     * @return
     */
    @Override
    public boolean isConnected() {
        node_data node_temp;
        if (Graph1.nodeSize() == 1 || Graph1.nodeSize() == 0)
            return true;
        Iterator<node_data> first = this.Graph1.getV().iterator();
        node_temp = first.next();
        // activates ann inner function to find the distance from the node_temp
        path_to_all(node_temp.getKey());
        for (node_data current_node : Graph1.getV()) {
            if (current_node.getTag() != 0)
                return false;
        }
        directed_weighted_graph upside_graph = copy_backward();
        path_to_all(node_temp.getKey());
        for (node_data current_node : upside_graph.getV()) {
            if (current_node.getTag() != 0)
                return false;
        }
        return true;
    }
    private void path_to_all(int src) {
        int node_key,edge_key;
        node_data temp_node;
        // operate ass a queue
        LinkedList<node_data> list_of_nodes = new LinkedList<node_data>();
        // go over the graph nodes and set their tag
        for (node_data current_node : Graph1.getV()) {
            current_node.setTag(-1);
        }
        // set first node and add it to queue
        Graph1.getNode(src).setTag(0);
        list_of_nodes.add(Graph1.getNode(src));
        // go over the nodes added to the queue until all been handled
        while (!list_of_nodes.isEmpty()) {
            temp_node = list_of_nodes.getFirst();
            node_key = temp_node.getKey();
            list_of_nodes.remove(list_of_nodes.getFirst());
            if (Graph1.getE(node_key).size() > 0) {
                // if the specific node have neighbors go over them
                for (edge_data current_edge : Graph1.getE(node_key)) {
                    edge_key = current_edge.getDest();
                    // if node tag not been changed set it and add the node to the queue
                    if (Graph1.getNode(edge_key).getTag()==-1) {
                        Graph1.getNode(edge_key).setTag(0);
                        list_of_nodes.add(Graph1.getNode(edge_key));
                    }
                }
            }
        }
    }
    /**
     * Compute a deep copy of this weighted graph but with back backward.
     * @return
     */
    public directed_weighted_graph copy_backward() {
        int node_key,edge_key;
        // crate a new graph
        directed_weighted_graph cop_graph = new DWGraph_DS();
        if (Graph1 == null)
            return cop_graph;
        // go over the graph vertices
        for (node_data current_node : Graph1.getV()) {
            node_key = current_node.getKey();
            // add each vertex to the new graph , if it already in it do nothing
            cop_graph.addNode(current_node);
            // go over each vertex neighbors
            for (edge_data current_edge :Graph1.getE(node_key)) {
                edge_key = current_edge.getDest();
                // add the neighbor to the graph, if already in the graph do nothing
                cop_graph.addNode(Graph1.getNode(edge_key));
                // connect between a vertex and it neighbor int new graph
                cop_graph.connect(edge_key,node_key, current_edge.getWeight());
            }
        }
        return cop_graph;
    }
    /**
     * returns the length of the shortest path between src to dest
     * Note: if no such path --> returns -1
     * @param src - start node
     * @param dest - end (target) node
     * @return
     */
    @Override
    public double shortestPathDist(int src, int dest) {
        HashMap<Integer,node_extend> dis_and_par ;
        if (Graph1 == null)
            return -1;
        if (Graph1.nodeSize() == 0)
            return -1;
        // if one of the vertices not in the graph return -1
        if (Graph1.getNode(src) == null || Graph1.getNode(dest) == null)
            return -1;
        dis_and_par = short_path_by_edge(src, dest);
        // return the dest vertex shortest path from src
        if (dis_and_par.get(dest).getDistance() == Integer.MAX_VALUE)
            return -1;
        else
            return dis_and_par.get(dest).getDistance();
    }
    /**
     * returns the the shortest path between src to dest - as an ordered List of nodes:
     * src--> n1-->n2-->...dest
     * see: https://en.wikipedia.org/wiki/Shortest_path_problem
     * Note if no such path --> returns null;
     * @param src - start node
     * @param dest - end (target) node
     * @return
     */
    @Override
    public List<node_data> shortestPath(int src, int dest) {
        LinkedList<node_data> path_list1 = new LinkedList<node_data>();
        LinkedList<node_data> path_list2 = new LinkedList<node_data>();
        HashMap<Integer,node_extend> full_path ;
        String prev;
        if (Graph1 == null)
            return path_list1;
        if (Graph1.getNode(src) == null || Graph1.getNode(dest) == null)
            return path_list1;
        full_path = short_path_by_edge(src, dest);
        if (full_path.get(dest).getDistance() == Integer.MAX_VALUE)
            return path_list1;
        prev = full_path.get(dest).getParent();
        path_list1.add(Graph1.getNode(dest));
        while (!prev.equals("")) {
            path_list1.add(Graph1.getNode(Integer.parseInt(prev)));
            prev = full_path.get(Integer.parseInt(prev)).getParent();
        }
        while (!path_list1.isEmpty()) {
            path_list2.add(path_list1.getLast());
            path_list1.remove(path_list1.getLast());
        }
        return path_list2;

    }
    /**
     * returns an hashmap collection that hold the information about the vertices in the
     * shortest path from src to dest
     * the function reset all the tag vertices in the graph to Integer MAX VALUE and insert them to a Priority Queue
     * than set the src tag to 0 , and go over his neighbors and set their tag to the weight of the edge between the vertices
     * than pull the next vertex with the smallest tag, and go over his neighbors
     * checks if their tag is bigger than his tag + the edge weight between them.
     * if true then update the neighbor tag and add his information (key and prev) to the hashmap
     * do the same until the queue is empty
     * then return the hashmap collection that holds the information of the vertices
     * @param src - start node
     * @param dest - end (target) node
     * @return
     */
    private HashMap<Integer,node_extend> short_path_by_edge(int src, int dest) {
        node_extend extend_temp1,extend_temp3;
        int node_key, neighbor_key;
        HashMap<Integer,node_extend> prev_contain = new HashMap<Integer,node_extend>();
        // a priority queue that holds the graph vertices
        PriorityQueue<node_extend> node_prior = new PriorityQueue<node_extend>();
        // sets the src node and insert it to the queue and the collection
        extend_temp1 = new node_extend(Graph1.getNode(src));
        extend_temp1.setDistance(0);
        prev_contain.put(src,extend_temp1);
        node_prior.add(extend_temp1);
        // if the dest and src are equal return just one node in the collection
        if (src == dest) {
            return prev_contain;
        }
        for (node_data current_node : Graph1.getV()) {
            if(current_node.getKey()!=src)
            { node_extend extend_temp2 = new node_extend(current_node);
            prev_contain.put(extend_temp2.getKey(),extend_temp2);}
        }
        // run until sets every node in the path
        while (!node_prior.isEmpty()) {
            // get closest node in queue to src node
            extend_temp3 = node_prior.poll();
            node_key = extend_temp3.getKey();
            // check if the node have neighbors if it had go over them and sets them
            if (Graph1.getE(node_key).size() > 0) {
                // go over the node neighbors
                for (edge_data neighbor_node : Graph1.getE(node_key)) {
                    neighbor_key = neighbor_node.getDest();
                    // if the neighbor vertex current distance is higher than the current node + edge , replace it
                    if (prev_contain.get(neighbor_key).getDistance() > prev_contain.get(node_key).getDistance() + neighbor_node.getWeight())
                    {// change the node distance from src and update it at the collection
                        node_prior.remove(prev_contain.get(neighbor_key));
                        prev_contain.get(neighbor_key).setDistance(prev_contain.get(node_key).getDistance() + neighbor_node.getWeight());
                        prev_contain.get(neighbor_key).setParent("" + node_key);
                        node_prior.add(prev_contain.get(neighbor_key));
                    }
                }
            }
        }
        return  prev_contain;
    }
    @Override
    public boolean save(String file) {
        return false;
    }

    @Override
    public boolean load(String file) {
        return false;
    }
    private class node_extend implements Comparable
    {
        int key;
        String parent;
        double distance;
        public node_extend(node_data node1)
        {
            this.key = node1.getKey();
            this.parent = "";
            this.distance = Integer.MAX_VALUE;
        }
        public int getKey()
        {
           return this.key;
        }
        public String getParent()
        {
            return  this.parent;
        }
        public void setParent(String parent) {
            this.parent = parent;
        }
        public double getDistance()
        {
            return this.distance;
        }
        public void setDistance(double distance) {
            this.distance = distance;
        }

        @Override
        public int compareTo(Object other_n) {
            node_extend node_other = (node_extend) other_n;
            if(this.getDistance()<node_other.getDistance())
                return -1;
            else if(this.getDistance()>node_other.getDistance())
                return 1;
            else
                return 0;
        }
    }
}

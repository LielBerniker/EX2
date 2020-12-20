package api;

import api.directed_weighted_graph;
import api.dw_graph_algorithms;
import com.google.gson.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.util.*;

public class DWGraph_Algo implements dw_graph_algorithms {
    private directed_weighted_graph Graph1;// the graph that the algorithms can be used on

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
     * doing so by copying the vertices of the graph and each edges that go out from
     * each vertex
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
     * this function checks if a specific vertex can rech to all the vertex
     * in the graph , doing so with an inner function.
     * if each vertex in the graph can be reached from the specific vertex.
     * create a copy of the graph but with all of the edges with the opposite direction,
     * doing so with an inner function, is still reach all the vertices in the graph
     * so the graph is connected and the function return true.
     * @return
     */
    @Override
    public boolean isConnected() {
        node_data node_temp;
        directed_weighted_graph temp_graph= this.copy();
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
        this.init(upside_graph);
        path_to_all(node_temp.getKey());
        for (node_data current_node : upside_graph.getV()) {
            if (current_node.getTag() != 0)
            {this.init(temp_graph);
                return false;}
        }
        this.init(temp_graph);
        return true;
    }

    /**
     * an inner function that go over the graph nodes from a certain node,
     * and check if there is a path from this node to all the nodes from the graph
     * the function sets every node tag that can be reced to 0
     * the function start from a certain node than go over its neighbors and repeat
     * @param src
     */
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
     * Compute a deep copy of this weighted graph but with every edges sets to his backward.
     * by going over the current graph and copy all of the graph nodes and edges that go out from each node
     * @return
     */
    private directed_weighted_graph copy_backward() {
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
     * soing so by using an information in the collection that an inner function return to the function ,
     * this collection contain all the nodes in the path and their distance from the source node
     * Note: if no such path --> returns -1
     * @param src - start node
     * @param dest - end (target) node
     * @return
     */
    @Override
    public double shortestPathDist(int src, int dest) {
        HashMap<Integer,node_extend> dis_and_par ;
        if (Graph1 == null || Graph1.nodeSize() == 0 ||Graph1.getNode(src) == null || Graph1.getNode(dest) == null)
            return -1;
        dis_and_par = short_path_by_edge(src, dest);
        // return the dest vertex shortest path from src
        if (dis_and_par.get(dest)==null)
            return -1;
        else
            return dis_and_par.get(dest).getDistance();
    }
    /**
     * returns the the shortest path between src to dest - as an ordered List of nodes:
     * src--> n1-->n2-->...dest
     * doing so by using an inner function that return a collection contains all the nodes in the path With their previous
     * that the function crete a list by the collection it gets from the inner function
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
        if (Graph1 == null || Graph1.getNode(src) == null || Graph1.getNode(dest) == null)
            return null;
        full_path = short_path_by_edge(src, dest);
        if (full_path.get(dest) == null)
            return null;
        prev = full_path.get(dest).getParent();
        path_list1.add(Graph1.getNode(dest));
        while (!prev.equals("")) {
            path_list1.add(Graph1.getNode(Integer.parseInt(prev)));
            prev = full_path.get(Integer.parseInt(prev)).getParent();
        }
        Collections.reverse(path_list1);
        return path_list1;

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
                    if(prev_contain.get(neighbor_key)==null)
                    {extend_temp1 = new node_extend(getGraph().getNode(neighbor_key));
                    prev_contain.put(neighbor_key,extend_temp1);}
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

    /**
     * return true if the function succeed of saving the graph the algorithm initiate to a file
     * if not succeed return false
     * @param file - the file name (may include a relative path).
     * @return
     */
    @Override
    public boolean save(String file) {

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson(this.Graph1);
        try {
            // write the graph to a file
            PrintWriter save_to = new PrintWriter(new File(file));
            save_to.write(json);
            save_to.close();
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * the function return true if it succeed to load a graph and initiate it from a file
     * if not succeed return false
     * @param file - file name of JSON file
     * @return
     */
    @Override
    public boolean load(String file) {
        try {
            GsonBuilder builder = new GsonBuilder();
            // use a class to build the graph from the json
            builder.registerTypeAdapter(directed_weighted_graph.class,new directed_weighted_graph_json_deserializer());
            Gson gson = builder.create();
            FileReader reader = new FileReader(file);
            // initiate the graph
            this.init(gson.fromJson(reader,directed_weighted_graph.class));
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        }
       return true;
    }

    /**
     * an inner class that holds a extended information about a node from the grpah
     * this class used by the shortest path function in the algorithms
     */
    private class node_extend implements Comparable
    {
        int key;
        String parent;
        double distance;

        /**
         * constructor to the node extend by a node
         * @param node1
         */
        public node_extend(node_data node1)
        {
            this.key = node1.getKey();
            this.parent = "";
            this.distance = Integer.MAX_VALUE;
        }

        /**
         * return the node extend key
         * @return
         */
        public int getKey()
        {
           return this.key;
        }

        /**
         * return the parent id
         * @return
         */
        public String getParent()
        {
            return  this.parent;
        }

        /**
         * set the parent id
         * @param parent
         */
        public void setParent(String parent) {
            this.parent = parent;
        }

        /**
         * det the distance from a certain node
         * @return
         */
        public double getDistance()
        {
            return this.distance;
        }

        /**
         * set the distance from a certain node
         * @param distance
         */
        public void setDistance(double distance) {
            this.distance = distance;
        }

        /**
         * a compare to function to the extend node
         * return -1 if the current node got a smaller distance
         * return 1 if the current node got a bigger distance
         * return 0 if the nodes distance are equal
         * @param other_n
         * @return
         */
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

    /**
     * a class that create a graph from a json by two types of json
     */
    private class directed_weighted_graph_json_deserializer implements JsonDeserializer<directed_weighted_graph>
    {
        /**
         * the function  cretae a graph from a json
         * it can create a graph from to types of json graphs
         * @param json
         * @param type
         * @param jsonDeserializationContext
         * @return
         * @throws JsonParseException
         */
        @Override
        public directed_weighted_graph deserialize(JsonElement json, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            JsonObject jsonObject = json.getAsJsonObject();
            directed_weighted_graph graph_create = new DWGraph_DS();
            int nk_temp,nt_temp,src_temp,dest_temp,et_temp;
            double nl_x , nl_y , nl_z, nw_temp,ew_temp;
            String ni_temp,ei_temp;
            JsonElement Graph_nodes_check = jsonObject.get("Graph_nodes");
            // if the json contain Graph_nodes create a graph by the first type json graph builder
            if(Graph_nodes_check!=null){
            JsonObject Graph_nodes_json_obj1 =Graph_nodes_check.getAsJsonObject();
            for (Map.Entry<String, JsonElement> set: Graph_nodes_json_obj1.entrySet()) {
                JsonElement jsonValueNode = set.getValue();
                JsonElement jsonValueLocation = jsonValueNode.getAsJsonObject().get("Node_Location");
                 nk_temp = jsonValueNode.getAsJsonObject().get("key").getAsInt();
                 nt_temp = jsonValueNode.getAsJsonObject().get("Node_Tag").getAsInt();
                 nw_temp = jsonValueNode.getAsJsonObject().get("Node_Weight").getAsDouble();
                 ni_temp = jsonValueNode.getAsJsonObject().get("Node_Info").getAsString();
                 nl_x = jsonValueLocation.getAsJsonObject().get("x").getAsDouble();
                 nl_y = jsonValueLocation.getAsJsonObject().get("y").getAsDouble();
                 nl_z = jsonValueLocation.getAsJsonObject().get("z").getAsDouble();
                 node_data node_temp = new NodeData(nk_temp,nl_x,nl_y,nl_z);
                 node_temp.setTag(nt_temp);
                 node_temp.setInfo(ni_temp);
                 node_temp.setWeight(nw_temp);
                 graph_create.addNode(node_temp);
            }
            JsonObject Graph_edges_json_obj2 = jsonObject.get("Graph_Edges_go_out").getAsJsonObject();
            for (Map.Entry<String, JsonElement> set1: Graph_edges_json_obj2.entrySet()) {
                JsonElement jsonValueEdge = set1.getValue();
                for (Map.Entry<String, JsonElement> set2 : jsonValueEdge.getAsJsonObject().entrySet()) {
                    JsonElement jsonValueSpecificEdge = set2.getValue();
                    src_temp = jsonValueSpecificEdge.getAsJsonObject().get("Src").getAsInt();
                    dest_temp = jsonValueSpecificEdge.getAsJsonObject().get("Dest").getAsInt();
                    ew_temp = jsonValueSpecificEdge.getAsJsonObject().get("Edge_Weight").getAsDouble();
                    ei_temp = jsonValueSpecificEdge.getAsJsonObject().get("Edge_Info").getAsString();
                    et_temp = jsonValueSpecificEdge.getAsJsonObject().get("Edge_Tag").getAsInt();
                    graph_create.connect(src_temp, dest_temp, ew_temp);
                    graph_create.getEdge(src_temp,dest_temp).setTag(et_temp);
                    graph_create.getEdge(src_temp,dest_temp).setInfo(ei_temp);
                }
            }
            }
            // if the json contain do not Graph_nodes create a graph by the seconed type json graph builder
            else
            {
                Graph_nodes_check = jsonObject.get("Nodes");
                JsonArray Graph_nodes_json_obj_new =Graph_nodes_check.getAsJsonArray();
                for (JsonElement set: Graph_nodes_json_obj_new) {
                    JsonElement jsonValueNode = set.getAsJsonObject();
                    JsonElement jsonValueLocation = jsonValueNode.getAsJsonObject().get("pos");
                    String [] pos_contain = jsonValueLocation.getAsString().split(",");
                    nk_temp = jsonValueNode.getAsJsonObject().get("id").getAsInt();
                    nl_x = Double.parseDouble(pos_contain[0]);
                    nl_y = Double.parseDouble(pos_contain[1]);
                    nl_z = Double.parseDouble(pos_contain[2]);
                    node_data node_temp = new NodeData(nk_temp,nl_x,nl_y,nl_z);
                    graph_create.addNode(node_temp);
                }
                Graph_nodes_check = jsonObject.get("Edges");
                JsonArray Graph_edges_json_obj_new = Graph_nodes_check.getAsJsonArray();
                for ( JsonElement set1: Graph_edges_json_obj_new) {
                    JsonElement jsonValueEdge = set1.getAsJsonObject();
                    ew_temp = jsonValueEdge.getAsJsonObject().get("w").getAsDouble();
                        src_temp = jsonValueEdge.getAsJsonObject().get("src").getAsInt();
                        dest_temp = jsonValueEdge.getAsJsonObject().get("dest").getAsInt();
                        graph_create.connect(src_temp, dest_temp, ew_temp);
                }
            }
           return graph_create;
        }
    }
}

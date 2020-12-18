package api;

import api.DWGraph_DS;
import api.directed_weighted_graph;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DWGraph_DSTest {

    /**
     * create a graph with 8 nodes and no edges
     * and return it
     * @return
     */
    private directed_weighted_graph create_graph1()
    {
        directed_weighted_graph graph_temp = new DWGraph_DS();
        for (int i = 0;i<8;i++)
        {
            node_data node1 = new NodeData(i,i,i+1,i+2);
            graph_temp.addNode(node1);
        }

        return graph_temp;
    }

    /**
     * create a graph with 8 nodes and 10 edges
     * @return
     */
    private directed_weighted_graph create_graph2()
    {
        directed_weighted_graph graph_temp = new DWGraph_DS();
        for (int i = 0;i<8;i++)
        {
            node_data node1 = new NodeData(i,i,i+1,i+2);
            graph_temp.addNode(node1);
        }
        graph_temp.connect(1,2,2);
        graph_temp.connect(1,3,0);
        graph_temp.connect(3,4,5);
        graph_temp.connect(1,5,4);
        graph_temp.connect(4,6,3);
        graph_temp.connect(2,7,1);
        graph_temp.connect(6,7,38);
        graph_temp.connect(3,1,1);
        graph_temp.connect(7,2,8);
        graph_temp.connect(7,6,10);
        return graph_temp;
    }
    /**
     * check node size when insert nodes with same key
     * and multiply nodes
     */
    @Test
    void valid_node_add_test() {
        directed_weighted_graph graph_test = new DWGraph_DS();
        node_data node_0 = new NodeData(1,2,5);
        node_data node_1 = new NodeData(7,3,8);
        node_data node_2 = new NodeData(8,3,4);
        node_data node_3 = new NodeData(24,66,67);
        graph_test.addNode(node_0);
        graph_test.addNode(node_0);
        assertEquals(1,graph_test.nodeSize());
        graph_test.addNode(node_1);
        graph_test.addNode(node_2);
        graph_test.addNode(node_3);
        assertEquals(4,graph_test.nodeSize());
        graph_test.addNode(node_3);
        assertEquals(4,graph_test.nodeSize());
        assertNotEquals(5,graph_test.nodeSize());
    }
    /**
     * check if edge exist in the graph
     * when insert the same edge
     * and an edge from node to his self
     */
    @Test
    void get_Edge_test() {

        directed_weighted_graph g=create_graph1();
        g.connect(1,2,2);
        g.connect(1,3,0);
        g.connect(3,4,5);
        g.connect(1,5,4);
        g.connect(4,6,3);
        g.connect(2,7,1);
        g.connect(6,7,38);
        assertEquals(0,g.getEdge(1,3).getWeight());
        assertEquals(null,g.getEdge(3,7));
        assertNotEquals(30,g.getEdge(2,7).getWeight());
        assertEquals(6,g.getEdge(4,6).getDest());
        assertEquals(3,g.getEdge(3,4).getSrc());
        assertNotNull(g.getEdge(3,4));
        g.connect(4,4,7);
        assertEquals(null,g.getEdge(4,4));
    }

    /** checks a proper edge connect and improper edge connect */
    @Test
    void connect_nodes_test() {
        directed_weighted_graph g=create_graph1();
        g.connect(1,2,2);
        g.connect(1,3,0);
        g.connect(3,4,5);
        g.connect(2,1,4);
        g.connect(4,3,5);

        assertEquals(2,g.getEdge(1,2).getWeight());
        assertEquals(5,g.edgeSize());

        g.removeEdge(1,2);
        assertEquals(4,g.edgeSize());

        assertNull(g.getEdge(1,2));
        assertNull(g.getEdge(1,7));

        g.connect(1,1,3);
        assertNull(g.getEdge(1,1));
        assertEquals(4,g.edgeSize());

        g.connect(1,2,2);
        g.removeNode(1);
        assertEquals(2,g.edgeSize());
        g.connect(4,3,5);
        assertEquals(2,g.edgeSize());

    }
    /**
     * check that the node size is correct
     * and if getv return all of the vertices in the graph
     */
    @Test
    void get_Vertices_test() {
        directed_weighted_graph g=create_graph1();
        boolean check_v=true;
        assertEquals(8,g.getV().size());
        assertNotEquals(30,g.getV().size());
        g.removeNode(5);
        assertEquals(7,g.getV().size());
        node_data node1 = new NodeData(0,6,7,5);
        g.addNode(node1);
        assertEquals(7,g.getV().size());
        for (node_data current_node: g.getV()) {
            if(g.getNode(current_node.getKey())==null){
                check_v = false;
                break;
            }
        }
        assertTrue(check_v);

    }
    /**
     * check that the number of edges that go from a vertex is correct
     */
    @Test
    void get_Edges_test() {
        directed_weighted_graph g=create_graph2();

        assertEquals(3,g.getE(1).size());
        assertEquals(1,g.getE(4).size());
        assertEquals(0,g.getE(20).size());
        assertEquals(2,g.getE(7).size());
        assertEquals(g.getE(3).size(),g.getE(7).size());
        g.removeEdge(7,2);
        g.removeEdge(7,6);
        assertEquals(0,g.getE(7).size());
    }
    /**
     * checks if remove keep the node size correct
     * and the edge size correct
     */
    @Test
    void remove_Node_test() {

        directed_weighted_graph g=create_graph2();

        g.removeNode(1);
        assertEquals(7,g.nodeSize());
        assertNull(g.removeNode(90));
        assertNull(g.getNode(1));
        assertEquals(6,g.edgeSize());
        g.removeNode(1);
        assertEquals(7,g.nodeSize());
        assertNotEquals(10,g.edgeSize());

    }
    /**
     * checks that edge remove effect on the edge size */
    @Test
    void removeEdge() {
        directed_weighted_graph g=create_graph2();

        assertEquals(38,g.removeEdge(6,7).getWeight());
        assertEquals(9,g.edgeSize());
        assertNull(g.removeEdge(6,7));
        assertEquals(10,g.getEdge(7,6).getWeight());
        assertEquals(9,g.edgeSize());
        assertNull(g.removeEdge(1,1));
        assertNull(g.removeEdge(2,3));
        assertNull(g.removeEdge(9,9));
        assertEquals(9,g.edgeSize());
        assertNotNull(g.removeEdge(4,6));
        assertEquals(8,g.edgeSize());

    }
    /**
     * checks node size when adding and removing vertices */
    @Test
    void nodeSize() {
        directed_weighted_graph g=create_graph1();

        assertEquals(8,g.getV().size());
        g.removeNode(5);
        assertEquals(7,g.getV().size());
        node_data node1 = new NodeData(5,6,7,8);
        g.addNode(node1);
        assertEquals(8,g.getV().size());
        node_data node2 = new NodeData(5,7,7,455);
        g.addNode(node2);
        assertEquals(8,g.getV().size());
    }
    /**
     * checks edge size when adding, connecting and removing vertices */
    @Test
    void edgeSize() {
        directed_weighted_graph g=create_graph2();

        g.removeEdge(7,2);
        assertEquals(9,g.edgeSize());
        g.removeEdge(7,2);
        assertEquals(9,g.edgeSize());
        g.removeEdge(6,8);
        assertEquals(9,g.edgeSize());
        g.removeEdge(7,7);
        assertEquals(9,g.edgeSize());
        g.removeNode(1);
        assertEquals(5,g.edgeSize());
        node_data node1 = new NodeData(1,6,7,8);
        g.addNode(node1);
        assertEquals(5,g.edgeSize());
        g.connect(0,7,97593593);
        assertEquals(6,g.edgeSize());
    }
    /**
     * checks the changes counter in a variety of function(add,remove,connect..)
     */
    @Test
    void getMC() {
        directed_weighted_graph g=create_graph2();
        assertEquals(18,g.getMC());

        g.removeEdge(7,2);
        g.removeEdge(7,2);
        g.removeEdge(6,8);
        g.removeEdge(7,7);
        assertEquals(19,g.getMC());

        g.removeNode(1);
        assertEquals(20,g.getMC());
        node_data node1 = new NodeData(1,6,7,8);
        g.addNode(node1);
        assertEquals(21,g.getMC());
        g.addNode(node1);
        assertEquals(21,g.getMC());
        g.connect(6,7,38);
        assertEquals(21,g.getMC());
        g.connect(6,7,56);
        assertEquals(22,g.getMC());
    }
}
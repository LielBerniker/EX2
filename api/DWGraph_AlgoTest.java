package api;

import api.DWGraph_Algo;
import api.directed_weighted_graph;
import api.dw_graph_algorithms;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DWGraph_AlgoTest {

    /** create a graph with 8 vertices and no edges **/
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
    /** create a graph with 8 vertices and 14 edges **/
    private directed_weighted_graph create_graph2()
    {
       directed_weighted_graph graph_create = new DWGraph_DS();
        for (int i = 0;i<8;i++)
        {
            node_data node1 = new NodeData(i,i,i+1,i+2);
            graph_create.addNode(node1);
        }
        graph_create.connect(0,1,1);
        graph_create.connect(1,6,1);
        graph_create.connect(1,5,4);
        graph_create.connect(6,7,3);
        graph_create.connect(6,1,5);
        graph_create.connect(6,5,1);
        graph_create.connect(5,6,1);
        graph_create.connect(5,4,2);
        graph_create.connect(4,2,2);
        graph_create.connect(2,0,2);
        graph_create.connect(2,3,6);
        graph_create.connect(2,4,9);
        graph_create.connect(3,0,5);
        graph_create.connect(3,4,3);

        return graph_create;
    }
    /** create a connected graph with 10000 vertices and 10000 edges **/
    private directed_weighted_graph create_graph3()
    {
        directed_weighted_graph graph_create = new DWGraph_DS();
        int num = 10000;
        for (int i = 0;i<num;i++)
        {
            node_data node1 = new NodeData(i,i,i+1,i+2);
            graph_create.addNode(node1);
            if(i<4500)
                graph_create.connect(i, (int)i*2, 2);
            else
                graph_create.connect(i, (int)i/3, 2);
        }

        return graph_create;
    }
    /** test for the init function **/
    @Test
    void init_test() {
        dw_graph_algorithms algo_test = new DWGraph_Algo();
        directed_weighted_graph graph_test1 = create_graph1();
        directed_weighted_graph graph_test2 = create_graph2();
        algo_test.init(graph_test1);
        assertEquals(graph_test1,algo_test.getGraph());
        graph_test1.removeNode(0);
        assertEquals(graph_test1,algo_test.getGraph());
        assertNotEquals(graph_test2,algo_test.getGraph());
        algo_test.init(graph_test2);
        assertEquals(graph_test2,algo_test.getGraph());
        assertNotEquals(graph_test1,algo_test.getGraph());
        node_data node_temp = new NodeData(90,5,6,7);
        graph_test2.addNode(node_temp);
        assertEquals(graph_test2,algo_test.getGraph());

    }
    /** test for copy function, bt compare equal graph and not equal graph  **/
    @Test
    void copy_test() {
        dw_graph_algorithms algo_test = new DWGraph_Algo();
        algo_test.init( create_graph1());
       directed_weighted_graph graph_test2,graph_test1 = algo_test.copy();
        assertEquals(algo_test.getGraph(),graph_test1);
        graph_test1.removeNode(0);
        assertNotEquals(graph_test1,algo_test.getGraph());
        algo_test.init( create_graph2());
        graph_test2 = algo_test.copy();
        assertEquals(algo_test.getGraph(),graph_test2);
        graph_test2.connect(1,7,2);
        assertNotEquals(graph_test2,algo_test.getGraph());

    }
    /** test for copy function, by compare equal graph and not equal graph  **/
    @Test
    void isConnected_test() {
        dw_graph_algorithms algo_test = new DWGraph_Algo();
        algo_test.init( create_graph1());
        assertFalse(algo_test.isConnected());
        algo_test.getGraph().connect(0,1,3);
        assertFalse(algo_test.isConnected());
        for (int i=1 ; i<6;i++)
        {
            algo_test.getGraph().connect(i,i+1,3);
            assertFalse(algo_test.isConnected());
        }
        algo_test.getGraph().connect(6,7,3);
        algo_test.getGraph().connect(7,0,7);
        assertTrue(algo_test.isConnected());
        algo_test.init( create_graph2());
        assertFalse(algo_test.isConnected());

    }
    /** test for shortest path function , checks the function by create a graph ,
     * and check the shortest path from different vertices in the graph
     * **/
    @Test
    void shortestPathDist_test() {
        dw_graph_algorithms algo_test = new DWGraph_Algo();
        assertEquals(-1,algo_test.shortestPathDist(5,7));
        algo_test.init(create_graph1());
        assertEquals(-1,algo_test.shortestPathDist(0,1));
        algo_test.getGraph().connect(0,1,3.4);
        assertEquals(3.4,algo_test.shortestPathDist(0,1));
        algo_test.init( create_graph2());
        assertEquals(0,algo_test.shortestPathDist(0,0));
        assertEquals(1,algo_test.shortestPathDist(0,1));
        assertEquals(7,algo_test.shortestPathDist(0,2));
        assertEquals(13,algo_test.shortestPathDist(0,3));
        assertEquals(5,algo_test.shortestPathDist(0,4));
        assertEquals(3,algo_test.shortestPathDist(0,5));
        assertEquals(2,algo_test.shortestPathDist(0,6));
        assertEquals(5,algo_test.shortestPathDist(0,7));

        assertEquals(-1,algo_test.shortestPathDist(7,0));
        assertEquals(-1,algo_test.shortestPathDist(7,1));
        assertEquals(-1,algo_test.shortestPathDist(7,2));
        assertEquals(-1,algo_test.shortestPathDist(7,3));
        assertEquals(-1,algo_test.shortestPathDist(7,4));
        assertEquals(-1,algo_test.shortestPathDist(7,5));
        assertEquals(-1,algo_test.shortestPathDist(7,6));
        assertEquals(0,algo_test.shortestPathDist(7,7));

        assertEquals(6,algo_test.shortestPathDist(5,0));
        assertEquals(6,algo_test.shortestPathDist(5,1));
        assertEquals(4,algo_test.shortestPathDist(5,2));
        assertEquals(10,algo_test.shortestPathDist(5,3));
        assertEquals(2,algo_test.shortestPathDist(5,4));
        assertEquals(0,algo_test.shortestPathDist(5,5));
        assertEquals(1,algo_test.shortestPathDist(5,6));
        assertEquals(4,algo_test.shortestPathDist(5,7));
        assertEquals(-1,algo_test.shortestPathDist(5,10));
    }

    /** test for shortest path function that return a list of the vertices in the pah.
     * checks the function by create a graph ,and check if the list contain the vertices in the short path
     * by the wright order
     * */
    @Test
    void shortestPath_test() {
        int[] vertices_path_0_7 = {0,1,6,7};
        int[] vertices_path_7_0 = {};
        int[] vertices_path_5_3 = {5,4,2,3};
        int[] vertices_path_0_3 = {0,1,6,5,4,2,3};
        int count = 0;
        dw_graph_algorithms algo_test = new DWGraph_Algo();
        assertNull(algo_test.shortestPath(19,3));
        algo_test.init( create_graph1());
        List<node_data> list_temp = new LinkedList<node_data>();
        assertEquals(null,algo_test.shortestPath(0,1));
        algo_test.init( create_graph2());
        List<node_data> vertices_list1= algo_test.shortestPath(0,7);
        for (node_data current_node: vertices_list1){
            Assertions.assertEquals(vertices_path_0_7[count],current_node.getKey());
            count++;
        }

        List<node_data> vertices_list2= algo_test.shortestPath(7,0);
        assertEquals(null,vertices_list2);

        List<node_data> vertices_list3= algo_test.shortestPath(5,3);
        count =0;
        for (node_data current_node: vertices_list3){
            Assertions.assertEquals(vertices_path_5_3[count],current_node.getKey());
            count++;
        }

        List<node_data> vertices_list4= algo_test.shortestPath(0,3);
        count =0;
        for (node_data current_node: vertices_list4){
            Assertions.assertEquals(vertices_path_0_3[count],current_node.getKey());
            count++;
        }
      assertNull(algo_test.shortestPath(19,3));
    }


    /** test for save and load function, the test create a graph add it to the graph algo and save it to a file
     * than load the graph and check if it equal to the original graph **/
    @Test
    void save_and_load_graph_test() {
        String file1="graph_file_0",file2="graph_file_1";
        directed_weighted_graph graph_temp1 = create_graph2();
        dw_graph_algorithms algo_test = new DWGraph_Algo();
        algo_test.init(graph_temp1);
        algo_test.save(file1);
        algo_test.load(file1);
        assertEquals(graph_temp1,algo_test.getGraph());
        directed_weighted_graph graph_temp2 = create_graph3();
        algo_test.init(graph_temp2);
        algo_test.save(file2);
        algo_test.load(file2);
        assertEquals(graph_temp2,algo_test.getGraph());
        assertNotEquals(graph_temp1,algo_test.getGraph());
        algo_test.load(file1);
        assertEquals(8,algo_test.getGraph().nodeSize());
        assertEquals(graph_temp1,algo_test.getGraph());
        assertNotEquals(graph_temp2,algo_test.getGraph());




    }
}
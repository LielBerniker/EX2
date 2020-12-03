import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DWGraph_DSTest {

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
    }

    @Test
    void getEdge() {
    }

    @Test
    void addNode() {
    }

    @Test
    void connect() {
    }

    @Test
    void getV() {
    }

    @Test
    void getE() {
    }

    @Test
    void removeNode() {
    }

    @Test
    void removeEdge() {
    }

    @Test
    void nodeSize() {
    }

    @Test
    void edgeSize() {
    }

    @Test
    void getMC() {
    }
}
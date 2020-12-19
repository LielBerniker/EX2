package gameClient;

import api.*;
import gameClient.util.Point3D;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CL_AgentTest {
    /**
     * crate an agent
     * @return
     */
 public CL_Agent create_agent1()
 {
     directed_weighted_graph graph1 = graph_creator();
     CL_Agent agent_temp = new CL_Agent(graph1,0);
return agent_temp;
 }

    /**
     * create an agent
     * @return
     */
    public CL_Agent create_agent2()
    {
        directed_weighted_graph graph1 = graph_creator();
        CL_Agent agent_temp = new CL_Agent(graph1,7);
        return agent_temp;
    }

    /**
     * crate a graph with 8 nodes and 7 edges
     * @return
     */
    public directed_weighted_graph graph_creator()
{
    directed_weighted_graph graph1 = new DWGraph_DS();
    for (int i = 0; i < 8; i++) {
        node_data temp_node = new NodeData(i,i+1,i+2,i+3);
       graph1.addNode(temp_node);
    }
    for (int i = 0; i < 7; i++) {
        graph1.connect(i,i+1,i);
    }
    return graph1;
}

    /**
     * test for tor source node functions
     */
    @Test
    void set_and_get_Src_Node_test() {
     CL_Agent agent_2 ,agent_1 = create_agent1();
     assertEquals(0,agent_1.getSrcNode());
     assertNotEquals(9,agent_1.getSrcNode());

     agent_2 = create_agent2();
     assertEquals(7,agent_2.getSrcNode());
     assertNotEquals(0,agent_2.getSrcNode());
     assertNotEquals(agent_2.getSrcNode(),agent_1.getSrcNode());

     agent_1.setCurrNode(7);
     assertEquals(7,agent_1.getSrcNode());
     assertEquals(agent_2.getSrcNode(),agent_1.getSrcNode());
     agent_2.setCurrNode(6);
     assertNotEquals(agent_2.getSrcNode(),agent_1.getSrcNode());
    }

    /**
     * test for tor value of node functions
     */
    @Test
    void set_and_get_value_test() {
        CL_Agent agent_2 ,agent_1 = create_agent1();
        assertEquals(0,agent_1.getValue());
        assertNotEquals(88,agent_1.getValue());
        agent_2 = create_agent2();
        assertEquals(0,agent_2.getValue());
        assertNotEquals(80,agent_2.getValue());
        assertEquals(agent_2.getValue(),agent_1.getValue());

        agent_1.setMoney(34.55);
        assertEquals(34.55,agent_1.getValue());
        agent_2.setMoney(67.99);
        assertNotEquals(agent_2.getValue(),agent_1.getValue());
        agent_2.setMoney(34.55);
        assertEquals(agent_2.getValue(),agent_1.getValue());
    }

    /**
     * test for tor destination node functions
     */
    @Test
    void set_and_get_NextNode_test() {
        CL_Agent agent_2 ,agent_1 = create_agent1();
        assertTrue(agent_1.setNextNode(1));
        assertEquals(1,agent_1.getNextNode());
        assertFalse(agent_1.setNextNode(5));
        assertEquals(-1,agent_1.getNextNode());

        agent_2= create_agent2();
        agent_2.setCurrNode(6);
        assertTrue(agent_2.setNextNode(7));
        assertEquals(7,agent_2.getNextNode());
        assertFalse(agent_2.setNextNode(3));
        assertEquals(-1,agent_2.getNextNode());
    }

    /**
     * test for check if the agents is moving
     */
    @Test
    void is_Moving_test() {
        CL_Agent agent_2 ,agent_1 = create_agent1();
        assertFalse(agent_1.isMoving());
        agent_2= create_agent2();
        assertFalse(agent_2.isMoving());
    }

    /**
     * test for tor agent id functions
     */
    @Test
    void get_ID_test() {
        CL_Agent agent_1 = create_agent1();
        assertEquals(-1,agent_1.getID());
        assertNotEquals(6,agent_1.getID());
    }

    /**
     * test for tor agent location functions
     */
    @Test
    void get_Location_test() {
        CL_Agent agent_1 = create_agent1();
        assertEquals(1,agent_1.getLocation().x());
        assertEquals(2,agent_1.getLocation().y());
        assertEquals(3,agent_1.getLocation().z());
        agent_1 = create_agent2();
        assertEquals(8,agent_1.getLocation().x());
        assertEquals(9,agent_1.getLocation().y());
        assertEquals(10,agent_1.getLocation().z());
        assertNotNull(agent_1.getLocation());
    }

    /**
     * test for tor agent speed functions
     */
    @Test
    void set_and_get_Speed_test() {
        CL_Agent agent_2 ,agent_1 = create_agent1();
        assertEquals(0,agent_1.getSpeed());
        agent_1.setSpeed(2);
        assertEquals(2,agent_1.getSpeed());

        agent_2= create_agent2();
        assertEquals(0,agent_2.getSpeed());
        agent_2.setSpeed(5);
        assertEquals(5,agent_2.getSpeed());
        agent_2.setSpeed(2);
        assertEquals(agent_1.getSpeed(),agent_2.getSpeed());
    }

    /**
     * test for tor agent pokemon in search functions
     */
    @Test
    void set_and_get_curr_fruit_test() {
        CL_Agent agent_1 = create_agent1();
       CL_Pokemon pokemon_1,pokemon_2;
     Point3D point1 = new Point3D(3.5, 6.8, 67.9);
     pokemon_1 = new CL_Pokemon(point1, 1, 40, null);
     assertNull(agent_1.get_curr_fruit());

     agent_1.set_curr_fruit(pokemon_1);
     assertEquals(1,agent_1.get_curr_fruit().getType());
     assertEquals(40,agent_1.get_curr_fruit().getValue());

     Point3D point2 = new Point3D(5.8, 3.565, 20.777);
     pokemon_2 = new CL_Pokemon(point1, -1, 2, null);

     agent_1.set_curr_fruit(pokemon_2);
     assertEquals(-1,agent_1.get_curr_fruit().getType());
     assertEquals(2,agent_1.get_curr_fruit().getValue());
    }

    /**
     * test for tor agent current edge functions
     */
    @Test
    void get_curr_edge() {
        CL_Agent agent_1 = create_agent1();
        assertNull(agent_1.get_curr_edge());
    }

    /**
     * test for tor agent path by nodes functions
     */
    @Test
    void set_and_get_Point_arg_test() {
        CL_Agent agent_1 = create_agent1();
        List<node_data> node_list = new ArrayList<>();
        for (int i = 0; i< 10 ; i++) {
            node_data node_temp = new NodeData(i,i+1,i+2);
            node_list.add(node_temp);
        }
        agent_1.setPoint_arg(node_list,10);
        for (int i = 0; i <11 ; i++) {
            assertNotNull(agent_1.getPoint_arg().get(i));
        }
    }

    /**
     * test for tor agent path node counter functions
     */
    @Test
    void full_counter_test() {
        CL_Agent agent_1 = create_agent1();
        assertEquals(0,agent_1.getNode_counter());
        agent_1.add_node_count();
        assertEquals(1,agent_1.getNode_counter());
        for (int i = 0; i <10 ; i++) {
            agent_1.add_node_count();
        }
        assertEquals(11,agent_1.getNode_counter());
        assertNotEquals(20,agent_1.getNode_counter());
        agent_1.setNode_counter(100);
        assertEquals(100,agent_1.getNode_counter());
    }


}
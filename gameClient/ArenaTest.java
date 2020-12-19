package gameClient;

import api.*;
import gameClient.util.Point3D;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * this class is a test class for the Arena class . the tests here are mainly for function of getting and setting
 * the main function of the Arena class are being tested at the Ex2 class while running the class and check
 * the functionality of the details represent as a graphic context at the Ex2
 */
class ArenaTest {
   /**
    * test for functions of getting and setting the time of the arena
    */
    @Test
    void set_and_get_Time_test() {
       Arena arena_1 = new Arena();
       assertEquals(0,arena_1.getTime());

       arena_1.setTime(3434);
       assertEquals(3434,arena_1.getTime());
       assertNotEquals(0,arena_1.getTime());
       arena_1.setTime(44);
       assertEquals(44,arena_1.getTime());
       arena_1.setTime(-4646);
       assertEquals(0,arena_1.getTime());
    }

   /**
    * test for functions of getting and setting the pokemons collection of the arena
    */
    @Test
    void set_and_get_Pokemons_test() {
       Arena arena_1 = new Arena();
       List<CL_Pokemon> pok_list = new ArrayList<>();
       for (int i = 0; i <10 ; i++) {
          Point3D point1 = new Point3D(i+0.5, i+1.5, i+2.5);
          CL_Pokemon poki = new CL_Pokemon(point1, 1, i*3, null);
          pok_list.add(poki);
       }
       arena_1.setPokemons(pok_list);
       for (CL_Pokemon poki: pok_list) {
          assertTrue(arena_1.getPokemons().contains(poki));
       }
    }

   /**
    * test for function of getting and setting the graph of the arena
    */
    @Test
    void set_and_get_Graph_test() {
       Arena arena_1 = new Arena();
       directed_weighted_graph graph_1 = new DWGraph_DS();
       for (int i = 0; i < 15; i++) {
          node_data node_1 = new NodeData(i,i+1,i+2);
          graph_1.addNode(node_1);
       }
       arena_1.setGraph(graph_1);
       assertTrue(arena_1.getGraph().equals(graph_1));
    }

   /**
    * test for function of getting  the agents collection of the arena
    */
    @Test
    void get_Agents_info() {
    Arena arena_1 = new Arena();
    assertNotNull(arena_1.get_Agents_info());
    }

}
package gameClient;

import api.*;
import gameClient.util.Point3D;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ArenaTest {
 public String create_agents_collection_1()
 {
   String all_agents = "{\"Agents\":[{\"Agent\":{\"id\":0,\"value\":13.0,\"src\":38,\"dest\":39,\"speed\"" +
           ":1.0,\"pos\":\"35.20202363793417,32.106825517755034,0.0\"}},{\"Agent\":{\"id\":1,\"value\":12.0,\"src\":18,\"" +
           "dest\":17,\"speed\":1.0,\"pos\":\"35.194416604039354,32.1060718578479,0.0\"}},{\"Agent\":{\"id\":2,\"value\"" +
           ":14.0,\"src\":13,\"dest\":-1,\"speed\":1.0,\"pos\":\"35.209415362389024,32.10265552605042,0.0\"}}]}";
   return all_agents;
 }
 public String create_agents_collection_2()
 {
    String all_agents_2 = "{\"Agents\":[{\"Agent\":{\"id\":0,\"value\":159.0,\"src\":5,\"dest\":6,\"speed\":5.0,\"pos\"" +
            ":\"35.20794565010835,32.10444608893041,0.0\"}},{\"Agent\":{\"id\":1,\"value\":185.0,\"src\":30,\"dest\":29,\"" +
            "speed\":5.0,\"pos\":\"35.20253610714826,32.10307345622551,0.0\"}},{\"Agent\":{\"id\":2,\"value\":301.0,\"src\"" +
            ":31,\"dest\":30,\"speed\":5.0,\"pos\":\"35.19786798547216,32.10151062016807,0.0\"}}]}";
    return all_agents_2;
 }
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


    @Test
    void get_Agents_info() {
    Arena arena_1 = new Arena();
    assertNotNull(arena_1.get_Agents_info());
    }

    @Test
    void pokemon_contain() {
    }

    @Test
    void pokemon_in_search() {
    }

    @Test
    void json2Pokemons_update() {
    }

    @Test
    void closest_pokemon() {
    }

    @Test
    void on_pokemon_edge() {
    }
}
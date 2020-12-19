package gameClient;

import api.EdgeData;
import api.edge_data;
import gameClient.util.Point3D;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CL_PokemonTest {
    /**
     * create pokemon
     * @return
     */
    public CL_Pokemon create_pokemon_1() {
        edge_data edge1 = new EdgeData(1, 2, 45);
        Point3D point1 = new Point3D(3.5, 6.8, 67.9);
        CL_Pokemon poki = new CL_Pokemon(point1, 1, 40, edge1);
        return poki;
    }
    /**
     * create pokemon
     * @return
     */
    public CL_Pokemon create_pokemon_2() {
        Point3D point1 = new Point3D(5.8, 3.565, 20.777);
        CL_Pokemon poki = new CL_Pokemon(point1, -1, 2, null);
        return poki;
    }

    /**
     * test for get and set edge of pokemon
     */
    @Test
    void get_edge_and_set_test() {
        edge_data edge_temp1,edge_temp2;
        CL_Pokemon pokemon_2,pokemon_1 = create_pokemon_1();
        edge_temp1  = pokemon_1.get_edge();
        assertEquals(edge_temp1,pokemon_1.get_edge());
        edge_temp2 = new EdgeData(1,2,9.6);
        assertNotEquals(edge_temp2,pokemon_1.get_edge());
        pokemon_2 = create_pokemon_2();
        assertNull(pokemon_2.get_edge());
        pokemon_2.set_edge(edge_temp2);
        assertEquals(edge_temp2.getWeight(),pokemon_2.get_edge().getWeight());
        assertNotNull(pokemon_2.get_edge());

    }

    /**
     * test for the location of the pokemon
     */
    @Test
    void getLocation_test() {
        CL_Pokemon pokemon_1 = create_pokemon_1();
        assertEquals(3.5,pokemon_1.getLocation().x());
        assertEquals(6.8,pokemon_1.getLocation().y());
        assertEquals(67.9,pokemon_1.getLocation().z());
        pokemon_1 = create_pokemon_2();
        assertEquals(5.8,pokemon_1.getLocation().x());
        assertEquals(3.565,pokemon_1.getLocation().y());
        assertEquals(20.777,pokemon_1.getLocation().z());
        assertNotNull(pokemon_1.getLocation());
    }

    /**
     * test for the type of the pokemon
     */
    @Test
    void getType() {
        CL_Pokemon pokemon_1 = create_pokemon_1();
        assertEquals(1,pokemon_1.getType());
        assertNotEquals(-1,pokemon_1.getType());
        pokemon_1 = create_pokemon_2();
        assertEquals(-1,pokemon_1.getType());
        assertNotEquals(1,pokemon_1.getType());
    }

    /**
     * test for the value of the pokemon
     */
    @Test
    void getValue() {
        CL_Pokemon pokemon_1 = create_pokemon_1();
        assertEquals(40,pokemon_1.getValue());
        assertNotEquals(5,pokemon_1.getValue());
        pokemon_1 = create_pokemon_2();
        assertEquals(2,pokemon_1.getValue());
        assertNotEquals(100,pokemon_1.getValue());
    }

    /**
     * test for the compare to function that do comparison by value,
     */
    @Test
    void compareTo() {
        CL_Pokemon pokemon_2, pokemon_1 = create_pokemon_1();
        pokemon_2 = create_pokemon_2();
        assertEquals(-1,pokemon_1.compareTo(pokemon_2));
        assertNotEquals(1,pokemon_1.compareTo(pokemon_2));

        assertEquals(1,pokemon_2.compareTo(pokemon_1));
        assertNotEquals(-1,pokemon_2.compareTo(pokemon_1));
        pokemon_1 = create_pokemon_2();
        assertEquals(0,pokemon_1.compareTo(pokemon_2));
    }

    /**
     * test for the equals function
     */
    @Test
    void testEquals() {
        CL_Pokemon pokemon_2, pokemon_1 = create_pokemon_1();
        pokemon_2 = create_pokemon_2();
        assertFalse(pokemon_1.equals(pokemon_2));
        assertFalse(pokemon_2.equals(pokemon_1));
        pokemon_1 = create_pokemon_2();
        assertTrue(pokemon_1.equals(pokemon_2));
        assertTrue(pokemon_2.equals(pokemon_1));


    }
}
package gameClient;

import api.EdgeData;
import api.edge_data;
import gameClient.util.Point3D;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CL_PokemonTest {

    private CL_Pokemon create_pokemon() {
        edge_data edge1 = new EdgeData(1, 2, 45);
        Point3D point1 = new Point3D(3.5, 6.8, 67.9);
        CL_Pokemon poki = new CL_Pokemon(point1, 1, 40, edge1);
        return poki;
    }
    @Test
    void testToString() {
    }

    @Test
    void get_edge() {
    }

    @Test
    void set_edge() {
    }

    @Test
    void getLocation() {
    }

    @Test
    void getType() {
    }

    @Test
    void getValue() {
    }

    @Test
    void compareTo() {
    }

    @Test
    void testEquals() {
    }
}
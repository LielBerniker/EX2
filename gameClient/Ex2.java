package gameClient;

import Server.Game_Server_Ex2;
import api.*;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class Ex2 {
    public static void main(String[] a) throws IOException {
        int level =0,counter=0;
        boolean agent_add=true;
        edge_data edge_temp;
        dw_graph_algorithms algo_run = new DWGraph_Algo();
        game_service game1 = Game_Server_Ex2.getServer(level);
        String level_graph = game1.getGraph();
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("level_"+Integer.toString(level)));
            writer.write(level_graph);
            writer.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        algo_run.load("level_"+Integer.toString(level));

        HashMap<Integer,CL_Agent> Agents = new HashMap<>();
       ArrayList<CL_Pokemon> Pokemons = Arena.json2Pokemons(game1.getPokemons());
       PriorityQueue <CL_Pokemon> Pokemons_pri = new PriorityQueue<CL_Pokemon>();
        Iterator<CL_Pokemon> it = Pokemons.iterator();
        while(it.hasNext()) {
            CL_Pokemon pokemon_go =it.next();
           pokemon_go.set_edge(Arena.correct_pokemon_edge(algo_run.getGraph(),pokemon_go));
            Pokemons_pri.add(pokemon_go);

        }

        while(agent_add )
        {
            if( !Pokemons_pri.isEmpty())
            {edge_temp = Pokemons_pri.poll().get_edge();
          agent_add =  game1.addAgent(edge_temp.getSrc());
            game1.chooseNextEdge(counter,edge_temp.getDest());
            counter++;}
           else
                agent_add =  game1.addAgent(1);
        }
      game1.startGame();
while (game1.isRunning())
{

}

    }

}

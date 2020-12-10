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
        CL_Pokemon poki_temp;
        dw_graph_algorithms algo_run = new DWGraph_Algo();

        Queue<CL_Pokemon> look_for_poki = new LinkedList<CL_Pokemon>();

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


       ArrayList<CL_Pokemon> Pokemons = Arena.json2Pokemons(game1.getPokemons());
       PriorityQueue <CL_Pokemon> Pokemons_pri = new PriorityQueue<CL_Pokemon>();
        Iterator<CL_Pokemon> it = Pokemons.iterator();
        while(it.hasNext()) {
            CL_Pokemon pokemon_go =it.next();
           pokemon_go.set_edge(Arena.correct_pokemon_edge(algo_run.getGraph(),pokemon_go));
            Pokemons_pri.add(pokemon_go);
        }
     // add agents to the game
        while(agent_add )
        {// if not all pokemons have been seek choose
            if( !Pokemons_pri.isEmpty())
            {poki_temp = Pokemons_pri.poll();
            // add the agent and reset its starting node
            agent_add =  game1.addAgent(poki_temp.get_edge().getSrc());
            // set the agent next node to be the dest
            game1.chooseNextEdge(counter,poki_temp.get_edge().getDest());
            // add the pokimon to the seeking pokemon collection
            look_for_poki.add(poki_temp);
            counter++;
            // of didedt been added
            if(!agent_add)
            {
                // return the pokemon to the waiting area
             Pokemons_pri.add(poki_temp);
             // and remove the pokemon from seeking pokemon
             look_for_poki.remove(poki_temp);
            }
            }
           else
               // add agent random
                agent_add =  game1.addAgent(1);
        }
        // list of agents after add
        List<CL_Agent> agents= Arena.getAgents(game1.getAgents(),algo_run.getGraph());
        // go over the agents and set their pokemon
        for (int i = 0; i <look_for_poki.size() ; i++) {
            CL_Agent agent_temp = agents.get(i);
            agent_temp.set_curr_fruit(look_for_poki.poll());
        }
      game1.startGame();
        System.out.println(game1.move());
while (game1.isRunning())
{
    if(Arena.free_agents(agents)==true) {
        Pokemons = Arena.json2Pokemons(game1.getPokemons());

    }

}

    }

}

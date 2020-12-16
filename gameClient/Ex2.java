package gameClient;

import Server.Game_Server_Ex2;
import api.*;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class Ex2 implements Runnable {
    private static frame _screen;
    private Arena game_arena;
    public static void main(String[] a) throws IOException {

        Thread user = new Thread(new Ex2());
        user.start();

    }
    @Override
    public void run() {
        int scenario,speed_all ;
        boolean flag;
        Scanner myObj = new Scanner(System.in);  // Create a Scanner object
        do {
            System.out.println("Please enter scenario");
            scenario = myObj.nextInt();  // Read user input
            if (scenario<0 || scenario>23)
            {
                System.out.println("there is no scenario " + scenario);
                flag = false;
            }
            else flag = true;
        }while (flag==false);
        //System.out.println("Please enter use id");
        //int id = myObj.nextInt();  // Read user input

        int id = 315708370;
        game_arena= new Arena();// an arena object to help with multiply functions
        dw_graph_algorithms algo_run = new DWGraph_Algo();// graph algorithms to help create a graph and use more function on it
        game_service game1 = Game_Server_Ex2.getServer(scenario);// the game level

        game1.login(id);
        try {
            init_graph_to_algo(scenario,game1,algo_run);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(game1);
        PriorityQueue <CL_Pokemon> Pokemons_pri = init_pokemones(game1,game_arena,algo_run);// collection of pokemos by their value
        try {
            add_all_agents(game_arena,game1,Pokemons_pri,algo_run);// collection of pokemons that been search by an agent
        } catch (JSONException e) {
            e.printStackTrace();
        }
        game_arena.setGraph(algo_run.getGraph());
        _screen = new frame("test Ex2");
        _screen.setSize(1000, 700);
        _screen.panel.update(game_arena);


        _screen.show();
        game1.startGame();
       // _screen.setTitle("Ex2 - OOP: (NONE trivial Solution) "+game1.toString());
        int ind=0;
        long dt=120;

        while (game1.isRunning())
        {

           game_arena.setTime(game1.timeToEnd()/1000);
           speed_all = game_full_move_2(game1,game_arena,algo_run);
            game1.move();
            System.out.println(game1.getAgents());
                try {
                    if (ind % 1 == 0) {
                        _screen.repaint();
                    }
                    Thread.sleep(dt-((10-speed_all)*speed_all)-1);
                                     ind++;
                               }
                              catch(Exception e) {
                                  e.printStackTrace();
                             }
            }
        String res = game1.toString();

        System.out.println(res);
        System.exit(0);
    }



    /**
     * the function set the graph in the algo , to the graph of the number of the level
     * provided
     * doing this by getting the graph from the game and write it to a file
     * than read it with the load function in the dw_graph_algorithms
     * @param level
     * @param game1
     * @param algo1
     * @throws IOException
     */
    public static void init_graph_to_algo(int level, game_service game1, dw_graph_algorithms algo1) throws IOException
{
    String level_graph = game1.getGraph();
    try {
        BufferedWriter writer = new BufferedWriter(new FileWriter("level_"+Integer.toString(level)));
        writer.write(level_graph);
        writer.close();
    }
    catch (IOException e) {
        e.printStackTrace();
    }
    algo1.load("level_"+Integer.toString(level));
}

    /**
     * the function create a  PriorityQueue and return it
     * the function get the pokemons from the game and set tham to a list
     * than go over the list an insert them into a Priority Queue
     * also sets the arena pokemons
     * @param game1
     * @param arena
     * @param algo
     * @return   return a Priority Queue of pokemons
     */
    public static PriorityQueue<CL_Pokemon> init_pokemones(game_service game1, Arena arena,dw_graph_algorithms algo)
{
    PriorityQueue <CL_Pokemon> Pokemons_pri = new PriorityQueue<CL_Pokemon>();
    ArrayList<CL_Pokemon> pokemons= arena.json2Pokemons(game1.getPokemons());
    // go over the pokemons list an insert them into a  Priority Queue
    Iterator<CL_Pokemon> it = pokemons.iterator();
    while(it.hasNext()) {
        CL_Pokemon pokemon_go =it.next();
        // set the pokemon edge before the insert to the Priority Queue
        pokemon_go.set_edge(arena.correct_pokemon_edge(algo.getGraph(),pokemon_go));
        Pokemons_pri.add(pokemon_go);
    }
    arena.setPokemons(pokemons);
    return Pokemons_pri;
}

    /**
     * the function return a Queue of pokemons that contain all the pokemons,
     * that currently been set to an agent
     * the function add an agents to the game and set their start node .
     * an agent start node sets by the pokemon from the Priority Queue
     * the start node been set to the closest node to the pokemon that been chose to the agent
     *
     * @param arena
     * @param game
     * @param pokemons_order
     * @return
     */
    public static void add_all_agents(Arena arena, game_service game ,PriorityQueue<CL_Pokemon> pokemons_order, dw_graph_algorithms algo) throws JSONException {
    boolean agent_add=true;
    CL_Pokemon poki_temp;
    int  rand;
    rand = random_node(algo);
    Queue<CL_Pokemon> look_for_pokemons = new LinkedList<CL_Pokemon>();
    JSONObject full_info = new JSONObject(game.toString());
    JSONObject g_server = full_info.getJSONObject("GameServer");
    int agents_all = g_server.getInt("agents");
    // add agents to the game

        for (int i = 0; i < agents_all; i++)
    {// if not all pokemons have been seek choose
        if( !pokemons_order.isEmpty()) {
            poki_temp = pokemons_order.poll();
            // add the agent and reset its starting node
            game.addAgent(poki_temp.get_edge().getSrc());
            // add the pokimon to the seeking pokemon collection
            look_for_pokemons.add(poki_temp);
        }
        else
        {  // add agent random
            rand = random_node(algo);
            agent_add =  game.addAgent(rand);}
    }
    arena.init_Agents_by_game(game.getAgents(),algo.getGraph());

    // go over the agents and set their pokemon
    for (int i = 0; i <arena.get_Agents_info().size() ; i++) {
        arena.get_Agents_info().get(i).set_curr_fruit(look_for_pokemons.poll());
        CL_Agent agn_temp = arena.get_Agents_info().get(i);
        CL_Pokemon poki2 = agn_temp.get_curr_fruit();

        int src_node = agn_temp.getSrcNode();
        int dest_node = poki2.get_edge().getSrc();
        List<node_data> node_list = algo.shortestPath(src_node,dest_node);
        agn_temp.setPoint_arg(node_list,poki2.get_edge().getDest());
    }
}

    /**
     * choose random a node from the graph
     * and return it
     * @param algo
     * @return
     */
    public static int random_node(dw_graph_algorithms algo)
{
    int rand ;
     rand  = (int)(Math.random()*(algo.getGraph().nodeSize()-1));
     return rand;
}
public static void game_full_move(game_service game ,Arena arena,dw_graph_algorithms algo)
{
    int id_agn, current_count=0;
    PriorityQueue <CL_Pokemon> Pokemons_pri = new PriorityQueue<>();
    arena.setPokemons( arena.json2Pokemons_update(game.getPokemons(), algo.getGraph()));
    arena.get_Agents_update(game.getAgents());
    arena.setAgents(arena.getAgents(game.getAgents(), algo.getGraph()));
    for (CL_Agent agn:arena.get_Agents_info().values()) {
        if(!arena.pokemon_contain(agn))
            arena.get_Agents_info().get(agn.getID()).set_curr_fruit(null);
    }
    for (CL_Pokemon poki: arena.getPokemons()) {
       if(!arena.pokemon_in_search(poki))
        { Pokemons_pri.add(poki); }
    }
    while(arena.available_agents())
    {
        CL_Pokemon poki_temp = Pokemons_pri.poll();
        id_agn = arena.closest_agents(poki_temp,algo);
        CL_Agent agn_temp = arena.get_Agents_info().get(id_agn);
        agn_temp.set_curr_fruit(poki_temp);
        int src_node = agn_temp.getSrcNode();
        int dest_node = poki_temp.get_edge().getSrc();
        List<node_data> node_list = algo.shortestPath(src_node,dest_node);
        agn_temp.setPoint_arg(node_list,poki_temp.get_edge().getDest());
        agn_temp.setNode_counter(1);

    }
    for (CL_Agent agn_go:arena.get_Agents_info().values()) {
        if(agn_go.get_curr_fruit()!=null && agn_go.getNextNode()==-1) {
            current_count = agn_go.getNode_counter();
            if(agn_go.getNode_counter()<agn_go.getPoint_arg().size())
            { int next_node = agn_go.getPoint_arg().get(current_count);
            agn_go.add_node_count();
            game.chooseNextEdge(agn_go.getID(),next_node);
           }
            else
            {
                agn_go.set_curr_fruit(null);
            }
        }
    }
}
    public static int game_full_move_2(game_service game ,Arena arena,dw_graph_algorithms algo)
    {
        double all_speed=0;
        int current_count=0,avg_speed;
       ArrayList <CL_Pokemon> Pokemons_list = new ArrayList<>();
        arena.setPokemons( arena.json2Pokemons_update(game.getPokemons(), algo.getGraph()));
        arena.get_Agents_update(game.getAgents());
        for (CL_Pokemon poki: arena.getPokemons()) {
            if(!arena.pokemon_in_search(poki))
            { Pokemons_list.add(poki); }
        }

        for (int i = 0; i <arena.get_Agents_info().size() ; i++) {
            CL_Agent agn_temp = arena.get_Agents_info().get(i);
            if(agn_temp.get_curr_fruit()==null) {
                if(!Pokemons_list.isEmpty()) {
                    CL_Pokemon poki_temp = arena.closest_pokemon(agn_temp, Pokemons_list, algo);
                    agn_temp.set_curr_fruit(poki_temp);
                    int src_node = agn_temp.getSrcNode();
                    int dest_node = poki_temp.get_edge().getSrc();
                    List<node_data> node_list = algo.shortestPath(src_node, dest_node);
                    agn_temp.setPoint_arg(node_list, poki_temp.get_edge().getDest());
                    agn_temp.setNode_counter(1);
                }
            }
            if (agn_temp.get_curr_fruit()!=null && agn_temp.getNextNode()==-1)
            {
                current_count = agn_temp.getNode_counter();
                if(agn_temp.getNode_counter()<agn_temp.getPoint_arg().size())
                { int next_node = agn_temp.getPoint_arg().get(current_count);
                    agn_temp.add_node_count();
                    game.chooseNextEdge(agn_temp.getID(),next_node);
                }
                else
                {
                    agn_temp.set_curr_fruit(null);
                }

              }
            all_speed = all_speed + agn_temp.getSpeed();
            }
        avg_speed =(int)(all_speed/arena.get_Agents_info().size());
        return avg_speed;
        }
    }



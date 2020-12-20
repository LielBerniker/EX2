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

    public static int id;
    public static int scenario;
    public static Thread user;
    private static frame _screen=new frame();
    private Arena game_arena;
    private static enteryPanel entry =new enteryPanel();
    private static Panel panel;

    public static void main(String[] args) throws IOException {

        user = new Thread(new Ex2());

       if(args.length==0){
           _screen.setSize(1000,700);
           _screen.add(entry);
           _screen.setVisible(true);

       }else{
           id=Integer.parseInt(args[0]);
           scenario=Integer.parseInt(args[1]);
           user.start();
       }

    }
    @Override
    public void run() {
        _screen.remove(entry);

        int  ind=0;
        long dt=125,sleep_change=0, time_to,on_edge=0;

        game_arena= new Arena();// an arena object to help with multiply functions

        dw_graph_algorithms algo_run = new DWGraph_Algo();// graph algorithms to help create a graph and use more function on it

        game_service game1 =Game_Server_Ex2.getServer(this.scenario);// the game level

        // login to the game with id
        game1.login(this.id);

        try {
            // initiate the graph by the information from the game
            init_graph_to_algo(this.scenario,game1,algo_run);

        } catch (IOException e) {
            e.printStackTrace();
        }
        // set the arena graph
        game_arena.setGraph(algo_run.getGraph());
        init_screen(game_arena);

        System.out.println(game1);
        // create a  Priority Queue of Pokemons by their value
        PriorityQueue <CL_Pokemon> Pokemons_pri = init_pokemones(game1,game_arena,algo_run);// collection of pokemos by their value
        try {
            // add agents to the game next to the pokemons in the  Priority Queue
            add_all_agents(game_arena,game1,Pokemons_pri,algo_run);// collection of pokemons that been search by an agent
        } catch (JSONException e) {
            e.printStackTrace();
        }

        game1.startGame();

        while (game1.isRunning())
        {
             time_to=(game1.timeToEnd()/1000);
           game_arena.setTime(time_to);
         game_full_move(game1,game_arena,algo_run);
            game1.move();
            _screen.repaint();

            if (game_arena.on_pokemon_edge()==true)
                 on_edge =50;
            else
                on_edge = 0;
            if (time_to<=7)
                sleep_change =20;
            System.out.println(game1.getAgents());
                try {

                    Thread.sleep(dt-(on_edge+sleep_change));
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
        // write the graph into a file
        BufferedWriter writer = new BufferedWriter(new FileWriter("level_"+Integer.toString(level)));
        writer.write(level_graph);
        writer.close();
    }
    catch (IOException e) {
        e.printStackTrace();
    }
    // load the graph into thr graph algo
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
    ArrayList<CL_Pokemon> pokemons= arena.json2Pokemons_update(game1.getPokemons());
    // go over the pokemons list an insert them into a  Priority Queue
    Iterator<CL_Pokemon> it = pokemons.iterator();
    while(it.hasNext()) {
        CL_Pokemon pokemon_go =it.next();
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
            game.addAgent(rand);}
    }
    arena.init_Agents_by_game(game.getAgents());

    // go over the agents and set their pokemon
    for (int i = 0; i <arena.get_Agents_info().size() ; i++) {
        arena.get_Agents_info().get(i).set_curr_fruit(look_for_pokemons.poll());
        CL_Agent agn_temp = arena.get_Agents_info().get(i);
        CL_Pokemon poki2 = agn_temp.get_curr_fruit();
        int src_node = agn_temp.getSrcNode();
        int dest_node = poki2.get_edge().getSrc();
        // find the list of nodes to the pokemon location from the agent location
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

    /**
     * the function rest the pokemons and agents collection in the arena by the json from the game
     * the function create a list of pokemons that not being searched .
     * than go over the agent and if an agent is not in a search of a pokemon
     * the function set the agent to search for a pokemon from the list
     * the if the agent is at is destination node sets a new node from the path to the pokemon
     * @param game
     * @param arena
     * @param algo
     * @return
     */
    public static int game_full_move(game_service game ,Arena arena,dw_graph_algorithms algo)
    {
        double all_speed=0;
        int current_count=0,avg_speed;
       ArrayList <CL_Pokemon> Pokemons_list = new ArrayList<>();
       // sets the pokemons by the json from the game
        arena.setPokemons( arena.json2Pokemons_update(game.getPokemons()));
        // sets the agents by the json from the game
        arena.get_Agents_update(game.getAgents());
        // go over the pokemons to check which one are not being search and insert them to a list
        for (CL_Pokemon poki: arena.getPokemons()) {
            if(!arena.pokemon_in_search(poki))
            { Pokemons_list.add(poki); }
        }
// go over the agent and sets their new pokemon to search and their new destination node
        for (CL_Agent agn_temp:arena.get_Agents_info().values())
            {
                // if the agent not in a search of a pokemon
            if(agn_temp.get_curr_fruit()==null) {
                // if there is a pokemon to search for
                if(!Pokemons_list.isEmpty()) {
                    // get the closest pokemon to the agent
                    CL_Pokemon poki_temp = arena.closest_pokemon(agn_temp, Pokemons_list, algo);
                    agn_temp.set_curr_fruit(poki_temp);
                    int src_node = agn_temp.getSrcNode();
                    int dest_node = poki_temp.get_edge().getSrc();
                    // return the shortest path to the pokemon and set to the path of the agent
                    List<node_data> node_list = algo.shortestPath(src_node, dest_node);
                    agn_temp.setPoint_arg(node_list, poki_temp.get_edge().getDest());
                    agn_temp.setNode_counter(1);
                }
            }
            // if the agent is in a search for pokemon and reach his destination node
            if (agn_temp.get_curr_fruit()!=null && agn_temp.getNextNode()==-1)
            {
                current_count = agn_temp.getNode_counter();
                // if there ia more to the path to the pokemon
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
        return ((int)(Math.ceil(all_speed/arena.get_Agents_info().size())));
        }


    /**
     * the function get a arena and sets the frame by the arena
     * @param game_arena
     */

        public static void init_screen(Arena game_arena)
        {
            _screen = new frame();
            _screen.setSize(1000, 700);
            panel=new Panel();
            _screen.add(panel);
            panel.update(game_arena);
            _screen.show();
        }
    }



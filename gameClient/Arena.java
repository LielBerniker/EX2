package gameClient;

import api.*;
import gameClient.util.Point3D;
import gameClient.util.Range;
import gameClient.util.Range2D;
import gameClient.util.Range2Range;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.*;
import java.util.zip.DeflaterOutputStream;

/**
 * This class represents a multi Agents Arena which move on a graph - grabs Pokemons
 * by the code of boaz.benmoshe
 */
public class Arena {
	public static final double EPS1 = 0.00001, EPS2=EPS1*EPS1, EPS=EPS2;
	private directed_weighted_graph _gg; // the graph that the arena operates on
	private List<CL_Pokemon> _pokemons;// the collection of pokemons
	private long time; // time to the end of the game
	private List<String> _info;
	private static Point3D MIN = new Point3D(0, 100,0);
	private static Point3D MAX = new Point3D(0, 100,0);
	private HashMap<Integer,CL_Agent> agents_information; // collection of agents in the game

	/**
	 * constructor to the arena
	 */
	public Arena() {
		_info = new ArrayList<String>();
		agents_information = new HashMap<Integer, CL_Agent>();
		time=0;
	}

	/**
	 * the function sets the time of the game
	 * @param t
	 */
	public void setTime(long t){
		if(t>=0)
		this.time=t;
		else
			this.time = 0;
	}

	/**
	 * the function return the current time of the game until it finish
	 * @return
	 */
	public  long getTime(){
		return this.time;
	}

	/**
	 * sets the pokemons in the game
	 * @param f
	 */
	public void setPokemons(List<CL_Pokemon> f) {
		this._pokemons = f;
	}

	/**
	 * set the graph of the game
	 * @param g
	 */
	public void setGraph(directed_weighted_graph g) {this._gg =g;}//init();}

	/**
	 * return the current pokemons in the game
	 * @return
	 */
	public List<CL_Pokemon> getPokemons() {return _pokemons;}

	/**
	 *
	 * @return
	 */
	public directed_weighted_graph getGraph() {
		return _gg;
	}

	/**
	 * return the information about the area
	 * @return
	 */
	public List<String> get_info() {
		return _info;
	}

	/**
	 * sets the information about the area
	 * @param _info
	 */
	public void set_info(List<String> _info) {
		this._info = _info;
	}

	public static Range2D GraphRange(directed_weighted_graph g) {
		Iterator<node_data> itr = g.getV().iterator();
		double x0=0,x1=0,y0=0,y1=0;
		boolean first = true;
		while(itr.hasNext()) {
			geo_location p = itr.next().getLocation();
			if(first) {
				x0=p.x(); x1=x0;
				y0=p.y(); y1=y0;
				first = false;
			}
			else {
				if(p.x()<x0) {x0=p.x();}
				if(p.x()>x1) {x1=p.x();}
				if(p.y()<y0) {y0=p.y();}
				if(p.y()>y1) {y1=p.y();}
			}
		}
		Range xr = new Range(x0,x1);
		Range yr = new Range(y0,y1);
		return new Range2D(xr,yr);
	}
	public static Range2Range w2f(directed_weighted_graph g, Range2D frame) {
		Range2D world = GraphRange(g);
		Range2Range ans = new Range2Range(world, frame);
		return ans;
	}

	/**
	 * the function return the edge from the current graph that the Pokemon is located on
	 * if no edge match to the location of the pokemon return null
	 * @param poki
	 * @return
	 */
	public  edge_data correct_pokemon_edge( CL_Pokemon poki)
{
	for (node_data current_node :getGraph().getV())
	{// go over the edges of the graph
		for (edge_data current_edge:getGraph().getE(current_node.getKey())) {
			// if == true return the edge
			if(isOnEdge(poki.getLocation(),current_edge,poki.getType(),getGraph()))
				return current_edge;
		}
	}
	return null;
}

	/**
	 * inner function to use of correct_pokemon_edge function return true or false if
	 * the current edge is the edge witch the pokemon located on
	 * @param p
	 * @param src
	 * @param dest
	 * @return
	 */
	private  boolean isOnEdge(geo_location p, geo_location src, geo_location dest ) {
		boolean ans = false;
		double dist = src.distance(dest);
		double d1 = src.distance(p) + p.distance(dest);
		if(dist>d1-EPS2) {ans = true;}
		return ans;
	}

	/**
	 * * inner function to use of correct_pokemon_edge function return true or false if
	 * 	 * the current edge is the edge witch the pokemon located on
	 * @param p
	 * @param s
	 * @param d
	 * @param g
	 * @return
	 */
	private  boolean isOnEdge(geo_location p, int s, int d, directed_weighted_graph g) {
		geo_location src = g.getNode(s).getLocation();
		geo_location dest = g.getNode(d).getLocation();
		return isOnEdge(p,src,dest);
	}

	/**
	 * * inner function to use of correct_pokemon_edge function return true or false if
	 * 	 * the current edge is the edge witch the pokemon located on
	 * @param p
	 * @param e
	 * @param type
	 * @param g
	 * @return
	 */
	private  boolean isOnEdge(geo_location p, edge_data e, int type, directed_weighted_graph g) {
		int src = g.getNode(e.getSrc()).getKey();
		int dest = g.getNode(e.getDest()).getKey();
		if(type<0 && dest>src) {return false;}
		if(type>0 && src>dest) {return false;}
		return isOnEdge(p,src, dest, g);
	}

	/**
	 * initiates the agents information in the agents collection by the json string
	 * the function gets
	 * @param agents_info
	 */
	public void init_Agents_by_game(String agents_info) {
		try {
			JSONObject all_agents = new JSONObject(agents_info);
			JSONArray ags = all_agents.getJSONArray("Agents");// take the agents part of the json
			// go over the json and take every information in the json array
			for(int i=0;i<ags.length();i++) {
				CL_Agent c = new CL_Agent(getGraph(),0);
				// sets the information by the specific agents json information
				c.update(ags.get(i).toString());
				agents_information.put(c.getID(),c);
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	/**
	 * the function update the agents collection by the information from the json string
	 * @param aa
	 */
	public void get_Agents_update(String aa) {

		try {
			JSONObject all_agents = new JSONObject(aa);
			JSONArray ags = all_agents.getJSONArray("Agents");
			// go over the json and take every information in the json array
			for(int i=0;i<ags.length();i++) {
				// call inner function to update the agents collection
				update_agent(i,ags.get(i).toString());
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	/**
	 *the function take a json string of a specific agents ans set his details by the details from the json
	 * @param id_agent
	 * @param json
	 */
	private void update_agent(int id_agent,String json) {
		JSONObject line;
		try {
			// get from the json string the information about the agent
			line = new JSONObject(json);
			JSONObject agents_full = line.getJSONObject("Agent");
			int id = agents_full.getInt("id");
				double speed = agents_full.getDouble("speed");
				String p = agents_full.getString("pos");
				int src = agents_full.getInt("src");
				int dest = agents_full.getInt("dest");
				double value = agents_full.getDouble("value");
				// sets thr information by the json
				agents_information.get(id_agent).set_pos(p);
				agents_information.get(id_agent).setCurrNode(src);
				agents_information.get(id_agent).setSpeed(speed);
				agents_information.get(id_agent).setNextNode(dest);
				agents_information.get(id_agent).setMoney(value);
				// if the agent is looking for a pokemon checks if the pokemon not been eaten
				if (agents_information.get(id).get_curr_fruit()!=null)
				{if(!pokemon_contain(agents_information.get(id_agent)))
					get_Agents_info().get(id_agent).set_curr_fruit(null);}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * return the collection holding the agents full information
	 * @return
	 */
	public HashMap<Integer,CL_Agent> get_Agents_info() {
		return agents_information;
	}

	/**
	 * the function checks that the pokemon that the agent is looking for
	 * is available , if not return false else true
	 * @param agn
	 * @return
	 */
	public boolean pokemon_contain(CL_Agent agn)
	{
		for (CL_Pokemon poke:this.getPokemons()) {
			 {
				if (agn.get_curr_fruit().equals(poke))
					return true;
			}
		}
		return false;
	}
	/**
	 * the function checks that the current pokemon is already in progress of searching
	 * return true if in progress and false if not
	 * @param pok
	 * @return
	 */
	public boolean pokemon_in_search(CL_Pokemon pok )
	{
		for (CL_Agent agn: this.get_Agents_info().values()) {
			if(agn.get_curr_fruit()!=null) {
				if (agn.get_curr_fruit().equals(pok))
					return true;
			}
		}
			return  false;
	}

	/**
	 * the function return a list of pokemons by the json information the function gets
	 * @param fs
	 * @return
	 */
	public  ArrayList<CL_Pokemon> json2Pokemons_update(String fs) {
		ArrayList<CL_Pokemon> ans = new ArrayList<>();
		try {
			// get the information about pokemons from the json
			JSONObject all_info = new JSONObject(fs);
			JSONArray pkemons = all_info.getJSONArray("Pokemons");
			// go over the pokemons and crate a pokemon by the json
			for(int i=0;i<pkemons.length();i++) {
				JSONObject pp = pkemons.getJSONObject(i);
				JSONObject pk = pp.getJSONObject("Pokemon");
				int t = pk.getInt("type");
				double v = pk.getDouble("value");
				String p = pk.getString("pos");
				CL_Pokemon f = new CL_Pokemon(new Point3D(p), t, v, null);
				// sets the pokemon edge
				f.set_edge(correct_pokemon_edge(f));
				ans.add(f);
			}
		}
		catch (JSONException e) {e.printStackTrace();}
		return ans;
	}

	/**
	 * the function return the closest pokemon to the specific agent
	 * @param agn
	 * @param Pokemons_list
	 * @param algo
	 * @return
	 */
	public CL_Pokemon closest_pokemon(CL_Agent agn, ArrayList <CL_Pokemon> Pokemons_list, dw_graph_algorithms algo)
	{ CL_Pokemon poki = null;
		double small_path_algo = Double.MAX_VALUE,temp_dis;
		// go over the pokemns and sets the closest one to an object
		for (CL_Pokemon pokemon_temp:Pokemons_list) {
				temp_dis = algo.shortestPathDist(agn.getSrcNode(),pokemon_temp.get_edge().getSrc());
				if (temp_dis<=small_path_algo)
				{small_path_algo= temp_dis;
					poki = pokemon_temp;}
		}
		return poki;
	}


	/**
	 * return true if the current agent is on an edge with a pokemon
	 * else return false
	 * @return
	 */
	public boolean on_pokemon_edge()
{
	for (CL_Agent agent_temp:this.get_Agents_info().values()) {
		CL_Pokemon temp_poki = agent_temp.get_curr_fruit();
		if(temp_poki!=null && agent_temp.getSrcNode() ==temp_poki.get_edge().getSrc())
			return true;
	}
	return  false;
}
}

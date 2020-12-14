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
 * This class represents a multi Agents Arena which move on a graph - grabs Pokemons and avoid the Zombies.
 * @author boaz.benmoshe
 *
 */
public class Arena {
	public static final double EPS1 = 0.00001, EPS2=EPS1*EPS1, EPS=EPS2;
	private directed_weighted_graph _gg;
	private List<CL_Agent> _agents;
	private List<CL_Pokemon> _pokemons;
	private long time;

	private List<String> _info;
	private static Point3D MIN = new Point3D(0, 100,0);
	private static Point3D MAX = new Point3D(0, 100,0);

	private HashMap<Integer,CL_Agent> agents_information;

	public Arena() {;
		_info = new ArrayList<String>();
		agents_information = new HashMap<Integer, CL_Agent>();
		time=0;
	}
	private Arena(directed_weighted_graph g, List<CL_Agent> r, List<CL_Pokemon> p) {
		_gg = g;
		this.setAgents(r);
		this.setPokemons(p);
	}

	public void setTime(long t){
		this.time=t;
	}
	public  long getTime(){
		return this.time;
	}

	public void setPokemons(List<CL_Pokemon> f) {
		this._pokemons = f;
	}

	public void setAgents(List<CL_Agent> f) {
		this._agents = f;
	}

	public void setGraph(directed_weighted_graph g) {this._gg =g;}//init();}

	private void init( ) {

		MIN=null; MAX=null;
		double x0=0,x1=0,y0=0,y1=0;
		Iterator<node_data> iter = _gg.getV().iterator();
		while(iter.hasNext()) {
			geo_location c = iter.next().getLocation();
			if(MIN==null) {x0 = c.x(); y0=c.y(); x1=x0;y1=y0;MIN = new Point3D(x0,y0);}
			if(c.x() < x0) {x0=c.x();}
			if(c.y() < y0) {y0=c.y();}
			if(c.x() > x1) {x1=c.x();}
			if(c.y() > y1) {y1=c.y();}
		}
		double dx = x1-x0, dy = y1-y0;
		MIN = new Point3D(x0-dx/10,y0-dy/10);
		MAX = new Point3D(x1+dx/10,y1+dy/10);
		
	}
	public List<CL_Agent> getAgents() {return _agents;}

	public List<CL_Pokemon> getPokemons() {return _pokemons;}

	
	public directed_weighted_graph getGraph() {
		return _gg;
	}

	public List<String> get_info() {
		return _info;
	}

	public void set_info(List<String> _info) {
		this._info = _info;
	}


	public static List<CL_Agent> getAgents(String aa, directed_weighted_graph gg) {
		ArrayList<CL_Agent> ans = new ArrayList<CL_Agent>();
		try {
			JSONObject ttt = new JSONObject(aa);
			JSONArray ags = ttt.getJSONArray("Agents");
			for(int i=0;i<ags.length();i++) {
				CL_Agent c = new CL_Agent(gg,0);
				c.update(ags.get(i).toString());
				ans.add(c);
			}
			//= getJSONArray("Agents");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return ans;
	}

	public static ArrayList<CL_Pokemon> json2Pokemons(String fs) {
		ArrayList<CL_Pokemon> ans = new ArrayList<>();
		try {
			JSONObject ttt = new JSONObject(fs);
			JSONArray ags = ttt.getJSONArray("Pokemons");
			for(int i=0;i<ags.length();i++) {
				JSONObject pp = ags.getJSONObject(i);
				JSONObject pk = pp.getJSONObject("Pokemon");
				int t = pk.getInt("type");
				double v = pk.getDouble("value");
				//double s = 0;//pk.getDouble("speed");
				String p = pk.getString("pos");

				CL_Pokemon f = new CL_Pokemon(new Point3D(p), t, v, 0, null);
				ans.add(f);
			}
		}
		catch (JSONException e) {e.printStackTrace();}
		return ans;
	}
	public static void updateEdge(CL_Pokemon fr, directed_weighted_graph g) {
		//	oop_edge_data ans = null;
		Iterator<node_data> itr = g.getV().iterator();
		while(itr.hasNext()) {
			node_data v = itr.next();
			Iterator<edge_data> iter = g.getE(v.getKey()).iterator();
			while(iter.hasNext()) {
				edge_data e = iter.next();
				boolean f = isOnEdge(fr.getLocation(), e,fr.getType(), g);
				if(f) {fr.set_edge(e);}
			}
		}
	}



	private static Range2D GraphRange(directed_weighted_graph g) {
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


	//my area:
	//$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$

	/**
	 * the function return the edge from the current graph that the Pokemon is located on
	 * if no edge match to the location of the pokemon return null
	 * @param graph1
	 * @param poki
	 * @return
	 */
	public static edge_data correct_pokemon_edge(directed_weighted_graph graph1 , CL_Pokemon poki)
{
	for (node_data current_node :graph1.getV())
	{// go over the edges of the graph
		for (edge_data current_edge:graph1.getE(current_node.getKey())) {
			// if == true return the edge
			if(isOnEdge(poki.getLocation(),current_edge,poki.getType(),graph1))
				return current_edge;
		}
	}
	return null;
}
	private static boolean isOnEdge(geo_location p, geo_location src, geo_location dest ) {

		boolean ans = false;
		double dist = src.distance(dest);
		double d1 = src.distance(p) + p.distance(dest);
		if(dist>d1-EPS2) {ans = true;}
		return ans;
	}
	private static boolean isOnEdge(geo_location p, int s, int d, directed_weighted_graph g) {
		geo_location src = g.getNode(s).getLocation();
		geo_location dest = g.getNode(d).getLocation();
		return isOnEdge(p,src,dest);
	}
	private static boolean isOnEdge(geo_location p, edge_data e, int type, directed_weighted_graph g) {
		int src = g.getNode(e.getSrc()).getKey();
		int dest = g.getNode(e.getDest()).getKey();
		if(type<0 && dest>src) {return false;}
		if(type>0 && src>dest) {return false;}
		return isOnEdge(p,src, dest, g);
	}
	public void init_Agents_by_game(String agents_info, directed_weighted_graph graph1) {
		try {
			JSONObject all_agents = new JSONObject(agents_info);
			JSONArray ags = all_agents.getJSONArray("Agents");
			for(int i=0;i<ags.length();i++) {
				CL_Agent c = new CL_Agent(graph1,0);
				c.update(ags.get(i).toString());
				agents_information.put(c.getID(),c);
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	public void get_Agents_update(String aa) {

		try {
			JSONObject all_agents = new JSONObject(aa);
			JSONArray ags = all_agents.getJSONArray("Agents");
			for(int i=0;i<ags.length();i++) {
				update_agent(i,ags.get(i).toString());
			}
			//= getJSONArray("Agents");
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	private void update_agent(int id_agent,String json) {
		JSONObject line;
		try {

			line = new JSONObject(json);
			JSONObject agents_full = line.getJSONObject("Agent");
			int id = agents_full.getInt("id");
			if(id==id_agent) {
				double speed = agents_full.getDouble("speed");
				String p = agents_full.getString("pos");
				int src = agents_full.getInt("src");
				int dest = agents_full.getInt("dest");
				double value = agents_full.getDouble("value");
				agents_information.get(id_agent).set_pos(p);
				agents_information.get(id_agent).setCurrNode(src);
				agents_information.get(id_agent).setSpeed(speed);
				agents_information.get(id_agent).setNextNode(dest);
				agents_information.get(id_agent).setMoney(value);
				if(!pokemon_contain(agents_information.get(id_agent)))
					get_Agents_info().get(id_agent).set_curr_fruit(null);
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}

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
			if(agn.get_curr_fruit()!=null) {
				if (agn.get_curr_fruit().equals(poke))
					return true;
			}
		}
		return false;
	}
	/**
	 * the function checks that the current pokemon is already in progress of searching
	 * return true if in progress anf false if not
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
	public boolean available_agents()
	{
		for (CL_Agent agn:this.get_Agents_info().values()) {
             if(agn.get_curr_fruit()==null && agn.getNextNode()==-1)
             	return true;
		}
		return false;
	}
	public int closest_agents(CL_Pokemon poki, dw_graph_algorithms algo)
	{ int small_id = -1;
	double small_path = Double.MAX_VALUE,temp_dis;
		for (CL_Agent agn:this.get_Agents_info().values()) {

			if(agn.get_curr_fruit()==null && agn.getNextNode()==-1)
			{
				System.out.println(agn.getSrcNode());
				System.out.println(poki);
				temp_dis = algo.shortestPathDist(agn.getSrcNode(),poki.get_edge().getSrc());
				if (temp_dis<=small_path)
				{small_path= temp_dis;
				     small_id = agn.getID();}
			}
		}
		return small_id;
	}

	public static ArrayList<CL_Pokemon> json2Pokemons_update(String fs,directed_weighted_graph graph1) {
		ArrayList<CL_Pokemon> ans = new ArrayList<>();
		try {
			JSONObject ttt = new JSONObject(fs);
			JSONArray ags = ttt.getJSONArray("Pokemons");
			for(int i=0;i<ags.length();i++) {
				JSONObject pp = ags.getJSONObject(i);
				JSONObject pk = pp.getJSONObject("Pokemon");
				int t = pk.getInt("type");
				double v = pk.getDouble("value");
				//double s = 0;//pk.getDouble("speed");
				String p = pk.getString("pos");

				CL_Pokemon f = new CL_Pokemon(new Point3D(p), t, v, 0, null);
				f.set_edge(correct_pokemon_edge(graph1,f));
				ans.add(f);
			}
		}
		catch (JSONException e) {e.printStackTrace();}
		return ans;
	}
	public CL_Pokemon closest_pokemon(CL_Agent agn, ArrayList <CL_Pokemon> Pokemons_list, dw_graph_algorithms algo)
	{ CL_Pokemon poki = null;
		double small_path = Double.MAX_VALUE,temp_dis;
		for (CL_Pokemon pokemon_temp:Pokemons_list) {
				temp_dis = algo.shortestPathDist(agn.getSrcNode(),pokemon_temp.get_edge().getSrc());
				if (temp_dis<=small_path)
				{small_path= temp_dis;
					poki = pokemon_temp;}
		}
		return poki;
	}
}

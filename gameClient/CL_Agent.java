package gameClient;

import api.directed_weighted_graph;
import api.edge_data;
import api.geo_location;
import api.node_data;
import gameClient.util.Point3D;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * this class represent the agents opreate in the game
 * this code is based on the code of boaz.benmoshe
 */
public class CL_Agent {
	public static final double EPS = 0.001;
	private static int _count = 0;
	private static int _seed = 3331;
	private edge_data _curr_edge;
	private directed_weighted_graph _gg;// graph that the agent operate on
	private node_data _curr_node;
	private int _id;
	private double _value;
	private CL_Pokemon _curr_fruit;// agent pokemon to search
	private geo_location _pos;
	private double _speed;
	private HashMap<Integer, Integer> point_arg; // contain the nodes in the agent path
	private int node_counter;// counter to the node int the agent path

	/**
	 * a constructor of the agent
	 * @param g
	 * @param start_node
	 */
	public CL_Agent(directed_weighted_graph g, int start_node) {
		this._gg = g;
		this._value = 0;
		this._curr_node = _gg.getNode(start_node);
		this._pos = _curr_node.getLocation();
		this._id = -1;
		this._speed = 0;
		point_arg = new HashMap<>();
		node_counter = 0;
		this._curr_edge= null;
	}
	/**
	 * update the agent by the json string the function gets
	 * @param json
	 */

	public void update(String json) {
		JSONObject line;
		try {
			line = new JSONObject(json);
			JSONObject agent_j = line.getJSONObject("Agent");
			int id = agent_j.getInt("id");
			if (id == this.getID() || this.getID() == -1) {
				if (this.getID() == -1) {
					_id = id;
				}
				double speed = agent_j.getDouble("speed");
				String p = agent_j.getString("pos");
				Point3D pp = new Point3D(p);
				int src = agent_j.getInt("src");
				int dest = agent_j.getInt("dest");
				double value = agent_j.getDouble("value");
				// sets the agent by the information from thr json
				this._pos = pp;
				this.setCurrNode(src);
				this.setSpeed(speed);
				this.setNextNode(dest);
				this.setMoney(value);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * return the agent source node
	 * @return
	 */
	public int getSrcNode() {
		return this._curr_node.getKey();
	}

	/**
	 * return a string of the agent information in a json form
	 * @return
	 */
	public String toJSON() {
		int d = this.getNextNode();
		String ans = "{\"Agent\":{"
				+ "\"id\":" + this._id + ","
				+ "\"value\":" + this._value + ","
				+ "\"src\":" + this._curr_node.getKey() + ","
				+ "\"dest\":" + d + ","
				+ "\"speed\":" + this.getSpeed() + ","
				+ "\"pos\":\"" + _pos.toString() + "\""
				+ "}"
				+ "}";
		return ans;
	}

	/**
	 * sets the agent value
	 * @param v
	 */
	public void setMoney(double v) {
		_value = v;
	}

	public boolean setNextNode(int dest) {
		boolean ans = false;
		int src = this._curr_node.getKey();
		this._curr_edge = _gg.getEdge(src, dest);
		if (_curr_edge != null) {
			ans = true;
		} else {
			_curr_edge = null;
		}
		return ans;
	}

	/**
	 * sets the agent current node
	 * @param src
	 */
	public void setCurrNode(int src) {
		this._curr_node = _gg.getNode(src);
	}

	public boolean isMoving() {
		return this._curr_edge != null;
	}

	/**
	 * return a type of json string
	 * @return
	 */
	public String toString() {
		return toJSON();
	}

	/**
	 * return a type of string to print
	 * @return
	 */
	public String toString1() {
		String ans = "" + this.getID() + "," + _pos + ", " + isMoving() + "," + this.getValue();
		return ans;
	}

	/**
	 * return the agents id
	 * @return
	 */
	public int getID() {
		return this._id;
	}

	/**
	 * return the agent loacation
	 * @return
	 */
	public geo_location getLocation() {
		return _pos;
	}

	/**
	 * return the agent value
	 * @return
	 */
	public double getValue() {
		return this._value;
	}

	/**
	 * return the next node in the agents path
	 * @return
	 */
	public int getNextNode() {
		int ans = -2;
		if (this._curr_edge == null) {
			ans = -1;
		} else {
			ans = this._curr_edge.getDest();
		}
		return ans;
	}

	/**
	 * return the agent speed
	 * @return
	 */
	public double getSpeed() {
		return this._speed;
	}

	/**
	 * sets the agent speed
	 * @param v
	 */
	public void setSpeed(double v) {
		this._speed = v;
	}

	/**
	 * return the current pokemon that the agent is in a search for
	 * @return
	 */
	public CL_Pokemon get_curr_fruit() {
		return _curr_fruit;
	}

	/**
	 * sets the pokemon that the agent is in a search for
	 * @param curr_fruit
	 */
	public void set_curr_fruit(CL_Pokemon curr_fruit) {
		this._curr_fruit = curr_fruit;
	}


	/**
	 * sets the agent current edge
	 * @return
	 */
	public edge_data get_curr_edge() {
		return this._curr_edge;
	}

	/**
	 * sets the position of the agent
	 * @param p
	 */
	public void set_pos(String p) {
		Point3D pp = new Point3D(p);
		this._pos = pp;
	}

	/**
	 * sets the path of nodes of the agent,by the list of nodes the function gets
	 * @param node_list
	 * @param dest
	 */
	public void setPoint_arg(List<node_data> node_list, int dest) {
		int i = 0;
		this.point_arg.clear();
		// go over the list a insert the nodes to the agent path collection
		Iterator<node_data> it = node_list.iterator();
		while (it.hasNext()) {
			node_data current_node = it.next();

			this.point_arg.put(i, current_node.getKey());
			i++;
		}
		this.point_arg.put(i, dest);
	}

	/**
	 * return a collection of the nodes in the agents path
	 * @return
	 */
	public HashMap<Integer, Integer> getPoint_arg() {
		return this.point_arg;
	}

	/**
	 * add 1 to the node conter
	 */
	public void add_node_count() {
		this.node_counter++;
	}

	/**
	 * return the node counter of the node path of the agent
	 * @return
	 */
	public int getNode_counter() {
		return this.node_counter;
	}

	/**
	 * set the node counter of the node path of agent
	 * @param node_count
	 */
	public void setNode_counter(int node_count) {
      this.node_counter = node_count;
	}
}
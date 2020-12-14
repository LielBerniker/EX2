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

public class CL_Agent {
	public static final double EPS = 0.0001;
	private static int _count = 0;
	private static int _seed = 3331;

	//	private long _key;
	private edge_data _curr_edge;
	private long _sg_dt;

	// my use
	private directed_weighted_graph _gg;
	private node_data _curr_node;
	private int _id;
	private double _value;
	private CL_Pokemon _curr_fruit;
	private geo_location _pos;
	private double _speed;
	private HashMap<Integer, Integer> point_arg;
	private int node_counter;


	public CL_Agent(directed_weighted_graph g, int start_node) {
		this._gg = g;
		this._value = 0;
		this._curr_node = _gg.getNode(start_node);
		this._pos = _curr_node.getLocation();
		this._id = -1;
		this._speed = 0;
		point_arg = new HashMap<>();
		node_counter = 0;
	}

	public void update(String json) {
		JSONObject line;
		try {
			// "GameServer":{"graph":"A0","pokemons":3,"agents":1}}
			line = new JSONObject(json);
			JSONObject ttt = line.getJSONObject("Agent");
			int id = ttt.getInt("id");
			if (id == this.getID() || this.getID() == -1) {
				if (this.getID() == -1) {
					_id = id;
				}
				double speed = ttt.getDouble("speed");
				String p = ttt.getString("pos");
				Point3D pp = new Point3D(p);
				int src = ttt.getInt("src");
				int dest = ttt.getInt("dest");
				double value = ttt.getDouble("value");
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

	//@Override
	public int getSrcNode() {
		return this._curr_node.getKey();
	}

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

	public void setCurrNode(int src) {
		this._curr_node = _gg.getNode(src);
	}

	public boolean isMoving() {
		return this._curr_edge != null;
	}

	public String toString() {
		return toJSON();
	}

	public String toString1() {
		String ans = "" + this.getID() + "," + _pos + ", " + isMoving() + "," + this.getValue();
		return ans;
	}

	public int getID() {
		// TODO Auto-generated method stub
		return this._id;
	}

	public geo_location getLocation() {
		// TODO Auto-generated method stub
		return _pos;
	}


	public double getValue() {
		// TODO Auto-generated method stub
		return this._value;
	}


	public int getNextNode() {
		int ans = -2;
		if (this._curr_edge == null) {
			ans = -1;
		} else {
			ans = this._curr_edge.getDest();
		}
		return ans;
	}

	public double getSpeed() {
		return this._speed;
	}

	public void setSpeed(double v) {
		this._speed = v;
	}

	public CL_Pokemon get_curr_fruit() {
		return _curr_fruit;
	}

	public void set_curr_fruit(CL_Pokemon curr_fruit) {
		this._curr_fruit = curr_fruit;
	}

	public void set_SDT(long ddtt) {
		long ddt = ddtt;
		if (this._curr_edge != null) {
			double w = get_curr_edge().getWeight();
			geo_location dest = _gg.getNode(get_curr_edge().getDest()).getLocation();
			geo_location src = _gg.getNode(get_curr_edge().getSrc()).getLocation();
			double de = src.distance(dest);
			double dist = _pos.distance(dest);
			if (this.get_curr_fruit().get_edge() == this.get_curr_edge()) {
				dist = _curr_fruit.getLocation().distance(this._pos);
			}
			double norm = dist / de;
			double dt = w * norm / this.getSpeed();
			ddt = (long) (1000.0 * dt);
		}
		this.set_sg_dt(ddt);
	}

	public edge_data get_curr_edge() {
		return this._curr_edge;
	}

	public long get_sg_dt() {
		return _sg_dt;
	}

	public void set_sg_dt(long _sg_dt) {
		this._sg_dt = _sg_dt;
	}

	// my area:
	//$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$44
	public void update_first(String json) {
		JSONObject line;
		try {
			line = new JSONObject(json);
			JSONObject agents = line.getJSONObject("Agent");
			int id = agents.getInt("id");
			if (id == this.getID() || this.getID() == -1) {
				if (this.getID() == -1) {
					_id = id;
				}
				double speed = agents.getDouble("speed");
				String p = agents.getString("pos");
				Point3D pp = new Point3D(p);
				int src = agents.getInt("src");
				int dest = agents.getInt("dest");
				double value = agents.getDouble("value");
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

	public void set_pos(String p) {
		Point3D pp = new Point3D(p);
		this._pos = pp;
	}

	public void setPoint_arg(List<node_data> node_list, int dest) {
		int i = 0;
		this.point_arg.clear();
		Iterator<node_data> it = node_list.iterator();
		while (it.hasNext()) {
			node_data current_node = it.next();

			this.point_arg.put(i, current_node.getKey());
			i++;
		}
		this.point_arg.put(i, dest);
	}

	public HashMap<Integer, Integer> getPoint_arg() {
		return this.point_arg;
	}

	public void add_node_count() {
		this.node_counter++;
	}

	public int getNode_counter() {
		return this.node_counter;
	}

	public void setNode_counter(int node_count) {
      this.node_counter = node_count;
	}
}
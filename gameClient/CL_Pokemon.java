package gameClient;
import api.DWGraph_Algo;
import api.edge_data;
import gameClient.util.Point3D;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

/**
 * this class represent the pokemons tin the game
 * this code base on the code of boaz.benmoshe
 */
public class CL_Pokemon implements Comparable {

	private edge_data _edge;
	private double _value;
	private int _type;// type of pokemon , refer to the type of edge the pokemon located on
	private Point3D _pos;// location of the pokemon

	/**
	 * constructor of the pokemon
	 * @param p
	 * @param t
	 * @param v
	 * @param e
	 */
	public CL_Pokemon(Point3D p, int t, double v, edge_data e) {
		_type = t;
		_value = v;
		set_edge(e);
		_pos = p;
	}

	/**
	 * return a string of the pokemon information
	 * @return
	 */
	public String toString() {return "F:{v="+_value+", t="+_type+"}";}

	/**
	 * return the pokemon current edge
	 * @return
	 */
	public edge_data get_edge() {
		return _edge;
	}

	/**
	 * set the pokemon edge
	 * @param _edge
	 */
	public void set_edge(edge_data _edge) {
		this._edge = _edge;
	}

	/**
	 * return the pokemon location
	 * @return
	 */
	public Point3D getLocation() {
		return _pos;
	}

	/**
	 * return the pokemon type
	 * @return
	 */
	public int getType() {return _type;}

	/**
	 * return the pokemon value
	 * @return
	 */
	public double getValue() {return _value;}


	/**
	 * a compare to function
	 * if this object is bigger than o object in value return -1
	 * if smaller in value return 1
	 * else return 0
	 * @param o
	 * @return
	 */
	@Override
	public int compareTo(@NotNull Object o) {
		CL_Pokemon cop_pokemon = (CL_Pokemon) o;
		if(this.getValue()<cop_pokemon.getValue())
			return 1;
		else if(this.getValue()>cop_pokemon.getValue())
			return -1;
		else
			return 0;

	}

	/**
	 * an equals  function
	 * if this object is not fully equal to o object return false
	 * or not from pokemon object type
	 * else return true
	 * @param o
	 * @return
	 */
	@Override
	public boolean equals( Object o) {
		// if o is from a different object
		if(!(o instanceof CL_Pokemon))
			return false;
		CL_Pokemon cop_pokemon = (CL_Pokemon) o;
		if(this.getValue()!=((CL_Pokemon) o).getValue())
			return false;
		if(this.getLocation().x()!=cop_pokemon.getLocation().x() || this.getLocation().y()!=cop_pokemon.getLocation().y() || this.getLocation().z()!=cop_pokemon.getLocation().z())
			return false;
		if (this.getType()!=((CL_Pokemon) o).getType())
			return false;
		return true;

	}

}

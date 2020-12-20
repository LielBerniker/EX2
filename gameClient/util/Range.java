package gameClient.util;
/**
 * This class represents a simple 1D range of shape [min,max]
 * base on the code of boaz_benmoshe
 *
 */
public class Range {
	private double _min, _max;

	/**
	 * constructor to the range by min and mux
	 * @param min
	 * @param max
	 */
	public Range(double min, double max) {
		set_min(min);
		set_max(max);
	}

	/**
	 * copy constructor to the range
	 * @param x
	 */
	public Range(Range x) {
		this(x._min, x._max);
	}

	/**
	 * return  a string that contain an information about the range
	 * @return
	 */
	public String toString() {
		String ans = "["+this.get_min()+","+this.get_max()+"]";
		if(this.isEmpty()) {ans = "Empty Range";}
		return ans;
	}

	/**
	 * return true if the min is bigger the max
	 * @return
	 */
	public boolean isEmpty() {
		return this.get_min()>this.get_max();
	}

	/**
	 * return the max
	 * @return
	 */
	public double get_max() {
		return _max;
	}

	/**
	 * return the length
	 * @return
	 */
	public double get_length() {
		return _max-_min;
	}

	/**
	 * set the max of the range
	 * @param _max
	 */
	private void set_max(double _max) {
		this._max = _max;
	}

	/**
	 * return the min of range
	 * @return
	 */
	public double get_min() {
		return _min;
	}

	/**
	 * set the min of the range
	 * @param _min
	 */
	private void set_min(double _min) {
		this._min = _min;
	}

	/**
	 * return the portion by the min
	 * @param d
	 * @return
	 */
	public double getPortion(double d) {
		double d1 = d-_min;
		double ans = d1/get_length();
		return ans;
	}

	/**
	 * return portion by the length and min
	 * @param p
	 * @return
	 */
	public double fromPortion(double p) {
		return _min+p* get_length();
	}
}

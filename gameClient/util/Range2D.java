package gameClient.util;
import api.geo_location;

/**
 * This class represents a 2D Range, composed from two 1D Ranges.
 * base on the code of boaz.benmoshe
 */

public class Range2D {
	private Range _y_range;
	private Range _x_range;
	/**
	 * constructor to the Range2D by to ranges
	 */
	public Range2D(Range x, Range y) {
		_x_range = new Range(x);
		_y_range = new Range(y);
	}

	/**
	 * copy constructor to the Range2D
	 * @param w
	 */
	public Range2D(Range2D w) {
		_x_range = new Range(w._x_range);
		_y_range = new Range(w._y_range);
	}

	/**
	 * return a portion as a 3d point by the location and get portion range
	 * @param p
	 * @return
	 */
	public Point3D getPortion(geo_location p) {
		double x = _x_range.getPortion(p.x());
		double y = _y_range.getPortion(p.y());
		return new Point3D(x,y,0);
	}
	/**
	 * return a portion as a 3d point by the location and from portion range
	 * @param p
	 * @return
	 */
	public Point3D fromPortion(geo_location p) {
		double x = _x_range.fromPortion(p.x());
		double y = _y_range.fromPortion(p.y());
		return new Point3D(x,y,0);
	}	
}

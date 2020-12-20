package gameClient.util;

import api.geo_location;
import gameClient.util.Point3D;
import gameClient.util.Range2D;

/**
 * This class represents a simple world 2 frame conversion (both ways).
 *based on the code of  boaz.benmoshe
 *
 */

public class Range2Range {
	private Range2D _world, _frame;

	/**
	 * constructor to the Range2Range by 2 Range2D
	 * @param w
	 * @param f
	 */
	public Range2Range(Range2D w, Range2D f) {
		_world = new Range2D(w);
		_frame = new Range2D(f);
	}

	/**
	 * get the location compare to the world at
	 * @param p
	 * @return
	 */
	public geo_location world2frame(geo_location p) {
		Point3D d = _world.getPortion(p);
		Point3D ans = _frame.fromPortion(d);
		return ans;
	}
	public geo_location frame2world(geo_location p) {
		Point3D d = _frame.getPortion(p);
		Point3D ans = _world.fromPortion(d);
		return ans;
	}

	/**
	 * return the world
	 * @return
	 */
	public Range2D getWorld() {
		return _world;
	}

	/**
	 * return the frame
	 * @return
	 */
	public Range2D getFrame() {
		return _frame;
	}
}

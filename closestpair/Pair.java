package alda.closestpair;

import java.awt.Point;

/**
 * Denna klass representerar ett par med punkter.
 * 
 * @author Johan Ekh, joek7107
 * @author Alexanderi Malmgren, alma1060
 *
 */
public class Pair {
	private Point point1;
	private Point point2;
	private double distance;
	
	public Pair(Point point1, Point point2) {
		this.point1 = point1;
		this.point2 = point2;
		this.distance = Math.hypot(point2.x - point1.x, point2.y - point1.y);
	}
	
	public double getDistance() {
		return distance;
	}

	public String toString() {
		return 	"x=" + point1.x + ", y=" + point1.y + "\t" + 
				"x=" + point2.x + ", y=" + point2.y + "\t" + distance;
	}
}
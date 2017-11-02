package alda.closestpair;

import static org.junit.Assert.*;
import java.awt.Point;
import java.util.*;

import org.junit.*;
public class Tester {
	private ArrayList<Point> points = new ArrayList<>();

	public void addPoint(Point point) {
		points.add(point);
	}


	@Test
		public void testFindClosestPair() 
		{
			ClosestPair cp = new ClosestPair();
			addPoint(new Point(2,6));
			addPoint(new Point(3,7));
			addPoint(new Point(4,5));
			addPoint(new Point(4,3));
			addPoint(new Point(5,6));
			addPoint(new Point(7,6));
			addPoint(new Point(8,3));
			addPoint(new Point(9,6));
			addPoint(new Point(11,8));
			addPoint(new Point(12,3));
			
//			System.out.println(cp.divideAndConquer(points));
//			System.out.println(cp.bruteForce(points));
//			cp.getClosestPair(points);
//			cp.bruteForce(points);
			assertEquals(cp.getClosestPair(points).getDistance(), cp.bruteForce(points).getDistance(), 0.00);
		}

	/**
	 *   Testfall: (1,10) (100,10) (90, 300), (15, 700) , (55, 2000), (42, 9000)
	 *   två koordinater som är långt ifrån varandra på x-axeln men ändå är närmast
	 */
	@Test
		public void testClosestPairFirstAndLast() 
		{
			points.clear();
			ClosestPair cp = new ClosestPair();
			addPoint(new Point(12,40));
			addPoint(new Point(1,4));
			addPoint(new Point(52,7));
			addPoint(new Point(245,6754));
			addPoint(new Point(12245,25));
			addPoint(new Point(114,432));
			addPoint(new Point(746,2));
			addPoint(new Point(452,54));
			addPoint(new Point(1325,625));
			addPoint(new Point(7876,96));
			addPoint(new Point(12,22));
			addPoint(new Point(245,765));
			addPoint(new Point(13,657));
			addPoint(new Point(345,63));
//			System.out.println(cp.divideAndConquer(points));
//			assertEquals(cp.findClosestInInterval(0,cp.getPlaneSize()-1), cp.findClosestPair(), 0.00);
			assertEquals(cp.getClosestPair(points).getDistance(), cp.bruteForce(points).getDistance(), 0.00);
		}
	
	@Test
		public void randomTest100()
		{
			ClosestPair cp = new ClosestPair();
			Random r = new Random();

			for(int i = 0; i<100; i++)
				addPoint(new Point(r.nextInt(1000), r.nextInt(1000)));
			assertEquals(cp.getClosestPair(points).getDistance(), cp.bruteForce(points).getDistance(), 0.00);
//			assertEquals(cp.findClosestInInterval(0,cp.getPlaneSize()-1), cp.findClosestPair(), 0.00);
		}

//	@Test
		public void randomTestr5000()
		{
			ClosestPair cp = new ClosestPair();
			Random r = new Random();

			for(int i = 0; i<5000; i++)
				addPoint(new Point(r.nextInt(5000), r.nextInt(5000)));
//			cp.getClosestPair(points);
			assertEquals(cp.getClosestPair(points).getDistance(), cp.bruteForce(points).getDistance(), 0.00);
//			assertEquals(cp.findClosestInInterval(0,cp.getPlaneSize()-1), cp.findClosestPair(), 0.00);
		}

//	@Test
		public void randomTestr10000()
		{
			ClosestPair cp = new ClosestPair();
			Random r = new Random();

			for(int i = 0; i<10000; i++)
				addPoint(new Point(r.nextInt(10000), r.nextInt(10000)));
			assertEquals(cp.getClosestPair(points).getDistance(), cp.bruteForce(points).getDistance(), 0.00);
//			assertEquals(cp.findClosestInInterval(0,cp.getPlaneSize()-1), cp.findClosestPair(), 0.00);
		}
}
package alda.closestpair;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * 
 * @author Johan Ekh, joek7107
 * @author Alexanderi Malmgren, alma1060
 *
 */
public class ClosestPair {
	private class ComparatorX implements Comparator<Point> {
		public int compare(Point point1, Point point2) {
			return point1.x - point2.x;
		}
	}

	private class ComparatorY implements Comparator<Point> {
		public int compare(Point point1, Point point2) {
			return point1.y - point2.y;
		}
	}

	public Pair getClosestPair(List<Point> points) {
		List<Point> pointsByXValue = new ArrayList<>(points);
		List<Point> pointsByYValue = new ArrayList<>(points);

		Collections.sort(pointsByXValue, new ComparatorX());
		Collections.sort(pointsByYValue, new ComparatorY());

		return getClosestPair(pointsByXValue, pointsByYValue);
	}

	private Pair getClosestPair(List<Point> pointsByXValue, List<Point> pointsByYValue) {
		int numberOfPoints = pointsByXValue.size();
		if (numberOfPoints <= 3) {
			return bruteForce(pointsByXValue);
		}

		Pair closestPair;
		List<Point> tempList;
		int middleIndex = numberOfPoints / 2;
		List<Point> leftSide = pointsByXValue.subList(0, middleIndex);
		List<Point> rightSide = pointsByXValue.subList(middleIndex, numberOfPoints);

		tempList = new ArrayList<>(leftSide);
		Collections.sort(tempList, new ComparatorY());
		Pair closestPairLeft = getClosestPair(leftSide, tempList);

		tempList = new ArrayList<>(rightSide);
		Collections.sort(tempList, new ComparatorY());
		Pair closestPairRight = getClosestPair(rightSide, tempList);

		if (closestPairLeft.getDistance() < closestPairRight.getDistance()) {
			closestPair = closestPairLeft;
		} else {
			closestPair = closestPairRight;
		}

		ArrayList<Point> strip = new ArrayList<>();
		double shortestDistance = closestPair.getDistance();
		double middleX = rightSide.get(0).x;

		for (Point point : pointsByYValue) {
			if (Math.abs(middleX - point.x) < shortestDistance) {
				strip.add(point);
			}
		}

		for (int i = 0; i < strip.size() - 1; i++) {
			Point point1 = strip.get(i);
			for (int j = i + 1; j < strip.size(); j++) {
				Point point2 = strip.get(j);
				if ((point2.y - point1.y) < shortestDistance) {
					Pair pair = new Pair(point1, point2);
					if (pair.getDistance() < closestPair.getDistance()) {
						closestPair = pair;
						shortestDistance = closestPair.getDistance();
					}
				}
			}
		}
		return closestPair;
	}

	// publik för att kunna användas i tester
	public Pair bruteForce(List<Point> points) {
		int numberOfPoints = points.size();
		// om den ursprungliga listan inte innehåller
		// mer än en punkt finns inget par
		if (numberOfPoints < 2) {
			return null;
		}

		Pair closestPair = new Pair(points.get(0), points.get(1));

		if (numberOfPoints == 2) {
			return closestPair;
		}

		for (int i = 0; i < numberOfPoints - 1; i++) {
			Point point1 = points.get(i);
			for (int j = i + 1; j < numberOfPoints; j++) {
				Point point2 = points.get(j);
				Pair pair = new Pair(point1, point2);
				if (pair.getDistance() < closestPair.getDistance()) {
					closestPair = pair;
				}
			}
		}
		return closestPair;
	}
}
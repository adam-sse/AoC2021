package day05;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LineOverlapping {

	private static class Point {
		int x;
		int y;
		Point(int x, int y) {
			this.x = x;
			this.y = y;
		}
		@Override
		public int hashCode() {
			return Objects.hash(x, y);
		}
		@Override
		public boolean equals(Object obj) {
			if (this == obj) {
				return true;
			}
			if (!(obj instanceof Point)) {
				return false;
			}
			Point other = (Point) obj;
			return x == other.x && y == other.y;
		}
		@Override
		public String toString() {
			return "(" + x + "," + y + ")";
		}
	}
	
	private static void part1(int x1, int y1, int x2, int y2, Map<Point, Integer> lineCoverage) {
		if (x1 == x2) {
			for (int y = Math.min(y1, y2); y <= Math.max(y1, y2); y++) {
				Point p = new Point(x1, y);
				lineCoverage.put(p, lineCoverage.getOrDefault(p, 0) + 1);
			}
		} else if (y1 == y2) {
			for (int x = Math.min(x1, x2); x <= Math.max(x1, x2); x++) {
				Point p = new Point(x, y1);
				lineCoverage.put(p, lineCoverage.getOrDefault(p, 0) + 1);
			}
		}
	}
	
	private static void part2(int x1, int y1, int x2, int y2, Map<Point, Integer> lineCoverage) {
		if (x1 == x2) {
			for (int y = Math.min(y1, y2); y <= Math.max(y1, y2); y++) {
				Point p = new Point(x1, y);
				lineCoverage.put(p, lineCoverage.getOrDefault(p, 0) + 1);
			}
		} else if (y1 == y2) {
			for (int x = Math.min(x1, x2); x <= Math.max(x1, x2); x++) {
				Point p = new Point(x, y1);
				lineCoverage.put(p, lineCoverage.getOrDefault(p, 0) + 1);
			}
		} else {
			int dx = x1 < x2 ? +1 : -1;
			int dy = y1 < y2 ? +1 : -1;
			for (int i = 0; i <= Math.abs(x1 - x2); i++) {
				Point p = new Point(x1 + i * dx, y1 + i * dy);
				lineCoverage.put(p, lineCoverage.getOrDefault(p, 0) + 1);
			}
		}
	}
	
	public static void main(String[] args) throws IOException {
		Path input = Path.of("src/day05/input.txt");
		
		Map<Point, Integer> lineCoverage1 = new HashMap<>(10000);
		Map<Point, Integer> lineCoverage2 = new HashMap<>(10000);
		
		Pattern pattern = Pattern.compile("(?<x1>[0-9]+),(?<y1>[0-9]+) -> (?<x2>[0-9]+),(?<y2>[0-9]+)");
		
		Files.lines(input).forEach(line -> {
			Matcher m = pattern.matcher(line);
			m.matches();
			int x1 = Integer.parseInt(m.group("x1"));
			int y1 = Integer.parseInt(m.group("y1"));
			int x2 = Integer.parseInt(m.group("x2"));
			int y2 = Integer.parseInt(m.group("y2"));
			
			part1(x1, y1, x2, y2, lineCoverage1);
			part2(x1, y1, x2, y2, lineCoverage2);
		});
		
		System.out.println(lineCoverage1.values().stream().filter(i -> i >= 2).count());
		System.out.println(lineCoverage2.values().stream().filter(i -> i >= 2).count());
	}
	
}

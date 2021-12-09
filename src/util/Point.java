package util;

import java.util.Objects;

public class Point {
    
	public final int x;
	
	public final int y;
	
	public Point(int x, int y) {
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
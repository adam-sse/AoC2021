package day19;

import java.util.Objects;

public class V3 {

	public static final V3 ZERO = new V3(0, 0 ,0);
	
	public final int x;
	
	public final int y;
	
	public final int z;

	public V3(int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public static V3 parse(String line) {
		String[] parts = line.split(",");
		return new V3(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]), Integer.parseInt(parts[2]));
	}
	
	public V3 add(V3 other) {
		return new V3(this.x + other.x, this.y + other.y, this.z + other.z);
	}
	
	public V3 sub(V3 other) {
		return new V3(this.x - other.x, this.y - other.y, this.z - other.z);
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(x, y, z);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof V3)) {
			return false;
		}
		V3 other = (V3) obj;
		return x == other.x && y == other.y && z == other.z;
	}

	@Override
	public String toString() {
		return "(" + x + "," + y + "," + z + ")";
	}

}

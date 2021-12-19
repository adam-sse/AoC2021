package day19;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class BeaconMap {

	private static Set<V3> SCANNER_POSITIONS = new HashSet<>();
	
	public static int countOverlap(Set<V3> s1, Set<V3> s2) {
		return (int) s1.stream().filter(s2::contains).count();
	}
	
	private static boolean findOverlap(Set<V3> sensor1, Set<V3> sensor2, Map<Integer, Set<V3>> translations) {

		for (V3 s1First : sensor1) {
			for (V3 s2First : sensor2) {
				V3 translation = s1First.sub(s2First);
				
				Set<V3> s2Translated = sensor2.stream()
						.map(v -> translation.add(v))
						.collect(Collectors.toSet());
				
				int overlap = countOverlap(sensor1, s2Translated);
				
				if (overlap >= 12) {
					SCANNER_POSITIONS.add(translation);
					sensor1.addAll(s2Translated);
					return true;
				}
			}
		}
		
		return false;
	}
	
	public static boolean merge(Set<V3> sensor1, Set<V3> sensor2) {
		
		Map<Integer, Set<V3>> translations = new HashMap<>();
		
		if (findOverlap(sensor1, sensor2, translations)) {
			return true;
		}
		
		for (int rx = -1; rx <= 1; rx += 2) {
			for (int ry = -1; ry <= 1; ry += 2) {
				for (int rz = -1; rz <= 1; rz += 2) {
					int xx = rx;
					int yy = ry;
					int zz = rz;
					
					if (findOverlap(sensor1, sensor2.stream()
							.map(v -> new V3(v.x * xx, v.y * yy, v.z * zz))
							.collect(Collectors.toSet()), translations)) {
						return true;
					}
					if (findOverlap(sensor1, sensor2.stream()
							.map(v -> new V3(v.x * xx, v.z * zz, v.y * yy))
							.collect(Collectors.toSet()), translations)) {
						return true;
					}
					
					if (findOverlap(sensor1, sensor2.stream()
							.map(v -> new V3(v.y * yy, v.z * zz, v.x * xx))
							.collect(Collectors.toSet()), translations)) {
						return true;
					}
					if (findOverlap(sensor1, sensor2.stream()
							.map(v -> new V3(v.y * yy, v.x * xx, v.z * zz))
							.collect(Collectors.toSet()), translations)) {
						return true;
					}
					
					if (findOverlap(sensor1, sensor2.stream()
							.map(v -> new V3(v.z * zz, v.x * xx, v.y * yy))
							.collect(Collectors.toSet()), translations)) {
						return true;
					}
					if (findOverlap(sensor1, sensor2.stream()
							.map(v -> new V3(v.z * zz, v.y * yy, v.x * xx))
							.collect(Collectors.toSet()), translations)) {
						return true;
					}
				}
			}
		}
		
		return false;
	}

	private static boolean containsFalse(boolean[] done) {
		for (boolean b : done) {
			if (!b) {
				return false;
			}
		}
		return true;
	}
	
	public static void main(String[] args) throws IOException {
		Path input = Path.of("src/day19/input.txt");
		
		List<Set<V3>> scanners = new ArrayList<>();
		Set<V3> currentScanner = null;
		for (String line : Files.readAllLines(input)) {
			if (line.startsWith("---"))	 {
				currentScanner = new HashSet<>();
				scanners.add(currentScanner);
			} else if (line.isEmpty()) {
				currentScanner = null;
			} else {
				currentScanner.add(V3.parse(line));
			}
		}
		
		boolean[] done = new boolean[scanners.size()];
		Set<V3> result = scanners.get(0);
		done[0] = true;
		while (!containsFalse(done)) {
			for (int i = 0; i < scanners.size(); i++) {
				if (done[i]) continue;
				
				Set<V3> scanner = scanners.get(i);
				if (merge(result, scanner)) {
					done[i] = true;
				}
			}
		}
		
		System.out.println(result.size());
		
		int maxDist = 0;
		for (V3 s1 : SCANNER_POSITIONS) {
			for (V3 s2 : SCANNER_POSITIONS) {
				maxDist = Math.max(maxDist, Math.abs(s1.x - s2.x) + Math.abs(s1.y - s2.y) + Math.abs(s1.z - s2.z));
			}
		}
		System.out.println(maxDist);
	}

}

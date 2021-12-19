package day19;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

public class BeaconMap {

	private static Set<V3> SCANNER_POSITIONS = new HashSet<>();
	
	public static int countOverlap(Set<V3> s1, Set<V3> s2) {
		return (int) s1.stream().filter(s2::contains).count();
	}
	
	private static boolean findOverlap(Set<V3> sensor1, Set<V3> sensor2, UnaryOperator<V3> rotation) {

		for (V3 s1First : sensor1) {
			for (V3 s2First : sensor2) {
				V3 translation = s1First.sub(rotation.apply(s2First));
				
				Set<V3> s2Translated = sensor2.stream()
						.map(v -> translation.add(rotation.apply(v)))
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
		
		if (findOverlap(sensor1, sensor2, v -> new V3(v.x, v.y, v.z))) return true;
		if (findOverlap(sensor1, sensor2, v -> new V3(v.x, v.z, -v.y))) return true;
		if (findOverlap(sensor1, sensor2, v -> new V3(v.x, -v.y, -v.z))) return true;
		if (findOverlap(sensor1, sensor2, v -> new V3(v.x, -v.z, v.y))) return true;
		
		if (findOverlap(sensor1, sensor2, v -> new V3(-v.x, v.y, -v.z))) return true;
		if (findOverlap(sensor1, sensor2, v -> new V3(-v.x, v.z, v.y))) return true;
		if (findOverlap(sensor1, sensor2, v -> new V3(-v.x, -v.y, v.z))) return true;
		if (findOverlap(sensor1, sensor2, v -> new V3(-v.x, -v.z, -v.y))) return true;
		
		if (findOverlap(sensor1, sensor2, v -> new V3(v.y, -v.x, v.z))) return true;
		if (findOverlap(sensor1, sensor2, v -> new V3(v.y, -v.z, -v.x))) return true;
		if (findOverlap(sensor1, sensor2, v -> new V3(v.y, v.x, -v.z))) return true;
		if (findOverlap(sensor1, sensor2, v -> new V3(v.y, v.z, v.x))) return true;
		
		if (findOverlap(sensor1, sensor2, v -> new V3(-v.y, v.x, v.z))) return true;
		if (findOverlap(sensor1, sensor2, v -> new V3(-v.y, v.z, -v.x))) return true;
		if (findOverlap(sensor1, sensor2, v -> new V3(-v.y, -v.x, -v.z))) return true;
		if (findOverlap(sensor1, sensor2, v -> new V3(-v.y, -v.z, v.x))) return true;
		
		if (findOverlap(sensor1, sensor2, v -> new V3(v.z, v.y, -v.x))) return true;
		if (findOverlap(sensor1, sensor2, v -> new V3(v.z, v.x, v.y))) return true;
		if (findOverlap(sensor1, sensor2, v -> new V3(v.z, -v.y, v.x))) return true;
		if (findOverlap(sensor1, sensor2, v -> new V3(v.z, -v.x, -v.y))) return true;
		
		if (findOverlap(sensor1, sensor2, v -> new V3(-v.z, v.y, v.x))) return true;
		if (findOverlap(sensor1, sensor2, v -> new V3(-v.z, v.x, -v.y))) return true;
		if (findOverlap(sensor1, sensor2, v -> new V3(-v.z, -v.y, -v.x))) return true;
		if (findOverlap(sensor1, sensor2, v -> new V3(-v.z, -v.x, v.y))) return true;
		
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
		long t0 = System.currentTimeMillis();
		while (!containsFalse(done)) {
			for (int i = 0; i < scanners.size(); i++) {
				if (done[i]) continue;
				
				Set<V3> scanner = scanners.get(i);
				if (merge(result, scanner)) {
					done[i] = true;
				}
			}
		}
		long t1 = System.currentTimeMillis();
		System.out.println((t1 - t0) + " ms");
		
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

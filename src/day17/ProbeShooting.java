package day17;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ProbeShooting {

	private static class Area {
		final int xmin;
		final int xmas; // hihi
		final int ymin;
		final int ymax;
		Area(int xmin, int xmas, int ymin, int ymax) {
			this.xmin = xmin;
			this.xmas = xmas;
			this.ymin = ymin;
			this.ymax = ymax;
		}
		boolean contains(int x, int y) {
			return x >= xmin && x <= xmas && y >= ymin && y <= ymax;
		}
	}
	
	private static class HitResult {
		final boolean hits;
		final int maxy;
		HitResult(boolean hits, int maxy) {
			this.hits = hits;
			this.maxy = maxy;
		}
	}
	
	private static HitResult simulateShot(int vx, int vy, Area target) {
		int x = 0;
		int y = 0;
		int maxy = 0;
		
		while (y >= target.ymin) {
			x += vx;
			y += vy;
			
			maxy = Math.max(maxy, y);
			
			if (vx != 0) {
				vx = vx + (vx < 0 ? 1 : -1);
			}
			vy -= 1;
			
			if (target.contains(x, y)) {
				return new HitResult(true, maxy);
			}
		}
		
		return new HitResult(false, maxy);
	}
	
	private static int calcVxMin(Area target) {
		int vxMin = 0;
		boolean hits;
		do {
			vxMin++;
			
			int x = 0;
			int vx = vxMin;
			while (vx > 0) {
				x += vx;
				vx--;
			}
			
			hits = x >= target.xmin;
			
		} while (!hits);
		
		return vxMin;
	}

	private static final int MAX_Y = 500; // trial & error...
	
	private static int part1(Area target) {
		int vxMin = calcVxMin(target);
		int vxMax = target.xmas;
		
		int maxY = 0;
		
		for (int vx = vxMin; vx <= vxMax; vx++) {
			for (int vy = 0; vy <= MAX_Y; vy++) {
				HitResult hr = simulateShot(vxMin, vy, target);
				if (hr.hits) {
					maxY = Math.max(vx, hr.maxy);
				}
			}
		}
		
		return maxY;
	}
	
	private static int part2(Area target) {
		int vxMin = calcVxMin(target);
		int vxMax = target.xmas;
		
		int numHits = 0;
		
		for (int vx = vxMin; vx <= vxMax; vx++) {
			for (int vy = target.ymin; vy <= MAX_Y; vy++) {
				HitResult hr = simulateShot(vx, vy, target);
				if (hr.hits) {
					numHits++;
				}
			}
		}
		
		return numHits;
	}
	
	public static void main(String[] args) throws IOException {
		Path input = Path.of("src/day17/input.txt");
		
		String line = Files.readAllLines(input).get(0);
		Pattern regex = Pattern.compile("^target area: x=(?<xmin>-?[0-9]+)\\.\\.(?<xmas>-?[0-9]+), y=(?<ymin>-?[0-9]+)\\.\\.(?<ymax>-?[0-9]+)$");
		Matcher m = regex.matcher(line);
		m.matches();
		
		Area target = new Area(
				Integer.parseInt(m.group("xmin")),
				Integer.parseInt(m.group("xmas")),
				Integer.parseInt(m.group("ymin")),
				Integer.parseInt(m.group("ymax")));
		
		System.out.println(part1(target));
		System.out.println(part2(target));
	}
	
}

package day22;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class ReactorCubes {

	private static class Step {
		final boolean state;
		final Region region;
		Step(boolean state, Region region) {
			this.state = state;
			this.region = region;
		}
	}
	
	private static int part1(List<Step> steps) {
		boolean[][][] initArea = new boolean[101][101][101];
		int numOn = 0;
		
		for (Step step : steps) {
			
			for (int x = Math.max(-50, step.region.minx); x <= Math.min(50, step.region.maxx); x++) {
				for (int y = Math.max(-50, step.region.miny); y <= Math.min(50, step.region.maxy); y++) {
					for (int z = Math.max(-50, step.region.minz); z <= Math.min(50, step.region.maxz); z++) {
						if (step.state && !initArea[x + 50][y + 50][z + 50]) {
							numOn++;
						} else if (!step.state && initArea[x + 50][y + 50][z + 50]) {
							numOn--;
						}
						initArea[x + 50][y + 50][z + 50] = step.state;
					}
				}
			}
			
		}
		
		return numOn;
	}
	
	private static class Region {
		final int minx;
		final int maxx;
		final int miny;
		final int maxy;
		final int minz;
		final int maxz;
		Region(int minx, int maxx, int miny, int maxy, int minz, int maxz) {
			this.minx = minx;
			this.maxx = maxx;
			this.miny = miny;
			this.maxy = maxy;
			this.minz = minz;
			this.maxz = maxz;
		}
		boolean intersects(Region other) {
			boolean xIntersects = this.maxx >= other.minx && this.minx <= other.maxx; 
			boolean yIntersects = this.maxy >= other.miny && this.miny <= other.maxy;
			boolean zIntersects = this.maxz >= other.minz && this.minz <= other.maxz; 
			return xIntersects && yIntersects && zIntersects; 
		}
		Region intersection(Region other)  {
			int minx = Math.max(this.minx, other.minx);
			int maxx = Math.min(this.maxx, other.maxx);
			
			int miny = Math.max(this.miny, other.miny);
			int maxy = Math.min(this.maxy, other.maxy);
			
			int minz = Math.max(this.minz, other.minz);
			int maxz = Math.min(this.maxz, other.maxz);
			
			return new Region(minx, maxx, miny, maxy, minz, maxz);
		}
		long size() {
			return (long) (maxx - minx + 1) * (long) (maxy - miny + 1) * (long) (maxz - minz + 1);
		}
		@Override
		public String toString() {
			return "Region [minx=" + minx + ", maxx=" + maxx + ", miny=" + miny + ", maxy=" + maxy + ", minz=" + minz
					+ ", maxz=" + maxz + "]";
		}
	}
	
	private static class MutableRegion extends Region {
		List<MutableRegion> removed;
		MutableRegion(Region other) {
			super(other.minx, other.maxx, other.miny, other.maxy, other.minz, other.maxz);
			removed = new LinkedList<>();
		}
		void remove(Region remove) {
			MutableRegion toRemove = new MutableRegion(this.intersection(remove));
			
			for (Region removed : removed) {
				if (toRemove.intersects(removed)) {
					toRemove.remove(removed);
				}
			}

			if (toRemove.size() > 0) {
				removed.add(toRemove);
			}
		}
		@Override
		long size() {
			return super.size() - removed.stream().mapToLong(MutableRegion::size).sum();
		}
	}
	
	private static long part2(List<Step> steps) {
		List<MutableRegion> on = new ArrayList<>(steps.size());
		
		for (Step step : steps) {
			MutableRegion region = new MutableRegion(step.region);
			
			on.stream()
				.filter(r -> r.intersects(step.region))
				.forEach(other -> other.remove(region));
			
			if (step.state) {
				on.add(region);
			}
		}
		
		return on.stream().mapToLong(MutableRegion::size).sum();
	}
	
	public static void main(String[] args) throws IOException {
		Path input = Path.of("src/day22/input.txt");
		
		Pattern p = Pattern.compile("(?<state>on|off) x=(?<xmin>-?[0-9]+)\\.\\.(?<xmax>-?[0-9]+),y=(?<ymin>-?[0-9]+)\\.\\.(?<ymax>-?[0-9]+),z=(?<zmin>-?[0-9]+)\\.\\.(?<zmax>-?[0-9]+)");
		List<Step> steps = Files.lines(input)
				.map(line -> p.matcher(line))
				.peek(m -> m.matches())
				.map(m -> new Step(m.group("state").equals("on"), new Region(
						Integer.parseInt(m.group("xmin")), Integer.parseInt(m.group("xmax")),
						Integer.parseInt(m.group("ymin")), Integer.parseInt(m.group("ymax")),
						Integer.parseInt(m.group("zmin")), Integer.parseInt(m.group("zmax")))))
				.collect(Collectors.toList());
		
		System.out.println(part1(steps));
		System.out.println(part2(steps));
	}
	
}

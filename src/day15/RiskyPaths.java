package day15;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.ToIntFunction;

import util.Point;

public class RiskyPaths {

	private static int part1(byte[][] graph) {
		Point target = new Point(graph.length - 1, graph[0].length - 1);
		
		Set<Point> openSet = new HashSet<>();
		openSet.add(new Point(0, 0));
		
		int[][] dist = new int[graph.length][graph[0].length];
		for (int i = 0; i < dist.length; i++) {
			for (int j = 0; j < dist[i].length; j++) {
				dist[i][j] = Integer.MAX_VALUE;
			}
		}
		
		dist[0][0] = 0;
		
		ToIntFunction<Point> nextFunction = point -> {
			int g = dist[point.x][point.y];
			
			int dx = target.x - point.x;
			int dy = target.y - point.y;
			
			int h = (int) Math.sqrt(dx * dx + dy * dy);
			return g + h;
		};
		
		while (!openSet.isEmpty()) {
			Point current = openSet.stream().sorted((p1, p2) -> Integer.compare(nextFunction.applyAsInt(p1), nextFunction.applyAsInt(p2))).findFirst().get();
			
			if (current.equals(target)) {
				break;
			}
			
			openSet.remove(current);
			
			Point n1 = new Point(current.x - 1, current.y);
			Point n2 = new Point(current.x + 1, current.y);
			Point n3 = new Point(current.x, current.y - 1);
			Point n4 = new Point(current.x, current.y + 1);
			
			for (Point neighbor : List.of(n1, n2, n3, n4)) {
				if (neighbor.x >= 0 && neighbor.x < graph.length && neighbor.y >= 0 && neighbor.y < graph[0].length) {
					int alt = dist[current.x][current.y] + graph[neighbor.x][neighbor.y];
					if (alt < dist[neighbor.x][neighbor.y]) {
						dist[neighbor.x][neighbor.y] = alt;
						openSet.add(neighbor);
					}
				}
			}
		}
		
		return dist[target.x][target.y];
	}
	
	private static final int part2(byte[][] graph) {
		byte[][] larger = new byte[graph.length * 5][graph[0].length * 5];
		
		for (int i = 0; i < 5; i++) {
			for (int j = 0; j < 5; j++) {
				for (int k = 0; k < graph.length; k++) {
					for (int l = 0; l < graph[k].length; l++) {
						byte value = (byte) (graph[k][l] + i + j);
						if (value >= 10) {
							value -= 9;
						}
						larger[i * graph.length + k][j * graph[0].length + l] = value;
					}
				}
			}
		}
		
		return part1(larger);
	}
	
    private static byte[] lineToArray(String line) {
        byte[] array = new byte[line.length()];
        for (int i = 0; i < line.length(); i++) {
            array[i] = (byte) (line.charAt(i) - '0');
        }
        return array;
    }
	
	public static void main(String[] args) throws IOException {
		Path input = Path.of("src/day15/input.txt");
        
        byte[][] grid = Files.lines(input)
                .map(RiskyPaths::lineToArray)
                .toArray(size -> new byte[size][]);
        
        System.out.println(part1(grid));
        System.out.println(part2(grid));
	}
	
}

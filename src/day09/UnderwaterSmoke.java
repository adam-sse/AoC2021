package day09;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import util.Point;

public class UnderwaterSmoke {

    private static boolean compareSave(int[][] heightmap, int i1, int j1, int i2, int j2) {
        boolean result = true;
        if (i2 >= 0 && i2 < heightmap.length && j2 >= 0 && j2 < heightmap[i2].length) {
            int base = heightmap[i1][j1];
            int compare = heightmap[i2][j2];
            result = base < compare;
        }
        return result;
    }
    
    private static int part1(int[][] heightmap) {
        int sumOfThreats = 0;
        
        for (int i = 0; i < heightmap.length; i++) {
            for (int j = 0; j < heightmap[i].length; j++) {
                if (compareSave(heightmap, i, j, i - 1, j)
                        && compareSave(heightmap, i, j, i + 1, j)
                        && compareSave(heightmap, i, j, i, j - 1)
                        && compareSave(heightmap, i, j, i, j + 1)) {
                    int threat = heightmap[i][j] + 1;
                    sumOfThreats += threat;
                }
            }
        }
        
        return sumOfThreats;
    }
    
    private static boolean saveIsNine(int[][] heightmap, int i, int j) {
        boolean result = true;
        if (i >= 0 && i < heightmap.length && j >= 0 && j < heightmap[i].length) {
            result = heightmap[i][j] == 9;
        }
        return result;
    }
    
    private static void traverseBasin(int[][] heightmap, int i, int j, Set<Point> visited) {
        Point p = new Point(i, j);
        if (visited.contains(p)) {
            return;
        }
        
        visited.add(new Point(i, j));
        
        if (!saveIsNine(heightmap, i - 1, j)) {
            traverseBasin(heightmap, i - 1, j, visited);
        }
        if (!saveIsNine(heightmap, i + 1, j)) {
            traverseBasin(heightmap, i + 1, j, visited);
        }
        if (!saveIsNine(heightmap, i, j - 1)) {
            traverseBasin(heightmap, i, j - 1, visited);
        }
        if (!saveIsNine(heightmap, i, j + 1)) {
            traverseBasin(heightmap, i, j + 1, visited);
        }
    }
    
    private static int basinSize(int[][] heightmap, int i, int j) {
        Set<Point> visited = new HashSet<>(100);
        
        traverseBasin(heightmap, i, j, visited);
        
        return visited.size();
    }
    
    private static int part2(int[][] heightmap) {
        List<Integer> basinSizes = new LinkedList<>();
        
        for (int i = 0; i < heightmap.length; i++) {
            for (int j = 0; j < heightmap[i].length; j++) {
                if (compareSave(heightmap, i, j, i - 1, j)
                        && compareSave(heightmap, i, j, i + 1, j)
                        && compareSave(heightmap, i, j, i, j - 1)
                        && compareSave(heightmap, i, j, i, j + 1)) {
                    basinSizes.add(basinSize(heightmap, i, j));
                }
            }
        }
        
        return basinSizes.stream().sorted(Comparator.reverseOrder()).limit(3).mapToInt(Integer::intValue).reduce(1, (a, b) -> a * b);
    }
    
    private static int[] lineToArray(String line) {
        int[] array = new int[line.length()];
        for (int i = 0; i < line.length(); i++) {
            array[i] = line.charAt(i) - '0';
        }
        return array;
    }
    
    public static void main(String[] args) throws IOException {
        Path input = Path.of("src/day09/input.txt");
        
        int[][] heightmap = Files.lines(input)
                .map(UnderwaterSmoke::lineToArray)
                .toArray(size -> new int[size][]);
        
        System.out.println(part1(heightmap));
        System.out.println(part2(heightmap));
    }
    
}

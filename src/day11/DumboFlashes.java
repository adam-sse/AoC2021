package day11;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class DumboFlashes {

    private static void increaseNeighbors(int[][] grid, int i, int j) {
        for (int di = -1; di <= 1; di++) {
            for (int dj = -1; dj <= 1; dj++) {
                if (i + di >= 0 && i + di < grid.length && j + dj >= 0 && j + dj < grid[0].length) {
                    grid[i + di][j + dj]++;
                }
            }
        }
    }
    
    private static int step(int[][] grid) {
        boolean[][] flashed = new boolean[grid.length][grid[0].length];
        int numFlashes = 0;
        
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                grid[i][j]++;
            }
        }
        
        boolean foundFlash;
        do {
            foundFlash = false;
            for (int i = 0; i < grid.length; i++) {
                for (int j = 0; j < grid[i].length; j++) {
                    if (grid[i][j] > 9 && !flashed[i][j]) {
                        flashed[i][j] = true;
                        increaseNeighbors(grid, i, j);
                        foundFlash = true;
                        numFlashes++;
                    }
                }
            }
        } while (foundFlash);
        
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                if (flashed[i][j]) {
                    grid[i][j] = 0;
                }
            }
        }
        
        return numFlashes;
    }
    
    private static int part1(int[][] grid) {
        int numFlashes = 0;
        
        for (int i = 0; i < 100; i++) {
            numFlashes += step(grid);
        }
        
        return numFlashes;
    }
    
    private static int part2(int[][] grid) {
        int step = 0;
        while (true) {
            step++;
            
            int flashes = step(grid);
            if (flashes == 100) {
                return step;
            }
        }
    }
    
    private static int[] lineToArray(String line) {
        int[] array = new int[line.length()];
        for (int i = 0; i < line.length(); i++) {
            array[i] = line.charAt(i) - '0';
        }
        return array;
    }
    
    public static void main(String[] args) throws IOException {
        Path input = Path.of("src/day11/input.txt");
        
        int[][] grid = Files.lines(input)
                .map(DumboFlashes::lineToArray)
                .toArray(size -> new int[size][]);
        
        System.out.println(part1(grid));
        
        grid = Files.lines(input)
                .map(DumboFlashes::lineToArray)
                .toArray(size -> new int[size][]);
        
        System.out.println(part2(grid));
    }
    
}

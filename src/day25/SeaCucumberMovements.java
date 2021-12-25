package day25;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class SeaCucumberMovements {

    private static char[] lineToArray(String line) {
        char[] array = new char[line.length()];
        for (int i = 0; i < line.length(); i++) {
            array[i] = line.charAt(i);
        }
        return array;
    }

    private static boolean step(char[][] map) {
    	boolean moved = false;
    	
    	char[][] next = new char[map.length][map[0].length];
    	
    	for (int row = 0; row < map.length; row++) {
    		for (int column = 0; column < map[row].length; column++) {
    			int nextColumn = (column + 1) % map[row].length;
    			if (map[row][column] == '>' && map[row][nextColumn] == '.') {
    				moved = true;
    				next[row][column] = '.';
    				next[row][nextColumn] = '>';
    			}
    		}
    	}
    	
    	for (int row = 0; row < map.length; row++) {
    		for (int column = 0; column < map[row].length; column++) {
    			if (next[row][column] != 0) {
    				map[row][column] = next[row][column];
    				next[row][column] = 0;
    			}
    		}
    	}
    	
    	for (int row = 0; row < map.length; row++) {
    		for (int column = 0; column < map[row].length; column++) {
    			int nextRow = (row + 1) % map.length;
    			if (map[row][column] == 'v' && map[nextRow][column] == '.') {
    				moved = true;
    				next[row][column] = '.';
    				next[nextRow][column] = 'v';
    			}
    		}
    	}
    	
    	for (int row = 0; row < map.length; row++) {
    		for (int column = 0; column < map[row].length; column++) {
    			if (next[row][column] != 0) {
    				map[row][column] = next[row][column];
    			}
    		}
    	}
    	
    	return moved;
    }
    
    private static int part1(char[][] map) {
    	int steps = 0;
    	boolean moved = false;
    	do {
    		moved = step(map);
    		steps++;
    		
//    		System.out.println();
//    		System.out.println("After " + steps + " steps:");
//        	for (int row = 0; row < map.length; row++) {
//        		for (int column = 0; column < map[row].length; column++) {
//        			System.out.print(map[row][column]);
//        		}
//        		System.out.println();
//        	}
    	} while (moved);
    	
    	return steps;
    }
	
	public static void main(String[] args) throws IOException {
		Path input = Path.of("src/day25/input.txt");
		
		char[][] map = Files.lines(input)
				.map(SeaCucumberMovements::lineToArray)
				.toArray(size -> new char[size][]);
		
		
		System.out.println(part1(map));
	}
	
}

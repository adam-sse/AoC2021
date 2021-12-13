package day13;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class ManualFolding {
    
    private static boolean[][] instruction(boolean[][] paper, String instruction) {
        char direction = instruction.charAt(11);
        
        // position doesn't matter, we always fold in the middle
        
        boolean[][] result = new boolean
                [direction == 'x' ? paper.length : paper.length / 2 ]
                [direction == 'x' ? paper[0].length / 2 : paper[0].length];
        
        for (int row = 0; row < result.length; row++) {
            for (int column = 0; column < result[row].length; column++) {
                result[row][column] = paper[row][column];
                
                if (direction == 'x') {
                    result[row][column] |= paper[row][paper[row].length - column - 1];
                } else {
                    result[row][column] |= paper[paper.length - row - 1][column];
                }
            }
        }
        
        return result;
    }
    
    private static int part1(boolean[][] paper, String instruction) {
        
        paper = instruction(paper, instruction);
        
        int numDots = 0;
        for (boolean[] line : paper) {
            for (boolean dot : line) {
                if (dot) {
                    numDots++;
                }
            }
        }
        
        return numDots;
    }
    
    private static String part2(boolean[][] paper, List<String> instructions) {
        for (String instruction : instructions) {
            paper = instruction(paper, instruction);
        }
        
        
        StringBuilder sb = new StringBuilder();
        for (boolean[] line : paper) {
            for (boolean b : line) {
                sb.append(b ? '#' : '.');
            }
            sb.append('\n');
        }
        
        return sb.toString();
    }
    
    public static void main(String[] args) throws IOException {
        Path input = Path.of("src/day13/input.txt");
        
        List<String> lines = Files.readAllLines(input);
        
        int maxX = 0;
        int maxY = 0;
        for (String line : lines) {
            if (line.equals("")) break;
            
            int comma = line.indexOf(',');
            int x = Integer.parseInt(line.substring(0, comma));
            int y = Integer.parseInt(line.substring(comma + 1));
            
            if (x > maxX) maxX = x;
            if (y > maxY) maxY = y;
        }
        
        boolean[][] paper = new boolean[maxY + 1][maxX + 1];
        
        for (String line : lines) {
            if (line.equals("")) break;
            
            int comma = line.indexOf(',');
            int x = Integer.parseInt(line.substring(0, comma));
            int y = Integer.parseInt(line.substring(comma + 1));
            
            paper[y][x] = true;
        }
        
        System.out.println(part1(paper, lines.get(lines.indexOf("") + 1)));
        System.out.print(part2(paper, lines.subList(lines.indexOf("") + 1, lines.size())));
    }

}

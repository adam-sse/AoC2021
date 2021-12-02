package day02;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

public class Piloting {

    private static int part1(List<String> lines) {
        int depth = 0;
        int horizontalPosition = 0;
        
        for (String line : lines) {
            int space = line.indexOf(' ');
            String command = line.substring(0, space);
            int amount = Integer.parseInt(line.substring(space + 1));
            
            switch (command) {
            case "forward":
                horizontalPosition += amount;
                break;
            case "down":
                depth += amount;
                break;
            case "up":
                depth -= amount;
                break;
            }
        }
        
        return horizontalPosition * depth;
    }
    
    private static int part2(List<String> lines) {
        int depth = 0;
        int horizontalPosition = 0;
        int aim = 0;
        
        for (String line : lines) {
            int space = line.indexOf(' ');
            String command = line.substring(0, space);
            int amount = Integer.parseInt(line.substring(space + 1));
            
            switch (command) {
            case "forward":
                horizontalPosition += amount;
                depth += amount * aim;
                break;
            case "down":
                aim += amount;
                break;
            case "up":
                aim -= amount;
                break;
            }
        }
        
        return horizontalPosition * depth;
    }
    
    public static void main(String[] args) throws IOException {
        Path input = Path.of("src/day02/input.txt");
        
        List<String> lines = Files.lines(input).collect(Collectors.toList());
        
        System.out.println(part1(lines));
        System.out.println(part2(lines));
    }
    
}

package day01;

import java.nio.file.Files;
import java.nio.file.Path;

public class DepthDetector {
    
    private static int part1(int[] measurements) {
        int numIncreases = 0;
        for (int i = 1; i < measurements.length; i++) {
            if (measurements[i - 1] < measurements[i]) {
                numIncreases++;
            }
        }
        return numIncreases;
    }
    
    
    private static int part2(int[] measurements) {
        int[] sliding = new int[measurements.length - 2];
        for (int i = 2; i < measurements.length; i++) {
            sliding[i - 2] = measurements[i - 2] + measurements[i - 1] + measurements[i];
        }
        return part1(sliding);
    }

    public static void main(String[] args) throws Throwable /* <- this is called good software engineering */ {
        Path input = Path.of("src/day01/input.txt");
        
        int[] measurements = Files.lines(input)
            .mapToInt(Integer::parseInt)
            .toArray();
        
        System.out.println(part1(measurements));
        System.out.println(part2(measurements));
    }
    
}

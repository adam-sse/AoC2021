package day07;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

public class CrabFuel {
    
    private static int part1(int[] positions) {
        Arrays.sort(positions);
        int alignPos = positions[positions.length / 2];
        
        int cost = 0;
        for (int pos : positions) {
            cost += Math.abs(alignPos - pos);
        }
        
        return cost;
    }
    
    
    private static int getCost(int[] positions, int alignPos) {
        int cost = 0;
        for (int pos : positions) {
            int diff = Math.abs(alignPos - pos);
            cost += diff * (diff + 1) / 2;
        }
        
        return cost;
    }
    
    private static int part2(int[] positions) {
        int alignPos1 = (int) Arrays.stream(positions).summaryStatistics().getAverage();
        int alignPos2 = (int) Arrays.stream(positions).summaryStatistics().getAverage() + 1;
        
        return Math.min(getCost(positions, alignPos1), getCost(positions, alignPos2));
    }

    public static void main(String[] args) throws IOException {
        Path input = Path.of("src/day07/input.txt");
        
        int[] positions = Arrays.stream(Files.lines(input).findFirst().orElseThrow().split(","))
                .mapToInt(Integer::parseInt)
                .toArray();
        
        System.out.println(part1(positions));
        System.out.println(part2(positions));
    }
    
}

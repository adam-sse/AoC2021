package day06;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class FishReproduction {

    public static void main(String[] args) throws IOException {
        Path input = Path.of("src/day06/input.txt");
        
        Map<Integer, Long> ifishies = new HashMap<>(10);
        
        Arrays.stream(Files.lines(input).findFirst().orElseThrow().split(","))
                .map(Integer::parseInt)
                .forEach(f -> ifishies.put(f, ifishies.getOrDefault(f, 0L) + 1));
        
        Map<Integer, Long> fishies = ifishies;
        
        for (int day = 0; day < 256; day++) {
            Map<Integer, Long> newFishies = new HashMap<>(10);
            
            for (Map.Entry<Integer, Long> entry : fishies.entrySet()) {
                int newDays = entry.getKey() - 1;
                
                if (newDays == -1) {
                    newDays = 6;
                    newFishies.put(8, entry.getValue());
                }
                
                newFishies.put(newDays, newFishies.getOrDefault(newDays, 0L) + entry.getValue());
            }
            
            fishies = newFishies;
            if (day == 79) {
                System.out.println(fishies.values().stream().mapToLong(Long::longValue).sum());
            }
        }
        
        System.out.println(fishies.values().stream().mapToLong(Long::longValue).sum());
        
    }
    
}

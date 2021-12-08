package day08;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class SevenSegment {
    
    private static class Line {
        private Set<Character>[] hints;
        private Set<Character>[] output;
        Line(Set<Character>[] hints, Set<Character>[] output) {
            this.hints = hints;
            this.output = output;
        }
    }
    
    private static int part1(List<Line> lines) {
        return (int) lines.stream()
            .flatMap(line -> Arrays.stream(line.output))
            .filter(output -> output.size() == 2 || output.size() == 4 || output.size() == 3 || output.size() == 7)
            .count();
    }
    
    private static Set<Character> findLength(Set<Character>[] array, int length) {
        for (int i = 0; i < array.length; i++) {
            if (array[i].size() == length) {
                return array[i];
            }
        }
        throw new RuntimeException();
    }
    
    @SafeVarargs
    private static Set<Character> without(Set<Character> base, Set<Character>... toRemove) {
        Set<Character> result = new HashSet<>(base);
        for (Set<Character> set : toRemove) {
            result.removeAll(set);
        }
        return result;
    }
    
    private static int getValue(Set<Character> reading, Set<Character>[] mapping) {
        for (int i = 0; i < mapping.length; i++) {
            if (reading.equals(mapping[i])) {
                return i;
            }
        }
        throw new RuntimeException();
    }
    
    private static int part2(List<Line> lines) {
        
        int sum = 0;
        
        for (Line line : lines) {
            @SuppressWarnings("unchecked")
            Set<Character>[] mapping = new Set[10];
            
            mapping[1] = findLength(line.hints, 2);
            mapping[4] = findLength(line.hints, 4);
            mapping[7] = findLength(line.hints, 3);
            mapping[8] = findLength(line.hints, 7);
            
            Set<Character> bOrD = without(mapping[4], mapping[1]);
            Set<Character> eOrG = without(mapping[8], mapping[4], mapping[7]);
            Set<Character> cOrF = new HashSet<>(mapping[1]);
            
            // 0, 6, 9
            List<Set<Character>> lengthSix = Arrays.stream(line.hints).filter(h -> h.size() == 6).collect(Collectors.toList());
            for (Set<Character> hint : lengthSix) {
                if (without(hint, bOrD).size() == 5) {
                    mapping[0] = hint;
                } else if (without(hint, cOrF).size() == 5) {
                    mapping[6] = hint;
                } else {
                    mapping[9] = hint;
                }
            }
            
            // 2, 3, 5
            List<Set<Character>> lengthFive = Arrays.stream(line.hints).filter(h -> h.size() == 5).collect(Collectors.toList());
            for (Set<Character> hint : lengthFive) {
                if (without(hint, cOrF).size() == 3) {
                    mapping[3] = hint;
                } else if (without(hint, eOrG).size() == 3) {
                    mapping[2] = hint;
                } else {
                    mapping[5] = hint;
                }
            }
            
            sum += getValue(line.output[0], mapping) * 1000
                    + getValue(line.output[1], mapping) * 100
                    + getValue(line.output[2], mapping) * 10
                    + getValue(line.output[3], mapping);
        }
        
        
        return sum;
    }

    private static Set<Character> toSet(String s) {
        Set<Character> set = new HashSet<>(s.length());
        for (char c : s.toCharArray()) {
            set.add(c);
        }
        return set;
    }
    
    public static void main(String[] args) throws IOException {
        Path input = Path.of("src/day08/input.txt");
        
        @SuppressWarnings("unchecked")
        List<Line> lines = Files.lines(input)
                .map(line -> {
                    int sep = line.indexOf('|');
                    String[] hints = line.substring(0, sep - 1).split(" ");
                    String[] output = line.substring(sep + 2).split(" ");
                    
                    return new Line(Arrays.stream(hints).map(SevenSegment::toSet).toArray(i -> new Set[i]),
                            Arrays.stream(output).map(SevenSegment::toSet).toArray(i -> new Set[i]));
                })
                .collect(Collectors.toList());
        
        System.out.println(part1(lines));
        System.out.println(part2(lines));
    }
    
}

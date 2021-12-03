package day03;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Diagnostics {
    
    private static int part1(List<String> lines) {
        int[] numberOfOnes = lines.stream()
            .map(s -> {
                int[] array = new int[s.length()];
                for (int i = 0; i < array.length; i++) {
                    array[i] = s.charAt(i) == '0' ? 0 : 1;
                }
                return array;
            })
            .reduce((a1, a2) -> {
                for (int i = 0; i < a1.length; i++) {
                    a1[i] += a2[i];
                }
                return a1;
            }).orElseThrow();
        
        int gammaRate = 0;
        int mask = 0;
        for (int i = 0; i < numberOfOnes.length; i++) {
            if (numberOfOnes[i] > lines.size() / 2) {
                gammaRate |= 1 << numberOfOnes.length - i - 1;
            }
            mask |= 1 << numberOfOnes.length - i - 1;
        }
        
        int epsilonRate = ~gammaRate & mask;
        
        return epsilonRate * gammaRate;
    }
    
    private static int mostcommon(List<Integer> array, int bit) {
        int numones = 0;
        int mask = 1 << bit;
        for (int i = 0; i < array.size(); i++) {
            if ((array.get(i) & mask) != 0) {
                numones++;
            }
        }
        
        if (array.size() / 2.0 == numones) {
            return 1;
        } else if (array.size() / 2.0 < numones) {
            return 1;
        } else {
            return 0; 
        }
    }
    
    private static int reduceList(List<Integer> list, boolean keepcommon, int numBits) {
        int bit = numBits - 1;
        while (list.size() > 1) {
            int mask = 1 << bit;
            int mostcommon = mostcommon(list, bit) << bit;
            list = list.stream()
                    .filter(i -> ((i & mask) == mostcommon) ^ !keepcommon)
                    .collect(Collectors.toList());
            bit--;
        }
        return list.get(0);
    }
    
    private static int part2(List<String> lines) {
        List<Integer> olist = lines.stream()
                .map(s -> Integer.parseInt(s, 2))
                .collect(Collectors.toList());
        List<Integer> co2list = new ArrayList<>(olist);
        
        
        int o = reduceList(olist, true, lines.get(0).length());
        int co2 = reduceList(co2list, false, lines.get(0).length());
        
        return o * co2;
    }

    public static void main(String[] args) throws IOException {
        Path input = Path.of("src/day03/input.txt");
        
        System.out.println(part1(Files.lines(input).collect(Collectors.toList())));
        System.out.println(part2(Files.lines(input).collect(Collectors.toList())));
    }
    
}

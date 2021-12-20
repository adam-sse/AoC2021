package day20;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class ImageEnhancer {
    
    private static byte outside = 0;
    
    private static byte get(byte[][] image, int i, int j) {
        if (i >= 0 && i < image.length && j >= 0 && j < image[i].length) {
            return image[i][j];
        } else {
            return outside;
        }
    }
    
    private static byte[][] step(byte[][] image, int[] enhancementTable) {
        byte[][] result = new byte[image.length + 2][image[0].length + 2];
        
        for (int i = 0; i < result.length; i++) {
            for (int j = 0; j < result[i].length; j++) {

                int region = 0;
                for (int di = -1; di <= 1; di++) {
                    for (int dj = -1; dj <= 1; dj++) {
                        region <<= 1;
                        region |= get(image, i - 1 + di, j - 1 + dj);
                    }
                }
                result[i][j] = (byte) enhancementTable[region];
            }
        }
        
        if (outside == 1) {
            outside = (byte) enhancementTable[0b111111111];
        } else {
            outside = (byte) enhancementTable[0];
        }
        
        return result;
    }

    private static int countLightAfterNIterations(byte[][] image, int[] enhancementTable, int n) {
        outside = 0;
        for (int i = 0; i < n; i++) {
            image = step(image, enhancementTable);
            
//            System.out.println();
//            print(image);
        }
        
        if (outside == 1) {
            throw new IllegalStateException("Infinitely many pixels are light");
        }
        
        int numLight = 0;
        for (int i = 0; i < image.length; i++) {
            for (int j = 0; j < image[i].length; j++) {
                numLight += image[i][j];
            }
        }
        
        return numLight;
    }
    
    
    private static byte[] lineToArray(String line) {
        byte[] array = new byte[line.length()];
        for (int i = 0; i < line.length(); i++) {
            array[i] = (byte) (line.charAt(i) == '#' ? 1 : 0);
        }
        return array;
    }
    
//    private static void print(byte[][] image) {
//        for (int i = 0; i < image.length; i++) {
//            for (int j = 0; j < image.length; j++) {
//                System.out.print(image[i][j] == 1 ? '#' : '.');
//            }
//            System.out.println();
//        }
//    }
    
    public static void main(String[] args) throws IOException {
        Path input = Path.of("src/day20/input.txt");
        
        List<String> lines = Files.readAllLines(input);
        int[] enhancementTable = lines.get(0).chars()
                .map(c -> c == '#' ? 1 : 0)
                .toArray();
        
        lines.remove(0);
        lines.remove(0);
        
        byte[][] image = lines.stream()
            .map(ImageEnhancer::lineToArray)
            .toArray(size -> new byte[size][]);
        
        
        System.out.println(countLightAfterNIterations(image, enhancementTable, 2));
        System.out.println(countLightAfterNIterations(image, enhancementTable, 50));
    }
    
}

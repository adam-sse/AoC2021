package day24;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class MonadProgramAnalyzer {

	private static class Block {
		long p1;
		long p2;
		long p3;
		Block(long p1, long p2, long p3) {
			this.p1 = p1;
			this.p2 = p2;
			this.p3 = p3;
		}
		long getDecreaseInput(long z) {
			return z % 26 + p2;
		}
		boolean canDecrease() {
			return p2 < 10 && p1 > 1;
		}
		boolean canDecrease(long z) {
			return p1 > 1 && getDecreaseInput(z) >= 1 && getDecreaseInput(z) <= 9;
		}
		long evaluate(long z, long input) {
			if ((z % 26 + p2) != input) {
				z = (z / p1) * 26 + input + p3;
			} else {
				z = z / p1;
			}
			return z;
		}
	}
	
	private static long evaluate(int[] serial, List<Block> blocks) {
		long z = 0;
		for (int i = 0; i < serial.length; i++) {
			z = blocks.get(i).evaluate(z, serial[i]);
		}
		return z;
	}
	
	private static List<Integer> search(List<Block> blocks, int blockIndex, long currentZ, int[] inputPriority) {
		if (blockIndex >= blocks.size()) {
			return currentZ == 0 ? new LinkedList<>() : null;
		}
		
		Block block = blocks.get(blockIndex);
		
		if (!block.canDecrease()) {
			for (int input : inputPriority) {
				List<Integer> result = search(blocks, blockIndex + 1, block.evaluate(currentZ, input), inputPriority);
				if (result != null) {
					result.add(input);
					return result;
				}
			}
			return null;
		} else {
			if (block.canDecrease(currentZ)) {
				int input = (int) block.getDecreaseInput(currentZ);
				List<Integer> result = search(blocks, blockIndex + 1, block.evaluate(currentZ, input), inputPriority);
				if (result != null) {
					result.add(input);
					return result;
				}
			}
			// assume that all blocks that can decrease also have to
			return null;
		}
	}
	
	public static void main(String[] args) throws IOException {
		Path file = Path.of("src/day24/input.txt");
		
		List<Block> blocks = new ArrayList<>(14);
		List<String> lines = Files.readAllLines(file);
		for (int i = 0; i < lines.size(); i += 18) {
			int p1 = Integer.parseInt(lines.get(i + 4).substring(6));
			int p2 = Integer.parseInt(lines.get(i + 5).substring(6));
			int p3 = Integer.parseInt(lines.get(i + 15).substring(6));
			blocks.add(new Block(p1, p2, p3));
		}
		
		List<Integer> result = search(blocks, 0, 0, new int[] {9,8,7,6,5,4,3,2,1});
		Collections.reverse(result);
		if (evaluate(result.stream().mapToInt(Integer::intValue).toArray(), blocks) != 0) {
			throw new RuntimeException();
		}
		System.out.println(result.stream().map(String::valueOf).collect(Collectors.joining()));
		
		result = search(blocks, 0, 0, new int[] {1,2,3,4,5,6,7,8,9});
		Collections.reverse(result);
		if (evaluate(result.stream().mapToInt(Integer::intValue).toArray(), blocks) != 0) {
			throw new RuntimeException();
		}
		System.out.println(result.stream().map(String::valueOf).collect(Collectors.joining()));
	}
	
}

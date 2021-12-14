package day14;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.HashMap;
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.LongSummaryStatistics;
import java.util.Map;
import java.util.Objects;

public class PolymerProduction {
	
	private static int part1(String polymer, Map<String, Character> rules) {
		for (int i = 0; i < 10; i++) {
			for (int pos = 0; pos < polymer.length() - 1; pos++) {
				Character insertion = rules.get(polymer.substring(pos, pos + 2));
				if (insertion != null) {
					polymer = polymer.substring(0, pos + 1) + insertion + polymer.substring(pos + 1);
					pos++;
				}
			}
		}
		
		Map<Character, Integer> counts = new HashMap<>(26);
		for (char c : polymer.toCharArray()) {
			counts.putIfAbsent(c, 0);
			counts.put(c, counts.get(c) + 1);
		}
		
		IntSummaryStatistics iss = counts.values().stream().mapToInt(Integer::intValue).summaryStatistics();
		
		return iss.getMax() - iss.getMin();
	}
	
	private static class Input {
		char c1;
		char c2;
		int depth;
		Input(char c1, char c2, int depth) {
			this.c1 = c1;
			this.c2 = c2;
			this.depth = depth;
		}
		@Override
		public int hashCode() {
			return Objects.hash(c1, c2, depth);
		}
		@Override
		public boolean equals(Object obj) {
			if (this == obj) {
				return true;
			}
			if (!(obj instanceof Input)) {
				return false;
			}
			Input other = (Input) obj;
			return c1 == other.c1 && c2 == other.c2 && depth == other.depth;
		}
		@Override
		public String toString() {
			return "Input [c1=" + c1 + ", c2=" + c2 + ", depth=" + depth + "]";
		}
	}
	
	private static void add(Map<Character, Long> base, Map<Character, Long> toAdd) {
		for (Map.Entry<Character, Long> entry : toAdd.entrySet()) {
			if (!base.containsKey(entry.getKey())) {
				base.put(entry.getKey(), entry.getValue());
			} else {
				base.put(entry.getKey(), base.get(entry.getKey()) + entry.getValue());
			}
		}
	}
	
	private static Map<Input, Map<Character, Long>> DP = new HashMap<>(10000); 
	private static Map<Character, Long> visit(char c1, char c2, Map<String, Character> rules, int depth) {
		Input i = new Input(c1, c2, depth);
		if (DP.containsKey(i)) {
			return DP.get(i);
		}
		
		if (depth > 40) {
			return Collections.emptyMap();
		}
		
		Map<Character, Long> counts = new HashMap<>(26);
		
		Character insertion = rules.get(String.valueOf(c1) + c2);
		if (insertion != null) {
			counts.put(insertion, 1L);
			
			add(counts, visit(c1, insertion, rules, depth + 1));
			add(counts, visit(insertion, c2, rules, depth + 1));
		}
		
		DP.put(i, counts);
		return counts;
	}
	
	private static long part2(String polymer, Map<String, Character> rules) {
		Map<Character, Long> counts = new HashMap<>(26);
		for (char c : polymer.toCharArray()) {
			counts.put(c, 1L);
		}
		
		for (int pos = 0; pos < polymer.length() - 1; pos++) {
			add(counts, visit(polymer.charAt(pos), polymer.charAt(pos + 1), rules, 1));
		}
		
		LongSummaryStatistics iss = counts.values().stream().mapToLong(Long::longValue).summaryStatistics();
		return iss.getMax() - iss.getMin();
	}

	public static void main(String[] args) throws IOException {
		Path input = Path.of("src/day14/input.txt");
		
		List<String> lines = Files.readAllLines(input);
		String polymer = lines.get(0);
		lines.remove(0);
		lines.remove(0);
		
		Map<String, Character> rules = new HashMap<>(lines.size());
		for (String line : lines) {
			rules.put(line.substring(0, 2), line.charAt(6));
		}
		
		System.out.println(part1(polymer, rules));
		System.out.println(part2(polymer, rules));
	}
	
}

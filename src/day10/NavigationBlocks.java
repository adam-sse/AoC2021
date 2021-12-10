package day10;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class NavigationBlocks {
	
	private static final Map<Character, Character> CLOSING = Map.of('(', ')', '{', '}', '[', ']', '<', '>');
	
	private static final Map<Character, Character> OPENING = Map.of(')', '(', '}', '{', ']', '[', '>', '<');

	
	private static int part1(List<String> lines) {
		Map<Character, Integer> scoreMap = Map.of(')', 3, ']', 57, '}', 1197, '>', 25137);
		
		int score = 0;
		
		for (String line : lines) {
			Deque<Character> stack = new LinkedList<>();
			
			char illegal = 0;
			loop: for (char c : line.toCharArray())  {
				switch (c) {
				case '(':
				case '{':
				case '[':
				case '<':
					stack.push(c);
					break;
					
				case ')':
				case '}':
				case ']':
				case '>':
					if (stack.pop() != OPENING.get(c)) {
						illegal = c;
						break loop;
					}
					break;
				}
			}
			
			if (illegal != 0) {
				score += scoreMap.get(illegal);
			}
		}
		
		return score;
	}
	
	private static long part2(List<String> lines) {
		Map<Character, Integer> scoreMap = Map.of(')', 1, ']', 2, '}', 3, '>', 4);
		
		List<Long> scores = new LinkedList<>();
		
		for (String line : lines) {
			Deque<Character> stack = new LinkedList<>();
			
			char illegal = 0;
			loop: for (char c : line.toCharArray())  {
				switch (c) {
				case '(':
				case '{':
				case '[':
				case '<':
					stack.push(c);
					break;
					
				case ')':
				case '}':
				case ']':
				case '>':
					if (stack.pop() != OPENING.get(c)) {
						illegal = c;
						break loop;
					}
					break;
				}
			}
			
			if (illegal == 0) {
				long score = 0;
				while (!stack.isEmpty()) {
					char closing = CLOSING.get(stack.pop());
					score = score * 5L + scoreMap.get(closing);
				}
				scores.add(score);
			}
		}
		
		Collections.sort(scores);
		return scores.get(scores.size() / 2);
	}
	
	public static void main(String[] args) throws IOException {
		Path input = Path.of("src/day10/input.txt");
		
		List<String> lines = Files.readAllLines(input);
		
		System.out.println(part1(lines));
		System.out.println(part2(lines));
	}
	
}

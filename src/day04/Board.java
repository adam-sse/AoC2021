package day04;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Board {

	private int[][] numbers;
	
	private boolean[][] marked;
	
	public Board(int[][] numbers) {
		this.numbers = numbers;
		this.marked = new boolean[numbers.length][numbers[0].length];
	}
	
	public void mark(int number) {
		for (int row = 0; row < numbers.length; row++) {
			for (int column = 0; column < numbers[row].length; column++) {
				if (numbers[row][column] == number) {
					marked[row][column] = true;
				}
			}
		}
	}
	
	public boolean hasWon() {
		for (int row = 0; row < numbers.length; row++) {
			boolean allMarked = true;
			for (int column = 0; column < numbers[row].length; column++) {
				if (!marked[row][column]) {
					allMarked = false;
					break;
				}
			}
			
			if (allMarked) {
				return true;
			}
		}
		
		for (int column = 0; column < numbers[0].length; column++) {
			boolean allMarked = true;
			for (int row = 0; row < numbers.length; row++) {
				if (!marked[row][column]) {
					allMarked = false;
					break;
				}
			}
			
			if (allMarked) {
				return true;
			}
		}
		
		return false;
	}
	
	public int sumNonMarked() {
		int sum = 0;
		for (int row = 0; row < numbers.length; row++) {
			for (int column = 0; column < numbers[row].length; column++) {
				if (!marked[row][column]) {
					sum += numbers[row][column];
				}
			}
		}
		
		return sum;
	}
	
	private static int part1(List<Integer> calls, List<Board> boards) {
		for (Integer call : calls) {
			for (Board board : boards) {
				board.mark(call);
				
				if (board.hasWon()) {
					return board.sumNonMarked() * call;
				}
			}
		}
		return -1;
	}
	private static int part2(List<Integer> calls, List<Board> boards) {
		for (Integer call : calls) {
			Set<Board> won = new HashSet<>();
			for (Board board : boards) {
				board.mark(call);
				
				if (board.hasWon()) {
					if (boards.size() > 1) {
						won.add(board);
					} else {
						return board.sumNonMarked() * call;
					}
				}
			}
			
			boards.removeAll(won);
		}
		
		return -1;
	}
	
	public static void main(String[] args) throws IOException {
		Path input = Path.of("src/day04/input.txt");
		
		List<String> lines = Files.lines(input).collect(Collectors.toList());
		
		List<Integer> calls = new LinkedList<>();
		Arrays.stream(lines.get(0).split(",")).map(Integer::parseInt).forEach(calls::add);
		
		lines.remove(0);
		lines.remove(0);
		
		List<Board> boards = new LinkedList<>();
		for (int i = 0; i < lines.size(); i += 6) {
			int[][] numbers = new int[5][5];
			for (int j = 0; j < 5; j++) {
				numbers[j] = Arrays.stream(lines.get(i + j).split(" ")).filter(s -> !s.isBlank()).mapToInt(Integer::parseInt).toArray();
			}
			boards.add(new Board(numbers));
		}
		
		System.out.println(part1(calls, boards));
		System.out.println(part2(calls, boards));
	}
	
}

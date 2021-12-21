package day21;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DiracDice {

	private static int part1(int player1Start, int player2Start) {
		int die = 1;
		int numRolls = 0;
		int player = 0;

		int[] score = {0, 0};
		int[] pos = {player1Start - 1, player2Start - 1};
		
		while (score[0] < 1000 && score[1] < 1000) {
			int move = 3 * die + 3;
			die += 3;
			if (die > 100) {
				die -= 100;
			}
			numRolls += 3;
			
			pos[player] = (pos[player] + move) % 10;
			
			score[player] += pos[player] + 1;
			
			player = (player + 1) % 2;
		}
		
		int losingScore = score[0] < 1000 ? score[0] : score[1];
		return losingScore * numRolls;
	}
	
	
	private static class Input {
		final int[] pos;
		final int[] score;
		final int player;
		Input(int[] pos, int[] score, int player) {
			this.pos = pos;
			this.score = score;
			this.player = player;
		}
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + Arrays.hashCode(pos);
			result = prime * result + Arrays.hashCode(score);
			result = prime * result + Objects.hash(player);
			return result;
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
			return player == other.player && Arrays.equals(pos, other.pos) && Arrays.equals(score, other.score);
		}
	}
	
	private static class Output	{
		long p1wins;
		long p2wins;
		Output(long p1wins, long p2wins) {
			this.p1wins = p1wins;
			this.p2wins = p2wins;
		}
		void add(Output other) {
			this.p1wins += other.p1wins;
			this.p2wins += other.p2wins;
		}
	}
	
	private static Map<Input, Output> DP = new HashMap<>(100000);
	
	private static Output move(Input input) {
		Output result = DP.get(input);
		if (result != null) {
			return result;
		}
		
		result = new Output(0, 0);
		
		for (int die1 = 1; die1 <= 3; die1++) {
			for (int die2 = 1; die2 <= 3; die2++) {
				for (int die3 = 1; die3 <= 3; die3++) {
					int newPos = (input.pos[input.player] + die1 + die2 + die3) % 10;
					int newScore = input.score[input.player] + newPos + 1;
					
					if (newScore >= 21) {
						if (input.player == 0) {
							result.p1wins++;
						} else {
							result.p2wins++;
						}
						
					} else {
						int[] pos = {input.pos[0], input.pos[1]};
						pos[input.player] = newPos;
						int[] score = {input.score[0], input.score[1]};
						score[input.player] = newScore;
						int nextPlayer = (input.player + 1) % 2;
						
						result.add(move(new Input(pos, score, nextPlayer)));
					}
				}
			}
		}
		
		DP.put(input, result);
		return result;
	}
	
	private static long part2(int player1Start, int player2Start) {
		
		Output result = move(new Input(new int[] {player1Start - 1, player2Start - 1}, new int[] {0, 0}, 0));
		
		return Math.max(result.p1wins, result.p2wins);
	}
	
	public static void main(String[] args) throws IOException {
		Path input = Path.of("src/day21/input.txt");
		
		Pattern p = Pattern.compile("Player [12] starting position: (?<pos>[0-9]{1,2})");
		
		List<String> lines = Files.readAllLines(input);
		
		Matcher m = p.matcher(lines.get(0));
		m.matches();
		int player1Start = Integer.parseInt(m.group("pos"));
		
		m = p.matcher(lines.get(1));
		m.matches();
		int player2Start = Integer.parseInt(m.group("pos"));
		
		System.out.println(part1(player1Start, player2Start));
		System.out.println(part2(player1Start, player2Start));
	}
	
}

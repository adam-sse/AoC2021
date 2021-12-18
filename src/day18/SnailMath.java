package day18;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class SnailMath {

	private static void explode(SnailNumber toExplode) {
		int left = toExplode.getLeft().getValue();
		int right = toExplode.getRight().getValue();
		
		if (toExplode.getParent().getLeft() == toExplode) {
			
			SnailNumber insertion = toExplode.getParent().getRight();
			while (!insertion.isValue()) {
				insertion = insertion.getLeft();
			}
			insertion.setValue(insertion.getValue() + right);
			
			SnailNumber prev = toExplode;
			SnailNumber parent = toExplode.getParent();
			while (parent != null && parent.getLeft() == prev) {
				prev = parent;
				parent = parent.getParent();
			}
			if (parent != null) {
				insertion = parent.getLeft();
				while (!insertion.isValue()) {
					insertion = insertion.getRight();
				}
				insertion.setValue(insertion.getValue() + left);
			}
			
		} else {
			
			SnailNumber insertion = toExplode.getParent().getLeft();
			while (!insertion.isValue()) {
				insertion = insertion.getRight();
			}
			insertion.setValue(insertion.getValue() + left);
			
			SnailNumber prev = toExplode;
			SnailNumber parent = toExplode.getParent();
			while (parent != null && parent.getRight() == prev) {
				prev = parent;
				parent = parent.getParent();
			}
			if (parent != null) {
				insertion = parent.getRight();
				while (!insertion.isValue()) {
					insertion = insertion.getLeft();
				}
				insertion.setValue(insertion.getValue() + right);
			}
			
		}
		
		toExplode.getParent().replace(toExplode, new SnailNumber(0));
	}
	
	private static void split(SnailNumber number) {
		int left = (int) Math.floor(number.getValue() / 2.0);
		int right = (int) Math.ceil(number.getValue() / 2.0);
		
		number.getParent().replace(number, new SnailNumber(new SnailNumber(left), new SnailNumber(right)));
	}
	
	private static boolean reduceExplode(SnailNumber number, int depth) {
		boolean changed = false;
		if (!number.isValue()) {
			if (depth > 4) {
				explode(number);
				changed = true;
				
			} else {
				changed = reduceExplode(number.getLeft(), depth + 1);
				if (!changed) {
					changed = reduceExplode(number.getRight(), depth + 1);
				}
			}
		}
		
		return changed;
	}
	
	private static boolean reduceSplit(SnailNumber number) {
		boolean changed = false;
		if (number.isValue()) {
			if (number.getValue() >= 10) {
				split(number);
				changed = true;
			}
		} else {
			changed = reduceSplit(number.getLeft());
			if (!changed) {
				changed = reduceSplit(number.getRight());
			}
		}
		
		return changed;
	}
	
	private static boolean reduce(SnailNumber number) {
		return reduceExplode(number, 1) || reduceSplit(number);
	}
	
	private static SnailNumber add(SnailNumber left, SnailNumber right) {
		SnailNumber result = new SnailNumber(left, right);
		while (reduce(result));
		return result;
	}
	
	private static long part1(List<SnailNumber> numbers) {
		LinkedList<SnailNumber> n = new LinkedList<>(numbers);
		
		SnailNumber result = n.poll();
		while (!n.isEmpty()) {
			result = add(result, n.poll());
		}
		
		return result.magnitude();
	}
	
	private static long part2(List<SnailNumber> numbers) {
		long maxMag = 0;
		for (int i = 0; i < numbers.size(); i++) {
			for (int j = 0; j < numbers.size(); j++) {
				if (i != j) {
					// copy numbers by re-parsing, as they are modified in add()
					SnailNumber n1 = SnailNumber.parse(numbers.get(i).toString());
					SnailNumber n2 = SnailNumber.parse(numbers.get(j).toString());
					long mag = add(n1, n2).magnitude();
					maxMag = Math.max(maxMag, mag);
				}
			}
		}
		return maxMag;
	}
	
	public static void main(String[] args) throws IOException {
		Path input = Path.of("src/day18/input.txt");
		
		List<SnailNumber> numbers = Files.lines(input)
			.map(SnailNumber::parse)
			.collect(Collectors.toList());
		System.out.println(part1(numbers));
		
		numbers = Files.lines(input)
				.map(SnailNumber::parse)
				.collect(Collectors.toList());
		System.out.println(part2(numbers));
	}
	
}

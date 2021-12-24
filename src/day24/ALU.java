package day24;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.function.LongBinaryOperator;

public class ALU {

	private long registers[] = new long[4];
	
	private long[] input;
	
	private int inputP;
	
	public ALU(long... input) {
		this.input = input;
	}
	
	private void set(char target, long value) {
		registers[target - 'w'] = value;
	}
	
	private long get(char target) {
		return registers[target - 'w'];
	}
	
	private void apply(char target, String operand, LongBinaryOperator func) {
		if (operand.length() == 1 && operand.charAt(0) >= 'w' && operand.charAt(0) <= 'z') {
			set(target, func.applyAsLong(get(target), get(operand.charAt(0))));
		} else {
			set(target, func.applyAsLong(get(target), Integer.parseInt(operand)));
		}
	}
	
	public void processInstruction(String instruction) {
		String[] inst = instruction.split(" ");
		
		char target = inst[1].charAt(0);
		switch (inst[0]) {
		case "inp":
			set(target, input[inputP++]);
			break;
			
		case "add":
			apply(target, inst[2], (a, b) -> a + b);
			break;
			
		case "mul":
			apply(target, inst[2], (a, b) -> a * b);
			break;
			
		case "div":
			apply(target, inst[2], (a, b) -> a / b);
			break;
			
		case "mod":
			apply(target, inst[2], (a, b) -> a % b);
			break;
			
		case "eql":
			apply(target, inst[2], (a, b) -> a == b ? 1 : 0);
			break;
		}
	}
	
	public static void main(String[] args) throws IOException {
		Path file = Path.of("src/day24/input.txt");
		
		List<String> instructions = Files.readAllLines(file);
		
//		long[] input = {9,9,2,9,8,9,9,3,1,9,9,8,7,3};
		long[] input = {7,3,1,8,1,2,2,1,1,9,7,1,1,1};
		
		ALU alu = new ALU(input);
		instructions.stream().forEach(alu::processInstruction);
		
		System.out.println(alu.get('z'));
	}
	
}

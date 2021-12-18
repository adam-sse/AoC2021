package day18;

import java.io.IOException;
import java.io.StringReader;

public class SnailNumber {

	private int value;
	
	private SnailNumber left;
	
	private SnailNumber right;
	
	private SnailNumber parent;
	
	public SnailNumber(int value) {
		this.value = value;;
	}

	public SnailNumber(SnailNumber left, SnailNumber right) {
		this.left = left;
		this.right = right;
		
		this.left.parent = this;
		this.right.parent = this;
	}
	
	public static SnailNumber parse(String number) {
		try {
			return parse(new StringReader(number));
		} catch (IOException e) {
			// can't happen
			throw new RuntimeException(e);
		}
	}
	
	private static SnailNumber parse(StringReader in) throws IOException {
		char start = (char) in.read();
		if (start == '[') {
			
			SnailNumber left = parse(in);
			
			char comma = (char) in.read();
			if (comma != ',') {
				throw new IOException("Expected comma, got " + comma);
			}
			
			SnailNumber right = parse(in);
			
			char closing = (char) in.read();
			if (closing != ']') {
				throw new IOException("Expected closing bracket, got " + closing);
			}
			
			return new SnailNumber(left, right);
			
		} else {
			return new SnailNumber(start - '0');
		}
	}

	public boolean isValue() {
		return left == null;
	}
	
	public int getValue() {
		if (!isValue()) {
			throw new IllegalStateException();
		}
		return value;
	}
	
	public void setValue(int value) {
		this.value = value;
		this.left = null;
		this.right = null;
	}
	
	public SnailNumber getLeft() {
		return left;
	}
	
	public void replaceLeft(SnailNumber left) {
		if (isValue()) {
			throw new IllegalStateException();
		}
		this.left = left;
		this.left.parent = this;
	}
	
	public SnailNumber getRight() {
		return right;
	}
	
	public void replaceRight(SnailNumber right) {
		if (isValue()) {
			throw new IllegalStateException();
		}
		this.right = right;
		this.right.parent = this;
	}
	
	public void replace(SnailNumber number, SnailNumber replaceWith) {
		if (isValue()) {
			throw new IllegalStateException();
		}
		if (left == number) {
			replaceLeft(replaceWith);
		} else if (right == number) {
			replaceRight(replaceWith);
		} else {
			throw new IllegalArgumentException();
		}
	}
	
	public SnailNumber getParent() {
		return parent;
	}
	
	public long magnitude() {
		if (isValue()) {
			return value;
		} else {
			return 3 * left.magnitude() + 2 * right.magnitude();
		}
	}
	
	@Override
	public String toString() {
		if (isValue()) {
			return String.valueOf(value);
		} else {
			return "[" + left + "," + right + "]";
		}
	}
	
}

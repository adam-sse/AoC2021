package day23;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import util.Point;

public class AmphipodSorting1 {
	
//	private static final int POS_H1 = 0;
//	private static final int POS_H2 = 1;
//	private static final int POS_H3 = 2;
//	private static final int POS_H4 = 3;
//	private static final int POS_H5 = 4;
//	private static final int POS_H6 = 5;
//	private static final int POS_H7 = 6;
	
	private static final int POS_RT[] = {7, 9, 11, 13};
	private static final int POS_RB[] = {8, 10, 12, 14};
	
	private static final int POS_R1T = 7;
	private static final int POS_R1B = 8;
	private static final int POS_R2T = 9;
	private static final int POS_R2B = 10;
	private static final int POS_R3T = 11;
	private static final int POS_R3B = 12;
	private static final int POS_R4T = 13;
	private static final int POS_R4B = 14;
	
	private static final Point[] COORDS = {
			new Point(1, 1),
			new Point(2, 1),
			new Point(4, 1),
			new Point(6, 1),
			new Point(8, 1),
			new Point(10, 1),
			new Point(11, 1),
			
			new Point(3, 2),
			new Point(3, 3),
			new Point(5, 2),
			new Point(5, 3),
			new Point(7, 2),
			new Point(7, 3),
			new Point(9, 2),
			new Point(9, 3),
	};
	
	private static final Map<Character, Integer> ENERGY = Map.of('A', 1, 'B', 10, 'C', 100, 'D', 1000);
	
	private static final Map<Character, Integer> TARGET_ROOM = Map.of('A', 0, 'B', 1, 'C', 2, 'D', 3);
	
	private static int dist(Point p1, Point p2) {
		return Math.abs(p1.x - p2.x) + Math.abs(p1.y - p2.y);
	}
	
	private static boolean isHallway(int i) {
		return i <= 6;
	}
	
	private static boolean isBottom(int i) {
		return i == 8 || i == 10 || i == 12 || i == 14;
	}
	
	private static boolean isHallwayFree(int from, int to, char[] positions) {
		int xFrom = COORDS[from].x;
		int xTo = COORDS[to].x;
		
		boolean occupied = false;
		for (int i = 0; i <= 6; i++) {
			if (i != from && COORDS[i].x >= Math.min(xFrom, xTo) && COORDS[i].x <= Math.max(xFrom, xTo)) {
				if (positions[i] != 0) {
					occupied = true;
					break;
				}
			}
		}
		
		return !occupied; 
	}
	
	private static boolean finished(char[] positions) {
		return
				positions[POS_R1T] == 'A' && positions[POS_R1B] == 'A' &&
				positions[POS_R2T] == 'B' && positions[POS_R2B] == 'B' &&
				positions[POS_R3T] == 'C' && positions[POS_R3B] == 'C' &&
				positions[POS_R4T] == 'D' && positions[POS_R4B] == 'D';
				
	}
	
	private static char[] copy(char[] positions) {
		char[] cpy = new char[positions.length];
		System.arraycopy(positions, 0, cpy, 0, positions.length);
		return cpy;
	}
	
	private static Long move(char[] positions, Long cost, int from, char amphipod, int target) {
		int move = dist(COORDS[from], COORDS[target]) * ENERGY.get(amphipod);
		
		char[] cpy = copy(positions);
		cpy[from] = 0;
		cpy[target] = amphipod;
		Long recursive = solve(cpy);
		
		if (recursive != null) {
			if (cost == null) {
				cost = recursive + move;
			} else if (cost != null) {
				cost = Math.min(cost, move + recursive);
			}
		}
		
		return cost;
	}
	
//	private static void print(char[] positions) {
//		Function<Character, Character> getCharacter = c -> c != 0 ? c : '.';
//		
//		Object[] chars = new Object[positions.length];
//		int i;
//		for (i = 0; i <= 6; i++) {
//			chars[i] = getCharacter.apply(positions[i]);
//		}
//		for (int j : new int[] {7, 9, 11, 13, 8, 10, 12, 14}) {
//			chars[i++] = getCharacter.apply(positions[j]);
//		}
//		
//		System.out.println(String.format("#############\n#%s%s.%s.%s.%s.%s%s#\n###%s#%s#%s#%s###\n  #%s#%s#%s#%s#\n  #########", chars));
//	}
	
	private static Map<Integer, Long> DP = new HashMap<>(10000);
	
	private static Long solve(char[] positions) {
		if (finished(positions)) {
			return 0L;
		}
		
		int hash = Arrays.hashCode(positions);
		if (DP.containsKey(hash)) {
			return DP.get(hash);
		}
		
		Long cost = null;
		
		for (int i = 0; i < positions.length; i++) {
			if (positions[i] != 0) {
				char amphipod = positions[i];
				int targetTop = POS_RT[TARGET_ROOM.get(amphipod)];
				int targetBottom = POS_RB[TARGET_ROOM.get(amphipod)];
				if (isHallway(i)) {
					
					if (positions[targetTop] == 0) {
						if (positions[targetBottom] == 0) {
							// move to bottom
							if (isHallwayFree(i, targetBottom, positions)) {
								cost = move(positions, cost, i, amphipod, targetBottom);
							}
							
						} else if (positions[targetBottom] == amphipod) {
							// move to top
							if (isHallwayFree(i, targetTop, positions)) {
								cost = move(positions, cost, i, amphipod, targetTop);
							}
						}
					}
				} else if (i != targetBottom && (i != targetTop || positions[targetBottom] != amphipod)) {
					if (isBottom(i)) {
						if (positions[i - 1] == 0) {
							// move to hallway
							for (int target = 0; target <= 6; target++) {
								if (isHallwayFree(i, target, positions)) {
									cost = move(positions, cost, i, amphipod, target);
								}
							}
							
						}
					} else {
						// move to hallway
						for (int target = 0; target <= 6; target++) {
							if (isHallwayFree(i, target, positions)) {
								cost = move(positions, cost, i, amphipod, target);
							}
						}
					}
				}
			}
		}
		
		DP.put(hash, cost);
		
		return cost;
	}

	public static void main(String[] args) {
		char[] positions = new char[15];
		
		// example
//		positions[POS_R1T] = 'B';
//		positions[POS_R1B] = 'A';
//		positions[POS_R2T] = 'C';
//		positions[POS_R2B] = 'D';
//		positions[POS_R3T] = 'B';
//		positions[POS_R3B] = 'C';
//		positions[POS_R4T] = 'D';
//		positions[POS_R4B] = 'A';
		
		// input
		positions[POS_R1T] = 'D';
		positions[POS_R1B] = 'C';
		positions[POS_R2T] = 'B';
		positions[POS_R2B] = 'A';
		positions[POS_R3T] = 'C';
		positions[POS_R3B] = 'D';
		positions[POS_R4T] = 'A';
		positions[POS_R4B] = 'B';
		
		
		System.out.println(solve(positions));
	}

}

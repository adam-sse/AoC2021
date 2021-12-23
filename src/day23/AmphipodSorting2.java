package day23;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import util.Point;

public class AmphipodSorting2 {
	
//	private static final int POS_H1 = 0;
//	private static final int POS_H2 = 1;
//	private static final int POS_H3 = 2;
//	private static final int POS_H4 = 3;
//	private static final int POS_H5 = 4;
//	private static final int POS_H6 = 5;
//	private static final int POS_H7 = 6;
	
	private static final int POS_ROOM[][] = {
		{7, 8, 9, 10},
		{11, 12, 13, 14},
		{15, 16, 17, 18},
		{19, 20, 21, 22},
	};
	
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
			new Point(3, 4),
			new Point(3, 5),
			
			new Point(5, 2),
			new Point(5, 3),
			new Point(5, 4),
			new Point(5, 5),
			
			new Point(7, 2),
			new Point(7, 3),
			new Point(7, 4),
			new Point(7, 5),
			
			new Point(9, 2),
			new Point(9, 3),
			new Point(9, 4),
			new Point(9, 5),
	};
	
	private static final Map<Character, Integer> ENERGY = Map.of('A', 1, 'B', 10, 'C', 100, 'D', 1000);
	
	private static final Map<Character, Integer> TARGET_ROOM = Map.of('A', 0, 'B', 1, 'C', 2, 'D', 3);
	
	private static int dist(Point p1, Point p2) {
		return Math.abs(p1.x - p2.x) + Math.abs(p1.y - p2.y);
	}
	
	private static boolean isHallway(int i) {
		return i <= 6;
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
		
		for (int room = 0; room < 4; room++) {
			for (int space : POS_ROOM[room]) {
				if (positions[space] != 'A' + room) {
					return false;
				}
			}
		}
		
		return true;
	}
	
	private static int[] getRoom(int i) {
		for (int room = 0; room < 4; room++) {
			if (contains(i, POS_ROOM[room])) {
				return POS_ROOM[room];
			}
		}
		return null;
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
	
	private static boolean contains(int i, int[] array) {
		for (int a : array) {
			if (a == i) {
				return true;
			}
		}
		return false;
	}
	
	private static boolean otherBelow(int i, int[] room, char amphipod, char[] positions) {
		for (int space : room) {
			if (space < i) {
				continue;
			}
			if (positions[space] != amphipod) {
				return true;
			}
		}
		return false;
	}
	
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
				
				int[] targetRoomI = POS_ROOM[TARGET_ROOM.get(amphipod)];
				
				if (isHallway(i)) {
					
					int j = targetRoomI.length - 1;
					while (j > 0 && positions[targetRoomI[j]] == amphipod) {
						j--;
					}
					
					boolean aboveFree = true;
					for (int k = j - 1; k >= 0; k--) {
						if (positions[targetRoomI[k]] != 0) {
							aboveFree = false;
							break;
						}
					}
					
					if (aboveFree && positions[targetRoomI[j]] == 0) {
						if (isHallwayFree(i, targetRoomI[j], positions)) {
							cost = move(positions, cost, i, amphipod, targetRoomI[j]);
						}
					}
					
				} else if (!contains(i, targetRoomI) || otherBelow(i, targetRoomI, amphipod, positions)) {
					int[] room = getRoom(i);
					
					int j = 0;
					while (room[j] != i) {
						j++;
					}
					
					boolean aboveFree = true;
					for (int k = j - 1; k >= 0; k--) {
						if (positions[room[k]] != 0) {
							aboveFree = false;
							break;
						}
					}
					
					if (aboveFree) {
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
		char[] positions = new char[23];
		
		// example
//		positions[POS_ROOM[0][0]] = 'B';
//		positions[POS_ROOM[0][1]] = 'D';
//		positions[POS_ROOM[0][2]] = 'D';
//		positions[POS_ROOM[0][3]] = 'A';
//		
//		positions[POS_ROOM[1][0]] = 'C';
//		positions[POS_ROOM[1][1]] = 'C';
//		positions[POS_ROOM[1][2]] = 'B';
//		positions[POS_ROOM[1][3]] = 'D';
//		
//		positions[POS_ROOM[2][0]] = 'B';
//		positions[POS_ROOM[2][1]] = 'B';
//		positions[POS_ROOM[2][2]] = 'A';
//		positions[POS_ROOM[2][3]] = 'C';
//		
//		positions[POS_ROOM[3][0]] = 'D';
//		positions[POS_ROOM[3][1]] = 'A';
//		positions[POS_ROOM[3][2]] = 'C';
//		positions[POS_ROOM[3][3]] = 'A';
		
		// input
		positions[POS_ROOM[0][0]] = 'D';
		positions[POS_ROOM[0][1]] = 'D';
		positions[POS_ROOM[0][2]] = 'D';
		positions[POS_ROOM[0][3]] = 'C';
		
		positions[POS_ROOM[1][0]] = 'B';
		positions[POS_ROOM[1][1]] = 'C';
		positions[POS_ROOM[1][2]] = 'B';
		positions[POS_ROOM[1][3]] = 'A';
		
		positions[POS_ROOM[2][0]] = 'C';
		positions[POS_ROOM[2][1]] = 'B';
		positions[POS_ROOM[2][2]] = 'A';
		positions[POS_ROOM[2][3]] = 'D';
		
		positions[POS_ROOM[3][0]] = 'A';
		positions[POS_ROOM[3][1]] = 'A';
		positions[POS_ROOM[3][2]] = 'C';
		positions[POS_ROOM[3][3]] = 'B';
		
		System.out.println(solve(positions));
	}

}

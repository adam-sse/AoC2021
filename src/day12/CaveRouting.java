package day12;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class CaveRouting {

	private static class Node {
		String name;
		boolean small;
		List<Node> neighbors = new LinkedList<>();
		Node(String name) {
			this.name = name;
			this.small = Character.isLowerCase(name.charAt(0));
		}
		void addNeighbor(Node other) {
			this.neighbors.add(other);
			other.neighbors.add(this);
		}
		@Override
		public int hashCode() {
			return Objects.hash(name);
		}
		@Override
		public boolean equals(Object obj) {
			if (this == obj) {
				return true;
			}
			if (!(obj instanceof Node)) {
				return false;
			}
			Node other = (Node) obj;
			return Objects.equals(name, other.name);
		}
		@Override
		public String toString() {
			return name;
		}
	}
	
	private static void findPaths1(Node current, List<Node> currentPath, List<List<Node>> result) {
		if (current.name.equals("end")) {
			result.add(currentPath);
			
		} else {
			for (Node neighbor : current.neighbors) {
				if (neighbor.small && currentPath.contains(neighbor)) continue;
				
				List<Node> path = new LinkedList<>(currentPath);
				path.add(neighbor);
				findPaths1(neighbor, path, result);
			}
		}
	}
	
	private static int part1(Map<String, Node> nodes) {
		List<List<Node>> allPaths = new LinkedList<>();
		
		LinkedList<Node> path = new LinkedList<>();
		path.add(nodes.get("start"));
		findPaths1(nodes.get("start"), path, allPaths);
		
		return allPaths.size();
	}
	
	private static void findPaths2(Node current, List<Node> currentPath, List<List<Node>> result, boolean hasSmallCaveTwice) {
		if (current.name.equals("end")) {
			result.add(currentPath);
			
		} else {
			for (Node neighbor : current.neighbors) {
				boolean hsct = hasSmallCaveTwice;
				if (neighbor.small && currentPath.contains(neighbor)) {
					if (hasSmallCaveTwice || neighbor.name.equals("start")) {
						continue;
					} else {
						hsct = true;
					}
				}
				
				List<Node> path = new LinkedList<>(currentPath);
				path.add(neighbor);
				findPaths2(neighbor, path, result, hsct);
			}
		}
	}
	
	private static int part2(Map<String, Node> nodes) {
		List<List<Node>> allPaths = new LinkedList<>();
		
		LinkedList<Node> path = new LinkedList<>();
		path.add(nodes.get("start"));
		findPaths2(nodes.get("start"), path, allPaths, false);
		
		return allPaths.size();
	}
	
	public static void main(String[] args) throws IOException {
		Path input = Path.of("src/day12/input.txt");
		
		Map<String, Node> nodes = new HashMap<>();
		for (String line : Files.readAllLines(input)) {
			int dash = line.indexOf('-');
			String firstName = line.substring(0, dash);
			String secondName = line.substring(dash + 1);
			
			nodes.putIfAbsent(firstName, new Node(firstName));
			nodes.putIfAbsent(secondName, new Node(secondName));
			
			nodes.get(firstName).addNeighbor(nodes.get(secondName));
		}
		
		System.out.println(part1(nodes));
		System.out.println(part2(nodes));
	}
	
}

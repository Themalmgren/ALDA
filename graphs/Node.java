package alda.graphs;

import java.util.ArrayList;

public class Node {
	private String name;
	private ArrayList<Node> edges = new ArrayList<>();

	public Node(String name) {
		if (name == null || name == ("")) {
			throw new IllegalArgumentException("Namnet kan inte vara en tom sträng");
		}
		this.name = name;
	}

	public void addEdge(Node edge) {
		for (Node temp : edges) {
			if (temp.equals(edge)) {
				return;
			}
		}
		edges.add(edge);
	}

	public String getName() {
		return name;
	}

	public ArrayList<Node> getEdges() {
		return edges;
	}
}

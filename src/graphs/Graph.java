package graphs;

import java.util.*;


public class Graph<E> {
	
	private HashMap<String, HashMap<String, Integer>> adjacencyMap;
	private HashMap<String, E> dataMap;

	public Graph() {

		this.adjacencyMap = new HashMap<>();

		this.dataMap = new HashMap<>();

	}

	public void addVertex(String vertexName, E data) {

		if (adjacencyMap.containsKey(vertexName)) {

			throw new IllegalArgumentException("Already Exists");

		}

		this.adjacencyMap.put(vertexName, new HashMap<String, Integer>());

		dataMap.put(vertexName, data);

	}

	public void addDirectedEdge(String startVertexName, String endVertexName, int cost) {

		if (!adjacencyMap.containsKey(startVertexName) || !adjacencyMap.containsKey(endVertexName)) {

			throw new IllegalArgumentException("Does not Exist");

		}

		adjacencyMap.get(startVertexName).put(endVertexName, cost);

	}

	public String toString() {

		Set<String> set = dataMap.keySet();

		TreeSet<String> treeSet = new TreeSet<>(set);

		TreeMap<String, HashMap<String, Integer>> treeMap = new TreeMap<>();

		for (Map.Entry<String, HashMap<String, Integer>> entry : adjacencyMap.entrySet()) {

			treeMap.put(entry.getKey(), entry.getValue());

		}

		String answer = "Vertices: " + treeSet.toString();

		answer += "\nEdges: ";

		for (Map.Entry<String, HashMap<String, Integer>> entry : treeMap.entrySet()) {

			answer += "\nVertex(" + entry.getKey() + ")--->" + entry.getValue();

		}

		return answer;

	}

	public Map<String, Integer> getAdjacentVertices(String vertexName) {

		return adjacencyMap.get(vertexName);

	}

	public int getCost(String startVertex, String endVertex) {

		if (!adjacencyMap.containsKey(startVertex) || !adjacencyMap.containsKey(endVertex)) {

			throw new IllegalArgumentException("Does not Exist");

		}

		int cost = 0;

		cost = adjacencyMap.get(startVertex).get(endVertex);

		return cost;

	}

	public Set<String> getVertices() {

		Set<String> set = new HashSet<String>();

		for (Map.Entry<String, HashMap<String, Integer>> entry : adjacencyMap.entrySet()) {

			set.add(entry.getKey());

		}

		return set;

	}

	public E getData(String vertex) {

		if (!adjacencyMap.containsKey(vertex)) {

			throw new IllegalArgumentException("Does Not Exist");

		}

		E returner = null;

		returner = dataMap.getOrDefault(vertex, null);

		return returner;

	}

	public void doBreadthFirstSearch(String startVertexName, CallBack<E> callback) {

		if (!adjacencyMap.containsKey(startVertexName)) {

			throw new IllegalArgumentException("Does not Exist");

		}

		Set<String> visited = new HashSet<>();

		LinkedList<String> discovered = new LinkedList<>();

		discovered.add(startVertexName);

		while (!discovered.isEmpty()) {

			String saver = discovered.poll();

			if (!visited.contains(saver)) {

				visited.add(saver);

				callback.processVertex(saver, dataMap.get(saver));

				for (Map.Entry<String, Integer> entry : adjacencyMap.get(saver).entrySet()) {

					if (!visited.contains(entry.getKey())) {

						discovered.offer(entry.getKey());

					}

				}

			}

		}

	}

	public void doDepthFirstSearch(String startVertexName, CallBack<E> callback) {

		if (!adjacencyMap.containsKey(startVertexName)) {

			throw new IllegalArgumentException("Does not Exist");

		}

		Set<String> visited = new HashSet<>();

		Stack<String> discovered = new Stack<>();

		discovered.add(startVertexName);

		while (!discovered.isEmpty()) {

			String saver = discovered.pop();

			if (!visited.contains(saver)) {

				visited.add(saver);

				callback.processVertex(saver, dataMap.get(saver));

				for (Map.Entry<String, Integer> entry : adjacencyMap.get(saver).entrySet()) {

					if (!visited.contains(entry.getKey())) {

						discovered.push(entry.getKey());

					}

				}

			}

		}

	}

	public int doDijkstras(String startVertex, String endVertex, ArrayList<String> shortestPath) {

		if (!adjacencyMap.containsKey(startVertex) || !adjacencyMap.containsKey(endVertex)) {

			throw new IllegalArgumentException("Does Not Exist");

		}

		if (startVertex.equals(endVertex)) {

			shortestPath.add(startVertex);
			return 0;

		}

		Set<String> set = new HashSet<>();

		HashMap<String, String> newMap = new HashMap<>();

		HashMap<String, Integer> costMap = new HashMap<>();

		for (String s : adjacencyMap.keySet()) {

			newMap.put(s, null);
			costMap.put(s, 100000);
		}

		costMap.put(startVertex, 0);

		while (set.size() != adjacencyMap.size()) {

			String costComp = startVertex;

			for (String s : costMap.keySet()) {

				if (!set.contains(s)) {

					costComp = s;

				}

			}

			for (String s : costMap.keySet()) {

				if (!set.contains(s)) {

					if (costMap.get(s) < costMap.get(costComp)) {

						costComp = s;

					}

				}

			}

			set.add(costComp);

			for (String s : adjacencyMap.get(costComp).keySet()) {

				if (!set.contains(s)) {

					if (costMap.get(costComp) + this.getCost(costComp, s) < costMap.get(s)) {

						costMap.put(s, costMap.get(costComp) + this.getCost(costComp, s));

						newMap.put(s, costComp);

					}

				}

			}

		}

		String end = endVertex;

		if (newMap.get(end) != null) {

			while (!end.equals(startVertex)) {

				shortestPath.add(end);

				end = newMap.get(end);

			}

			shortestPath.add(startVertex);
			Collections.reverse(shortestPath);

		} else {

			shortestPath.add("None");
			return -1;

		}

		return costMap.get(endVertex);

	}

}
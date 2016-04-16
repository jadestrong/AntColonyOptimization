package edu.bean;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Graph<T> {
	private Map<T,Set<T>> adjacent = new HashMap<>();
	
	public void addEdge(T source,T target) {
		adjacent.get(source).add(target);	
	}
	
	public void addNode(T node) {
		adjacent.put(node, new HashSet<T>());
	}
}

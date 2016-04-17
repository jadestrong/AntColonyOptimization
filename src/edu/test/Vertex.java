package edu.test;

import java.util.HashSet;
import java.util.Set;

public class Vertex {
	
	private int id;
	private String name;
	private Set<String> parameters = new HashSet<>();
	private int visitedAntId = -1;//表示没有蚂蚁占领
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Set<String> getParameters() {
		return parameters;
	}
	public void setParameters(Set<String> parameters) {
		this.parameters = parameters;
	}
	public int getVisitedAntId() {
		return visitedAntId;
	}
	public void setVisitedAntId(int visitedAntId) {
		this.visitedAntId = visitedAntId;
	}
	@Override
	public String toString() {
		return "Vertex [id=" + id + ", name=" + name + ", parameters="
				+ parameters + ", visitedAntId=" + visitedAntId + "]";
	}
}

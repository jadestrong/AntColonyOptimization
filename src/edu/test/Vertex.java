package edu.test;

import java.util.HashSet;
import java.util.Set;

import edu.bean.Ant;

public class Vertex {

	private int id;
	private String name;
	private Set<String> parameters = new HashSet<>();
	private Ant visitedAnt = null;// 表示没有蚂蚁占领
	
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

	public Ant getVisitedAnt() {
		return visitedAnt;
	}

	public void setVisitedAnt(Ant visitedAnt) {
		this.visitedAnt = visitedAnt;
	}

	@Override
	public String toString() {
		return "Vertex [id=" + id + ", name=" + name + ", parameters=" + parameters + ", visitedAnt=" + visitedAnt
				+ "]";
	}
}

package edu.bean;

import java.util.HashSet;
import java.util.Set;

public class Vertex<T> {
	private int id;
	private Set<T> oIAdjacent = new HashSet<>();
	private Set<T> oOAdjacent = new HashSet<>();
	private Set<String> keywords = new HashSet<>();
	
	@Override
	public boolean equals(Object otherObject) {
		if(this == otherObject) return true;
		if(otherObject == null) return false;
		if(getClass() != otherObject.getClass()) return false;
		
		@SuppressWarnings("unchecked")
		Vertex<T> other = (Vertex<T>) otherObject;
		
		return id == other.getId();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	public Set<T> getoIAdjacent() {
		return oIAdjacent;
	}

	public void setoIAdjacent(Set<T> oIAdjacent) {
		this.oIAdjacent = oIAdjacent;
	}

	public Set<T> getoOAdjacent() {
		return oOAdjacent;
	}

	public void setoOAdjacent(Set<T> oOAdjacent) {
		this.oOAdjacent = oOAdjacent;
	}

	public Set<String> getKeywords() {
		return keywords;
	}

	public void setKeywords(Set<String> keywords) {
		this.keywords = keywords;
	}
}

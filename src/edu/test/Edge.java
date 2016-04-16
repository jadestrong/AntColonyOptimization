package edu.test;

public class Edge {
	private int source;
	private int target;
	private double weight;
	private double pheromone;
	public int getSource() {
		return source;
	}
	public void setSource(int source) {
		this.source = source;
	}
	public int getTarget() {
		return target;
	}
	public void setTarget(int target) {
		this.target = target;
	}
	public double getWeight() {
		return weight;
	}
	public void setWeight(double weight) {
		this.weight = weight;
	}
	public double getPheromone() {
		return pheromone;
	}
	public void setPheromone(double pheromone) {
		this.pheromone = pheromone;
	}
	@Override
	public String toString() {
		return "Edge [source=" + source + ", target=" + target + ", weight="
				+ weight + ", pheromone=" + pheromone + "]";
	}
}

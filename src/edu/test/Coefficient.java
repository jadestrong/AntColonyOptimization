package edu.test;

public class Coefficient {
	private double weight;
	private double pheromone;
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
		return "Coefficient [weight=" + weight + ", pheromone=" + pheromone
				+ "]";
	}	
}

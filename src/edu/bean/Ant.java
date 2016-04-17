package edu.bean;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Random;
import java.util.Set;
import java.util.Stack;

import edu.test.Coefficient;
import edu.test.Graph;
import edu.test.Vertex;

public class Ant {
	private static double pheroInspiredFactor;
	private static double expectedInspiredFactor;
	private static int nextId = 0;
	private int id = assignId();
	private Set<String> foundKeywords;
	private Set<Vertex> visitedVertexs;
	private boolean isAlreadySave = false;
	
	static {
		Properties props = new Properties();
		try (InputStream in = Files.newInputStream(Paths.get("parameters.properties"))) {
			props.load(in);
		} catch (IOException e) {
			e.printStackTrace();
		}
		pheroInspiredFactor = Double.valueOf(props.getProperty("pheroInspiredFactor"));
		expectedInspiredFactor = Double.valueOf(props.getProperty("expectedInspiredFactor"));
	}
	/**
	 * 根据当前节点找寻可以走的下一节点
	 * @param curVertex
	 * @param graph
	 * @return
	 */
	public Vertex selectNextPath(Vertex curVertex,Graph graph) {
		int curId = curVertex.getId();
		Set<Integer> nextIds = graph.getAdjacent().get(id);
		if (nextIds.size() > 0) {
			Coefficient[][] coefArray = graph.getCoefficient();
			Map<Integer,Vertex> vertexs = graph.getVertexs();
			Map<Integer,Double> everyProbability = new HashMap<>();
			double sum = 0.0;
			for (int nextId : nextIds) {
				if (!visitedVertexs.contains(vertexs.get(nextId))) {
					Coefficient coef = coefArray[curId][nextId];
					double weight = coef.getWeight();
					double pheromone = coef.getPheromone();
					double numerator = Math.pow(pheromone, pheroInspiredFactor)
							/ (Math.pow(weight, expectedInspiredFactor));
					everyProbability.put(nextId, numerator);
					sum += numerator;
				}
			}
			
			for (Map.Entry<Integer, Double> entry : everyProbability.entrySet()) {
				double probability = entry.getValue() / sum;
				entry.setValue(probability);
			}
			
			int nextVertexId = randomSelect(everyProbability);
			if (nextVertexId < 0) {
				return null;
			}
			return vertexs.get(nextVertexId);
		}
		return null;
	}
	
	private int randomSelect(Map<Integer,Double> probability) {
		double selectP = new Random(System.currentTimeMillis()).nextDouble();
		double sumSel = 0.0;
		for (Map.Entry<Integer, Double> entry : probability.entrySet()) {
			sumSel += entry.getValue();
			if (sumSel > selectP) {
				return entry.getKey();
			}
		}
		return -1;
	}
	
	public void destory() {
		
	}
	//将该蚂蚁找到的关键字及访问过的节点都并入目标蚂蚁
	public void transfer(int targetAntId,Stack<Ant> savedAnt) {
		Ant targetAnt = findTargetAnt(targetAntId, savedAnt);
		targetAnt.getFoundKeywords().addAll(this.foundKeywords);
		
	}
	
	private Ant findTargetAnt(int antId,Stack<Ant> savedAnt) {
		Iterator<Ant> iter = savedAnt.iterator();
		Ant ant = null;
		while (iter.hasNext()) {
			ant = iter.next();
			int targetId = ant.getId();
			if (antId == targetId) {
				break;
			}
		}
		return ant;
	}
	/**
	 * 
	 * @param visitedVertexs
	 * @param ant 当ant为null时，表示
	 */
	private void changeVisitedVertexInfo(Set<Vertex> visitedVertexs,Ant ant) {
		for (Vertex v : visitedVertexs) {
			
		}
	}
	
	private static int assignId() {
		int r = nextId;
		nextId++;
		return r;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Set<String> getFoundKeywords() {
		return foundKeywords;
	}

	public Set<Vertex> getVisitedVertexs() {
		return visitedVertexs;
	}

	public boolean isAlreadySave() {
		return isAlreadySave;
	}

	public void setAlreadySave(boolean isAlreadySave) {
		this.isAlreadySave = isAlreadySave;
	}
}

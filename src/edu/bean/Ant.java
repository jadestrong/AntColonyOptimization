package edu.bean;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Random;
import java.util.Set;
import java.util.Stack;
import java.util.logging.Logger;

import edu.test.Coefficient;
import edu.test.Graph;
import edu.test.Vertex;

public class Ant {
	
	private static double ALPHA;
	private static double BETA;
	private static int maxStepNum;
	private static int nextId = 0;
	private int id = assignId();
	private Set<String> foundKeywords = new HashSet<>();
	private Set<Vertex> visitedVertexs = new HashSet<>();
//	private boolean isAlreadySave = false;
	private int stepNum = 0;
	
	private static Logger logger = Logger.getLogger("edu.bean");
	
	static {
		Properties props = new Properties();
		try (InputStream in = Files.newInputStream(Paths.get("parameters.properties"))) {
			props.load(in);
		} catch (IOException e) {
			e.printStackTrace();
		}
		ALPHA = Double.valueOf(props.getProperty("ALPHA"));
		BETA = Double.valueOf(props.getProperty("BETA"));
		maxStepNum = Integer.valueOf(props.getProperty("maxStepNum"));
	}
	
	public void findPath(Vertex curVertex,Graph graph,Stack<Ant> savedAnt) {
//		logger.entering("Ant", "findPath", curVertex);
//		System.out.println("savedAnt.size:" + savedAnt.size());
//		System.out.println("Current stepNum:" + stepNum);
		//是否有选择的节点
		if (curVertex != null && this.stepNum < maxStepNum) {
			//首先获取当前蚂蚁找到关键字的数量
			int foundKeywordNum = foundKeywords.size();
			if (foundKeywordNum == 0) {
				foundKeywordNum = curVertex.getParameters().size();
			}
			//判断当前节点是否被占领
			if (curVertex.getVisitedAnt() == null) {//未被占领
				System.out.println("该节点未被占领~~~~~~~~~~~~~~~~~~~~");
				this.stepNum += 1;
				//先占领该节点
				curVertex.setVisitedAnt(this);
				//获取该节点包含的关键字并将该节点存入已访问对象集合
				foundKeywords.addAll(curVertex.getParameters());
				visitedVertexs.add(curVertex);
				//如果找到的关键字数量发生变化则表示发现新节点，如果是第一次发现的话，保存该蚂蚁
				if (foundKeywords.size() > foundKeywordNum && !savedAnt.contains(this)) {
					savedAnt.add(this);
//					this.setAlreadySave(true);
//					this.isAlreadySave = true;
				}
				//从当前节点出发从邻接表中找出下一节点，这个节点不是已访问过的，但有可能被其他蚂蚁访问过
				Vertex selectedVertex = this.selectNextPath(curVertex, graph);
				findPath(selectedVertex,graph,savedAnt);
			} else {//已经被某只蚂蚁占领
				System.out.println("该节点已被占领-----------------");
//				System.out.println("visitedAnt：" + curVertex.getVisitedAnt());
				this.transfer(curVertex.getVisitedAnt());
				this.destory(curVertex.getVisitedAnt(), savedAnt);
			}
		} else if (!savedAnt.contains(this)) {
			//若没有下一步可以走了，则表明该蚂蚁没有找到目标蚂蚁，
			//若该蚂蚁也未被保存，则就要单纯的销毁它，若保存了则不做操作
			this.destory(null, savedAnt);
		}
		logger.exiting("Ant", "findPath");
	}
	
	/**
	 * 根据当前节点找寻可以走的下一节点
	 * 
	 * @param curVertex
	 * @param graph
	 * @return
	 */
	public Vertex selectNextPath(Vertex curVertex, Graph graph) {
		int curId = curVertex.getId();
		Set<Integer> nextIds = graph.getAdjacent().get(curId);
		// 如果存在邻接点
		if (nextIds != null && nextIds.size() > 0) {
			Coefficient[][] coefArray = graph.getCoefficient();
			Map<Integer, Vertex> vertexs = graph.getVertexs();
			Map<Integer, Double> everyProbability = new HashMap<>();
			double sum = 0.0;
			for (int nextId : nextIds) {
				if (!visitedVertexs.contains(vertexs.get(nextId))) {
					Coefficient coef = coefArray[curId][nextId];
					double weight = coef.getWeight();
					double pheromone = coef.getPheromone();
					double numerator = Math.pow(pheromone, ALPHA)
							/ (Math.pow(weight, BETA));
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

	private int randomSelect(Map<Integer, Double> probability) {
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

	/**
	 * 若无目标蚂蚁则将参数设为null
	 * 
	 * @param targetAnt
	 * @param savedAnt
	 */
	public void destory(Ant targetAnt, Stack<Ant> savedAnt) {
		changeVisitedVertexInfo(this.visitedVertexs, targetAnt);
		if (savedAnt.contains(this)) {
			savedAnt.pop();
		}
	}

	/**
	 * 将该蚂蚁找到的关键字及访问过的节点都并入目标蚂蚁
	 * 
	 * @param targetAnt
	 * @param savedAnt
	 */
	public void transfer(Ant targetAnt) {
		targetAnt.getFoundKeywords().addAll(this.foundKeywords);
		targetAnt.getVisitedVertexs().addAll(this.visitedVertexs);
	}

	/**
	 * 
	 * @param visitedVertexs
	 * @param ant
	 *            当ant为null时，表示置该节点为非占领状态
	 */
	private void changeVisitedVertexInfo(Set<Vertex> visitedVertexs, Ant ant) {
		for (Vertex v : visitedVertexs) {
			v.setVisitedAnt(ant);
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

//	@Override
//	public String toString() {
//		return "Ant [id=" + id + ", foundKeywords=" + foundKeywords + ", visitedVertexs=" + visitedVertexs
//				+ ", stepNum=" + stepNum + "]";
//	}

//	public boolean isAlreadySave() {
//		return isAlreadySave;
//	}
//
//	public void setAlreadySave(boolean isAlreadySave) {
//		this.isAlreadySave = isAlreadySave;
//	}
}

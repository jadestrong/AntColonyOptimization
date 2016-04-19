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
		//�Ƿ���ѡ��Ľڵ�
		if (curVertex != null && this.stepNum < maxStepNum) {
			//���Ȼ�ȡ��ǰ�����ҵ��ؼ��ֵ�����
			int foundKeywordNum = foundKeywords.size();
			if (foundKeywordNum == 0) {
				foundKeywordNum = curVertex.getParameters().size();
			}
			//�жϵ�ǰ�ڵ��Ƿ�ռ��
			if (curVertex.getVisitedAnt() == null) {//δ��ռ��
				System.out.println("�ýڵ�δ��ռ��~~~~~~~~~~~~~~~~~~~~");
				this.stepNum += 1;
				//��ռ��ýڵ�
				curVertex.setVisitedAnt(this);
				//��ȡ�ýڵ�����Ĺؼ��ֲ����ýڵ�����ѷ��ʶ��󼯺�
				foundKeywords.addAll(curVertex.getParameters());
				visitedVertexs.add(curVertex);
				//����ҵ��Ĺؼ������������仯���ʾ�����½ڵ㣬����ǵ�һ�η��ֵĻ������������
				if (foundKeywords.size() > foundKeywordNum && !savedAnt.contains(this)) {
					savedAnt.add(this);
//					this.setAlreadySave(true);
//					this.isAlreadySave = true;
				}
				//�ӵ�ǰ�ڵ�������ڽӱ����ҳ���һ�ڵ㣬����ڵ㲻���ѷ��ʹ��ģ����п��ܱ��������Ϸ��ʹ�
				Vertex selectedVertex = this.selectNextPath(curVertex, graph);
				findPath(selectedVertex,graph,savedAnt);
			} else {//�Ѿ���ĳֻ����ռ��
				System.out.println("�ýڵ��ѱ�ռ��-----------------");
//				System.out.println("visitedAnt��" + curVertex.getVisitedAnt());
				this.transfer(curVertex.getVisitedAnt());
				this.destory(curVertex.getVisitedAnt(), savedAnt);
			}
		} else if (!savedAnt.contains(this)) {
			//��û����һ���������ˣ������������û���ҵ�Ŀ�����ϣ�
			//��������Ҳδ�����棬���Ҫ������������������������������
			this.destory(null, savedAnt);
		}
		logger.exiting("Ant", "findPath");
	}
	
	/**
	 * ���ݵ�ǰ�ڵ���Ѱ�����ߵ���һ�ڵ�
	 * 
	 * @param curVertex
	 * @param graph
	 * @return
	 */
	public Vertex selectNextPath(Vertex curVertex, Graph graph) {
		int curId = curVertex.getId();
		Set<Integer> nextIds = graph.getAdjacent().get(curId);
		// ��������ڽӵ�
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
	 * ����Ŀ�������򽫲�����Ϊnull
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
	 * ���������ҵ��Ĺؼ��ּ����ʹ��Ľڵ㶼����Ŀ������
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
	 *            ��antΪnullʱ����ʾ�øýڵ�Ϊ��ռ��״̬
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

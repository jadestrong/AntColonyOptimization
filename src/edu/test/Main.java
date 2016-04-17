package edu.test;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Queue;
import java.util.Set;

import edu.bean.Ant;
import edu.dbImpl.VertexImpl;
import edu.util.MapUtil;

public class Main {
	
	private static int maxStepNum;
	private static int antNum;
	private static int maxIterNum;
	static {
		Properties props = new Properties();
		try (InputStream in = Files.newInputStream(Paths.get("parameters.properties"))) {
			props.load(in);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		maxStepNum = Integer.valueOf(props.getProperty("maxStepNum"));
		antNum = Integer.valueOf(props.getProperty("antNum"));
		maxIterNum = Integer.valueOf(props.getProperty("maxIterNum"));
	}
	
	public static void main(String[] args) {
		//��ȡ�ļ��е�����ؼ��ֺ������ؼ��֣���������
		Set<String> reqKeywords = null;
		Set<String> conKeywords = null;
		//��������ؼ��ּ��ϴ����ݿ����ҳ�����ӵ�иùؼ��ֵĽڵ�id����
		Map<String,Set<Integer>> reqMap = VertexImpl.getKeywordIds(reqKeywords);
		//���������ؼ��֡�����
		Map<String,Set<Integer>> conMap = VertexImpl.getKeywordIds(conKeywords);
		//�����йؼ�����ڵ�idsӳ�伯�Ϲ���Graph����
		Map<String,Set<Integer>> allMap = new HashMap<>();
		allMap.putAll(reqMap);
		allMap.putAll(conMap);
		//������ӳ�伯��������ȡ��һ���ڵ㣬��Graph�еõ���Ӧ�Ķ��󣬴���list
		Graph graph = new Graph(allMap);
		
		for (int i = 0;i < maxIterNum;i++) {
			//��reqMap��һ��ȡ��һ��id
			List<Integer> randIds = MapUtil.getRandomId(reqMap);
			//��randIds����graph�У���ͼ��ȡ����Ӧ�Ľڵ����
			List<Vertex> randVertexs = graph.getCorrespondVertex(randIds);
			//��ʼ�����϶������δ���Queue
			Queue<Ant> antQueue = new LinkedList<>();
			for (int j = 1;j <= antNum;j++) {
				Ant ant = new Ant();
				ant.setId(j);
				antQueue.add(ant);
			}
			//Ant Queue���գ��ͳ����������
			/**
			*forѭ��list��ÿ��ȡ��һ���ڵ㣬���ж��Ƿ��ѱ�����ռ�죬���û�У�����ؼ��ִ���ant����
			*��ʱҪ��¼�ؼ������������ؼ����ٴ�����ʱ����־���ҵ����µĽڵ㣻����ѱ�ռ�죬��ִ�����ٲ�����
			*����ҵ��½ڵ㣬��Ҫ���ýڵ����Stack�У�����isAlreadySave����Ϊtrue
			*��ִ�����ٲ���ʱ���ж���isAlreadySave����Ϊtrue�����ջ�е���ջ��Ԫ�أ����������ҵ��Ĺؼ��ֺͷ���
			*���Ľڵ㶼����ռ���Ǹ��ڵ�����ϵĶ����С���Ϊfalse����Ҫ�����ռ���Ԫ�صı�־��
			*/
			while (!antQueue.isEmpty()) {
				Queue<Ant> savedAnt = new LinkedList<>();
				for (Vertex v : randVertexs) {
					Ant ant = antQueue.poll();
					//�˴�Ӧ���Ǵӵ�ǰ�ڵ������Ѱ��һ�ڵ�
//					ant.getFoundKeywords().addAll(v.getParameters());
				}
			}
		}
	}
	
	
	public void findPath(Ant ant,Vertex curVertex,Graph graph,Queue<Ant> savedAnt) {
		//�Ƿ���ѡ��Ľڵ�
		if (curVertex != null) {
			//���Ȼ�ȡ��ǰ�����ҵ��ؼ��ֵ�����
			int foundKeywordNum = ant.getFoundKeywords().size();
			if (foundKeywordNum == 0) {
				foundKeywordNum = curVertex.getParameters().size();
			}
			//�жϵ�ǰ�ڵ��Ƿ�ռ��
			if (curVertex.getVisitedAntId() >= 0) {//δ��ռ��
				ant.getFoundKeywords().addAll(curVertex.getParameters());
				ant.getVisitedVertexs().add(curVertex);
				int nowKeywordNum = ant.getFoundKeywords().size();
				if (nowKeywordNum > foundKeywordNum && !ant.isAlreadySave()) {
					savedAnt.add(ant);
				}
				//�ӵ�ǰ�ڵ�������ڽӱ����ҳ���һ�ڵ�
				Vertex selectedVertex = ant.selectNextPath(curVertex, graph);
				findPath(ant,selectedVertex,graph,savedAnt);
			} else {
				
			}
		} else {//����Ҫִ�����ٲ������жϸ������Ƿ񱻴洢��
			if (!ant.isAlreadySave()) {
				//���δ���洢����Ҫ��ո����ϵ�ռ����Ϣ
				
			}
		}
	}
	
	
	/**
	 * ÿ��listѭ����ϣ�����Ҫִ��Stack�ĳ�ջ������ֱ�����꣬�����жϸ������Ƿ��ҵ������еĹؼ��֣�����ȫ�ˣ���
	 * ִ����Ϣ�ظ��²�����Ȼ������˹̹���������Ҫִ�����ٲ���
	 */
	
	
}

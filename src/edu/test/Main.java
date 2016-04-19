package edu.test;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;
import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

import edu.bean.Ant;
import edu.dbImpl.VertexImpl;
import edu.util.MapUtil;

public class Main {
	
	private static int maxStepNum;
	private static int antNumPerGroup;
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
		antNumPerGroup = Integer.valueOf(props.getProperty("antNumPerGroup"));
		maxIterNum = Integer.valueOf(props.getProperty("maxIterNum"));
	}
	
	public static void main(String[] args) {
		if (System.getProperty("java.tuil.logging.config.class") == null
				&& System.getProperty("java.util.logging.config.file") == null) {
//			try {
				Logger.getLogger("edu").setLevel(Level.INFO);
//				final int LOG_ROTATION_COUNT = 10;
//				Handler handler = new FileHandler("%h/Main.log",0,LOG_ROTATION_COUNT);
				Handler handler = new ConsoleHandler();
				Logger.getLogger("edu").addHandler(handler);
//			} catch (IOException e) {
//				Logger.getLogger("edu").log(Level.SEVERE, "Can't create log file handler", e);
//			}
		}
		//��ȡ�ļ��е�����ؼ��ֺ������ؼ��֣���������
		Set<String> reqKeywords = new HashSet<>();
		Set<String> conKeywords = new HashSet<>();
		reqKeywords.add("retailPrice");
		reqKeywords.add("accId");
		conKeywords.add("city");
		conKeywords.add("sid");
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
		int allKeywordNum = allMap.size();
		int antNum = reqMap.size() * antNumPerGroup;
		List<Ant> result = new ArrayList<>();
		for (int i = 0;i < maxIterNum;i++) {
			System.out.println("��" + i + "�ε�����");
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
				Stack<Ant> savedAnt = new Stack<>();
				for (Vertex v : randVertexs) {
					Ant ant = antQueue.poll();
					//�˴�Ӧ���Ǵӵ�ǰ�ڵ������Ѱ��һ�ڵ�
					ant.findPath(v, graph, savedAnt);
				}
				//ִ����һ�ε�����ÿ�������ж��ɳ���һֻ���Ͻ�����Ѱ·����ʱ���Խ��г�ջ��������֤���
				while (!savedAnt.isEmpty()) {
					Ant ant = savedAnt.pop();
//					ant.setAlreadySave(false);
					if (ant.getFoundKeywords().size() == allKeywordNum) {
						System.out.println("���ҵ���" + ant.getId());
//						System.out.println(ant);
						result.add(ant);
						//������Ϣ��
						
					}
					ant.destory(null, savedAnt);
				}
				savedAnt = null;
			}
		}
		System.out.println("�ҵ��Ľ��������" + result.size());
	}
	
	
	
	
	
	/**
	 * ÿ��listѭ����ϣ�����Ҫִ��Stack�ĳ�ջ������ֱ�����꣬�����жϸ������Ƿ��ҵ������еĹؼ��֣�����ȫ�ˣ���
	 * ִ����Ϣ�ظ��²�����Ȼ������˹̹���������Ҫִ�����ٲ���
	 */
	
	
}

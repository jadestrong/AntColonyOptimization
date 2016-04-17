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
		//读取文件中的请求关键字和条件关键字，两个集合
		Set<String> reqKeywords = null;
		Set<String> conKeywords = null;
		//根据请求关键字集合从数据库中找出所有拥有该关键字的节点id集合
		Map<String,Set<Integer>> reqMap = VertexImpl.getKeywordIds(reqKeywords);
		//根据条件关键字。。。
		Map<String,Set<Integer>> conMap = VertexImpl.getKeywordIds(conKeywords);
		//用所有关键字与节点ids映射集合构造Graph对象
		Map<String,Set<Integer>> allMap = new HashMap<>();
		allMap.putAll(reqMap);
		allMap.putAll(conMap);
		//从请求映射集合中依次取出一个节点，从Graph中得到对应的对象，存入list
		Graph graph = new Graph(allMap);
		
		for (int i = 0;i < maxIterNum;i++) {
			//从reqMap中一次取出一个id
			List<Integer> randIds = MapUtil.getRandomId(reqMap);
			//将randIds传入graph中，从图中取出对应的节点对象
			List<Vertex> randVertexs = graph.getCorrespondVertex(randIds);
			//初始化蚂蚁对象，依次存入Queue
			Queue<Ant> antQueue = new LinkedList<>();
			for (int j = 1;j <= antNum;j++) {
				Ant ant = new Ant();
				ant.setId(j);
				antQueue.add(ant);
			}
			//Ant Queue不空，就持续下面操作
			/**
			*for循环list，每次取出一个节点，就判断是否已被蚂蚁占领，如果没有，将其关键字存入ant对象，
			*此时要记录关键字数量，当关键字再次增长时，标志着找到了新的节点；如果已被占领，则执行销毁操作。
			*如果找到新节点，则要将该节点存入Stack中，设置isAlreadySave属性为true
			*当执行销毁操作时，判断其isAlreadySave，若为true，则从栈中弹出栈首元素，并将其所找到的关键字和访问
			*过的节点都并入占领那个节点的蚂蚁的对象中。若为false，则要清空其占领的元素的标志。
			*/
			while (!antQueue.isEmpty()) {
				Queue<Ant> savedAnt = new LinkedList<>();
				for (Vertex v : randVertexs) {
					Ant ant = antQueue.poll();
					//此处应该是从当前节点出发找寻下一节点
//					ant.getFoundKeywords().addAll(v.getParameters());
				}
			}
		}
	}
	
	
	public void findPath(Ant ant,Vertex curVertex,Graph graph,Queue<Ant> savedAnt) {
		//是否有选择的节点
		if (curVertex != null) {
			//首先获取当前蚂蚁找到关键字的数量
			int foundKeywordNum = ant.getFoundKeywords().size();
			if (foundKeywordNum == 0) {
				foundKeywordNum = curVertex.getParameters().size();
			}
			//判断当前节点是否被占领
			if (curVertex.getVisitedAntId() >= 0) {//未被占领
				ant.getFoundKeywords().addAll(curVertex.getParameters());
				ant.getVisitedVertexs().add(curVertex);
				int nowKeywordNum = ant.getFoundKeywords().size();
				if (nowKeywordNum > foundKeywordNum && !ant.isAlreadySave()) {
					savedAnt.add(ant);
				}
				//从当前节点出发从邻接表中找出下一节点
				Vertex selectedVertex = ant.selectNextPath(curVertex, graph);
				findPath(ant,selectedVertex,graph,savedAnt);
			} else {
				
			}
		} else {//这里要执行销毁操作，判断该蚂蚁是否被存储了
			if (!ant.isAlreadySave()) {
				//如果未被存储。则要清空该蚂蚁的占领信息
				
			}
		}
	}
	
	
	/**
	 * 每当list循环完毕，就需要执行Stack的出栈操作，直至出完，依次判断该蚂蚁是否找到了所有的关键字，若找全了，则
	 * 执行信息素更新操作，然后生成斯坦纳树，最后都要执行销毁操作
	 */
	
	
}

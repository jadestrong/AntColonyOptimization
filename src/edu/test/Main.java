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
		//读取文件中的请求关键字和条件关键字，两个集合
		Set<String> reqKeywords = new HashSet<>();
		Set<String> conKeywords = new HashSet<>();
		reqKeywords.add("retailPrice");
		reqKeywords.add("accId");
		conKeywords.add("city");
		conKeywords.add("sid");
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
		int allKeywordNum = allMap.size();
		int antNum = reqMap.size() * antNumPerGroup;
		List<Ant> result = new ArrayList<>();
		for (int i = 0;i < maxIterNum;i++) {
			System.out.println("第" + i + "次迭代。");
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
				Stack<Ant> savedAnt = new Stack<>();
				for (Vertex v : randVertexs) {
					Ant ant = antQueue.poll();
					//此处应该是从当前节点出发找寻下一节点
					ant.findPath(v, graph, savedAnt);
				}
				//执行完一次迭代，每个集合中都派出了一只蚂蚁进行了寻路，此时可以进行出栈操作，验证结果
				while (!savedAnt.isEmpty()) {
					Ant ant = savedAnt.pop();
//					ant.setAlreadySave(false);
					if (ant.getFoundKeywords().size() == allKeywordNum) {
						System.out.println("我找到了" + ant.getId());
//						System.out.println(ant);
						result.add(ant);
						//更新信息素
						
					}
					ant.destory(null, savedAnt);
				}
				savedAnt = null;
			}
		}
		System.out.println("找到的结果数量：" + result.size());
	}
	
	
	
	
	
	/**
	 * 每当list循环完毕，就需要执行Stack的出栈操作，直至出完，依次判断该蚂蚁是否找到了所有的关键字，若找全了，则
	 * 执行信息素更新操作，然后生成斯坦纳树，最后都要执行销毁操作
	 */
	
	
}

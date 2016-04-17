package edu.test;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.util.BaseDAO;

public class Graph {
	
	private Map<Integer,Vertex> vertexs = new HashMap<>();
	private Map<Integer,Set<Integer>> adjacent = new HashMap<>();
	private Coefficient[][] coefficient;
	
	/**
	 * @param keywordIds 关键字及其节点集合
	 */
	public Graph(Map<String,Set<Integer>> keywordIds) {
		super();
		this.vertexs = getServiceNode();
		setParameter(vertexs, keywordIds);
		List<Edge> edges = getAllEdges();
		int edgeNum = edges.size();
		coefficient = new Coefficient[edgeNum][edgeNum];
		generate(coefficient,edges);
	}
	/**
	 * 生成图的邻接表，并构建权重及信息素对象数组
	 * @param coe 信息素对象数据，其中有对应边的权重及信息素大小
	 * @param edges 从数据库中取得所有边，source->target，用于构建邻接表
	 */
	private void generate(Coefficient[][] coe,List<Edge> edges) {
		for (Edge e : edges) {
			int source = e.getSource();
			int target = e.getTarget();
			double weight = e.getWeight();
			Set<Integer> adj = new HashSet<>();
			if (adjacent.containsKey(source)) {
				adj = adjacent.get(source);
			} else {
				adjacent.put(source, adj);
			}
			adj.add(target);
			
			double initPheromone = 0.1;
			Coefficient c = new Coefficient();
			c.setPheromone(initPheromone);
			c.setWeight(weight);
			coefficient[source][target] = c;
		}
	}
	/**
	 * 从数据库取得所有的服务节点（ID及Name）
	 * @return Map<Integer,Vertex>类型，节点的ID对应其对象，这里生成的对象非常重要，
	 * 		后面的操作都是基于这里的对象
	 */
	private Map<Integer,Vertex> getServiceNode() {
		String getSQL = "select Id,Name from Service";
		BaseDAO dao = new BaseDAO();
		Map<Integer,Vertex> vertexMap = new HashMap<>();
		try {
			ResultSet rs = dao.executeQuery(getSQL, new Object[]{});
			while(rs.next()){
				Vertex v = new Vertex();
				v.setId(rs.getInt("Id"));
				v.setName(rs.getString("Name"));
				vertexMap.put(v.getId(), v);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			dao.close();
		}
		return vertexMap;
	}
	/**
	 * 为节点对象设置其参数信息，将对应的关键字存入相应的对象
	 * @param vertexs 图中的节点map
	 * @param keywordIds 这是一个键值对结构的map对象，其中键是关键字，值是拥有该关键字的节点的id集合
	 */
	public void setParameter(Map<Integer,Vertex> vertexs,Map<String,Set<Integer>> keywordIds) {
		for (Map.Entry<String, Set<Integer>> entry : keywordIds.entrySet()) {
			String keyword = entry.getKey();
			Set<Integer> ids = entry.getValue();
			for (int id : ids) {
				Vertex v = vertexs.get(id);
				v.getParameters().add(keyword);
			}
		}
	}
	/**
	 * 获取图中的所有的边，这里对边做了过滤，对于重复的边只取权重小的
	 * @return
	 */
	public List<Edge> getAllEdges() {
		String getSQL = "select distinct Source,Target,Min(Weight) as Weight from Relation Group By Source,Target";
		BaseDAO dao = new BaseDAO();
		List<Edge> list = new ArrayList<Edge>();
		try {
			ResultSet rs = dao.executeQuery(getSQL, new Object[]{});
			while(rs.next()){
				Edge e = new Edge();
				e.setSource(rs.getInt("Source"));
				e.setTarget(rs.getInt("Target"));
				e.setWeight(rs.getDouble("Weight"));
				list.add(e);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			dao.close();
		}
		return list;
	}
	/**
	 * 根据传入的随机节点Id，从图中找出对应的节点对象
	 * @param randIds
	 * @return
	 */
	public List<Vertex> getCorrespondVertex(List<Integer> randIds) {
		List<Vertex> randVertexs = new ArrayList<>();
		for (int id : randIds) {
			Vertex v = vertexs.get(id);
			randVertexs.add(v);
		}
		return randVertexs;
	}
	
	public Vertex getVertex(int id) {
		return vertexs.get(id);
	}

	public Map<Integer, Vertex> getVertexs() {
		return vertexs;
	}

	public void setVertexs(Map<Integer, Vertex> vertexs) {
		this.vertexs = vertexs;
	}

	public Map<Integer, Set<Integer>> getAdjacent() {
		return adjacent;
	}

	public void setAdjacent(Map<Integer, Set<Integer>> adjacent) {
		this.adjacent = adjacent;
	}

	public Coefficient[][] getCoefficient() {
		return coefficient;
	}

	public void setCoefficient(Coefficient[][] coefficient) {
		this.coefficient = coefficient;
	}

	@Override
	public String toString() {
		return "Graph [vertexs=" + vertexs + ", adjacent=" + adjacent
				+ ", coefficient=" + Arrays.toString(coefficient) + "]";
	}
}

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
	 * @param keywordIds �ؼ��ּ���ڵ㼯��
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
	 * ����ͼ���ڽӱ�������Ȩ�ؼ���Ϣ�ض�������
	 * @param coe ��Ϣ�ض������ݣ������ж�Ӧ�ߵ�Ȩ�ؼ���Ϣ�ش�С
	 * @param edges �����ݿ���ȡ�����бߣ�source->target�����ڹ����ڽӱ�
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
	 * �����ݿ�ȡ�����еķ���ڵ㣨ID��Name��
	 * @return Map<Integer,Vertex>���ͣ��ڵ��ID��Ӧ������������ɵĶ���ǳ���Ҫ��
	 * 		����Ĳ������ǻ�������Ķ���
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
	 * Ϊ�ڵ���������������Ϣ������Ӧ�Ĺؼ��ִ�����Ӧ�Ķ���
	 * @param vertexs ͼ�еĽڵ�map
	 * @param keywordIds ����һ����ֵ�Խṹ��map�������м��ǹؼ��֣�ֵ��ӵ�иùؼ��ֵĽڵ��id����
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
	 * ��ȡͼ�е����еıߣ�����Ա����˹��ˣ������ظ��ı�ֻȡȨ��С��
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
	 * ���ݴ��������ڵ�Id����ͼ���ҳ���Ӧ�Ľڵ����
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

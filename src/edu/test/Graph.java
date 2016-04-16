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
	public Graph() {
		Set<Vertex> vertexs = getServiceNode();
		this.vertexs = setParameter(vertexs);
		List<Edge> edges = getAllEdges();
		int edgeNum = edges.size();
		coefficient = new Coefficient[edgeNum][edgeNum];
		generate(coefficient,edges);
	}
	
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
	
	private Set<Vertex> getServiceNode() {
		String getSQL = "select Id,Name from Service";
		BaseDAO dao = new BaseDAO();
		Set<Vertex> vertexs = new HashSet<>();
		try {
			ResultSet rs = dao.executeQuery(getSQL, new Object[]{});
			while(rs.next()){
				Vertex v = new Vertex();
				v.setId(rs.getInt("Id"));
				v.setName(rs.getString("Name"));
				vertexs.add(v);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			dao.close();
		}
		return vertexs;
	}
	
	public Map<Integer,Vertex> setParameter(Set<Vertex> vertexs) {
		String getSQL = "select Name from Parameter where Id = ?";
		Map<Integer,Vertex> vertexsMap = new HashMap<>();
		for (Vertex v : vertexs) {
			BaseDAO dao = new BaseDAO();
			try {
				int id = v.getId();
				ResultSet rs = dao.executeQuery(getSQL, new Object[]{
						Integer.valueOf(id)
				});
				Set<String> parameters = new HashSet<>();
				while(rs.next()){
					parameters.add(rs.getString("Name"));
				}
				v.setParameters(parameters);
				vertexsMap.put(id, v);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				dao.close();
			}
		}
		return vertexsMap;
	}
	
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

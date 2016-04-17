package edu.dbImpl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import edu.util.BaseDAO;

public class VertexImpl {
	/**
	 * 根据关键字找出所有拥有该关键字的节点ID
	 * @param keywords 关键字集合，包括请求关键字和条件关键字
	 * @return
	 */
	public static Map<String,Set<Integer>> getKeywordIds(Set<String> keywords) {
		String getSQL = "select s.Id from Service s,Parameter p where p.Name = ? and p.Id = s.Id";
		Map<String,Set<Integer>> keywordIds = new HashMap<>();
		for (String keyword : keywords) {
			BaseDAO dao = new BaseDAO();
			try {
				Set<Integer> s = new HashSet<>();
				ResultSet rs = dao.executeQuery(getSQL, new Object[]{keyword});
				while (rs.next()) {
					s.add(rs.getInt("Id"));
				}
				keywordIds.put(keyword, s);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				dao.close();
			}
		}
		return keywordIds;
	}
}

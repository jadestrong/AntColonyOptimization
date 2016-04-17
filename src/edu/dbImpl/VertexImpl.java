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
	 * ���ݹؼ����ҳ�����ӵ�иùؼ��ֵĽڵ�ID
	 * @param keywords �ؼ��ּ��ϣ���������ؼ��ֺ������ؼ���
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

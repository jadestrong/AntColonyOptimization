package edu.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public class MapUtil {
	/**
	 * ��ÿ������ؼ��ֵĽڵ㼯�������ȡ��һ���ڵ��ID
	 * @param reqMap
	 * @return
	 */
	public static List<Integer> getRandomId(Map<String,Set<Integer>> reqMap) {
		List<Integer> randIds = new ArrayList<>();
		for (Map.Entry<String, Set<Integer>> entry : reqMap.entrySet()) {
			Set<Integer> set = entry.getValue();
			List<Integer> list = new ArrayList<>(set);
			Random rand = new Random();
			int randIndex = rand.nextInt(set.size());
			int randId = list.get(randIndex);
			randIds.add(randId);
		}
		return randIds;
	}
}

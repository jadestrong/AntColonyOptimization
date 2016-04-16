package edu.util;

import java.util.HashSet;
import java.util.Set;

public class SetHelper {
	public static <T> Set<T> getIntersection(Set<T> s1,Set<T> s2) {
		Set<T> result = new HashSet<>(s1);
		result.retainAll(s2);
		return result;
	}
}

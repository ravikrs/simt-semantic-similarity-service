package de.rwth.i9.cimt.ss.util;

import java.util.HashSet;
import java.util.Set;

public class CosineSimilarity {

	public double computeSimilarity(Set<String> leftVector, Set<String> rightVector) {
		double simScore = 0.0;
		if (leftVector == null || rightVector == null) {
			return simScore;
		}
		Set<String> intersectionSet = new HashSet<>(leftVector);
		intersectionSet.retainAll(rightVector);

		return simScore;
	}

}

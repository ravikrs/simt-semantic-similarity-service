package de.rwth.i9.simt.ss.service;

import java.util.List;

public interface SimilarityRelatednessService {
	public double computeRelatedness(List<String> vector1, List<String> vector2, String algorithmName);

	public List<List<Double>> computeWordRelatedness(List<String> vector1, List<String> vector2, String algorithmName);
}

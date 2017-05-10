package de.rwth.i9.cimt.ss.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import de.rwth.i9.cimt.ss.lib.algorithm.similarity.sspace.SSTermSimilarity;

@Service("ssSimilarityService")
public class SSSimilarityService implements SimilarityRelatednessService {
	private static final Logger log = LoggerFactory.getLogger(SSSimilarityService.class);

	@Override
	public double computeVectorRelatedness(List<String> vector1, List<String> vector2, String algorithmName) {
		double relatednessScore = 0.0;
		SSTermSimilarity sm = new SSTermSimilarity();
		relatednessScore = sm.calculateLSRSimilarityMeasureWordNet(vector1, vector2);
		return relatednessScore;
	}

	@Override
	public List<List<Double>> computeWordRelatedness(List<String> vector1, List<String> vector2, String algorithmName) {
		List<List<Double>> score = new ArrayList<>();
		List<Double> rowScore;
		SSTermSimilarity sm = new SSTermSimilarity();
		for (String token1 : vector1) {
			rowScore = new ArrayList<>();
			for (String token2 : vector2) {
				rowScore.add(sm.calculateLSRSimilarityMeasureWordNet(token1, token2));
			}
			score.add(rowScore);
		}
		return score;

	}

}

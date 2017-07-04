package de.rwth.i9.simt.ss.service.vsm;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import de.rwth.i9.cimt.ss.lib.algorithm.similarity.vsm.VSMTermSimilarity;
import de.rwth.i9.cimt.ss.lib.algorithm.similarity.vsm.VSMTermSimilarity.VSMSimilarityAlgorithm;
import de.rwth.i9.cimt.ss.lib.constants.SenseInventory;
import de.rwth.i9.cimt.ss.lib.util.ScoringUtil;
import de.rwth.i9.cimt.ss.lib.util.ScoringUtil.ScoringStrategy;
import de.rwth.i9.simt.ss.service.SimilarityRelatednessService;

@Service("wktnlVSMSimilarityService")
public class WiktionaryVSMSimilarityService implements SimilarityRelatednessService {
	private static final Logger log = LoggerFactory.getLogger(WiktionaryVSMSimilarityService.class);
	private @Value("${cimt.home}") String cimtHome;

	@Override
	public double computeRelatedness(List<String> vector1, List<String> vector2, String algorithmName) {
		double averageScore = 0.0;
		VSMTermSimilarity sm = new VSMTermSimilarity(SenseInventory.WIKTIONARY,
				cimtHome + "/ESA/VectorIndexes/Wiktionary", VSMSimilarityAlgorithm.fromString(algorithmName));
		List<Double> relatednessValues = new ArrayList<>();
		for (String token1 : vector1) {
			for (String token2 : vector2) {
				relatednessValues.add(sm.calculateVSMSimilarityMeasure(token1, token2));
			}
		}
		averageScore = ScoringUtil.getScore(ScoringStrategy.AVERAGE, relatednessValues);
		return averageScore;
	}

	@Override
	public List<List<Double>> computeWordRelatedness(List<String> vector1, List<String> vector2, String algorithmName) {
		List<List<Double>> score = new ArrayList<>();
		List<Double> rowScore;
		VSMTermSimilarity sm = new VSMTermSimilarity(SenseInventory.WIKTIONARY,
				cimtHome + "/ESA/VectorIndexes/Wiktionary", VSMSimilarityAlgorithm.fromString(algorithmName));
		for (String token1 : vector1) {
			rowScore = new ArrayList<>();
			for (String token2 : vector2) {
				rowScore.add(sm.calculateVSMSimilarityMeasure(token1, token2));
			}
			score.add(rowScore);
		}
		return score;

	}

}

package de.rwth.i9.simt.ss.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import de.rwth.i9.cimt.nlp.opennlp.OpenNLPImplSpring;
import de.rwth.i9.simt.ss.model.SentenceSimilarityVector;
import de.rwth.i9.simt.ss.model.SimilarityVector;
import de.rwth.i9.simt.ss.service.SimilarityRelatednessService;

@RestController
@RequestMapping("/similarity/lsr")
public class LSRSimilarityController {

	private static final Logger log = LoggerFactory.getLogger(LSRSimilarityController.class);

	@Autowired
	SimilarityRelatednessService wnLSRSimilarityService;

	@Autowired
	SimilarityRelatednessService wktnlLSRSimilarityService;

	@Autowired
	OpenNLPImplSpring openNLPImplSpring;

	@RequestMapping(value = "/wordnet", method = RequestMethod.GET)
	public double computeTokensRelatednessScoreWordNet(@RequestParam("token1") String token1,
			@RequestParam("token2") String token2) {
		return wnLSRSimilarityService.computeRelatedness(Arrays.asList(token1), Arrays.asList(token2), "default");
	}

	@RequestMapping(value = "/wordnet", method = RequestMethod.POST)
	public double computeTokensListRelatednessScoreWordNet(@RequestBody SimilarityVector sv) {
		return wnLSRSimilarityService.computeRelatedness(sv.getVector1(), sv.getVector2(), sv.getSimilarityAlgorithm());
	}

	@RequestMapping(value = "/wordnet/sentence", method = RequestMethod.POST)
	public double computeSentenceRelatednessScoreWordNet(@RequestBody SentenceSimilarityVector ssv) {
		List<String> text1tokens = new ArrayList<>(openNLPImplSpring.getTokensFromText(ssv.getText1()));
		List<String> text2tokens = new ArrayList<>(openNLPImplSpring.getTokensFromText(ssv.getText2()));
		return wnLSRSimilarityService.computeRelatedness(text1tokens, text2tokens, ssv.getSimilarityAlgorithm());
	}

	@RequestMapping(value = "/wordnet/vector", method = RequestMethod.POST)
	public List<List<Double>> computeVectorRelatednessScoreWordNet(@RequestBody SimilarityVector sv) {
		return wnLSRSimilarityService.computeWordRelatedness(sv.getVector1(), sv.getVector2(),
				sv.getSimilarityAlgorithm());
	}

	@RequestMapping(value = "/wiktionary", method = RequestMethod.GET)
	public double computeTokensRelatednessScoreWiktionary(@RequestParam("token1") String token1,
			@RequestParam("token2") String token2) {
		return wktnlLSRSimilarityService.computeRelatedness(Arrays.asList(token1), Arrays.asList(token2), "default");
	}

	@RequestMapping(value = "/wiktionary", method = RequestMethod.POST)
	public double computeTokensListRelatednessScoreWiktionary(@RequestBody SimilarityVector sv) {
		return wktnlLSRSimilarityService.computeRelatedness(sv.getVector1(), sv.getVector2(),
				sv.getSimilarityAlgorithm());
	}

	@RequestMapping(value = "/wiktionary/sentence", method = RequestMethod.POST)
	public double computeSentenceRelatednessScoreWiktionary(@RequestBody SentenceSimilarityVector ssv) {
		List<String> text1tokens = new ArrayList<>(openNLPImplSpring.getTokensFromText(ssv.getText1()));
		List<String> text2tokens = new ArrayList<>(openNLPImplSpring.getTokensFromText(ssv.getText2()));
		return wktnlLSRSimilarityService.computeRelatedness(text1tokens, text2tokens, ssv.getSimilarityAlgorithm());
	}

	@RequestMapping(value = "/wiktionary/vector", method = RequestMethod.POST)
	public List<List<Double>> computeWordRelatednessScoreWiktionary(@RequestBody SimilarityVector sv) {
		return wktnlLSRSimilarityService.computeWordRelatedness(sv.getVector1(), sv.getVector2(),
				sv.getSimilarityAlgorithm());
	}

}

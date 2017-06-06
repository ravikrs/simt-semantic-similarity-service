package de.rwth.i9.cimt.ss.controller;

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
import de.rwth.i9.cimt.ss.model.SentenceSimilarityVector;
import de.rwth.i9.cimt.ss.model.SimilarityVector;
import de.rwth.i9.cimt.ss.service.SimilarityRelatednessService;

@RestController
@RequestMapping("/similarity/vsm")
public class VSMSimilarityController {

	private static final Logger log = LoggerFactory.getLogger(VSMSimilarityController.class);

	@Autowired
	SimilarityRelatednessService wikipediaVSMSimilarityService;
	@Autowired
	SimilarityRelatednessService wktnlVSMSimilarityService;
	@Autowired
	SimilarityRelatednessService wnVSMSimilarityService;

	@Autowired
	OpenNLPImplSpring openNLPImplSpring;

	@RequestMapping(value = "/wordnet", method = RequestMethod.GET)
	public double computeTokensRelatednessScoreWordnet(@RequestParam("token1") String token1,
			@RequestParam("token2") String token2) {
		return wnVSMSimilarityService.computeRelatedness(Arrays.asList(token1), Arrays.asList(token2), "default");
	}

	@RequestMapping(value = "/wordnet", method = RequestMethod.POST)
	public double computeTokensListRelatednessScoreWordnet(@RequestBody SimilarityVector sv) {
		return wnVSMSimilarityService.computeRelatedness(sv.getVector1(), sv.getVector2(), sv.getSimilarityAlgorithm());
	}

	@RequestMapping(value = "/wordnet/sentence", method = RequestMethod.POST)
	public double computeSentenceRelatednessScoreWordNet(@RequestBody SentenceSimilarityVector ssv) {
		List<String> text1tokens = new ArrayList<>(openNLPImplSpring.getTokensFromText(ssv.getText1()));
		List<String> text2tokens = new ArrayList<>(openNLPImplSpring.getTokensFromText(ssv.getText2()));
		return wnVSMSimilarityService.computeRelatedness(text1tokens, text2tokens, ssv.getSimilarityAlgorithm());
	}

	@RequestMapping(value = "/wordnet/vector", method = RequestMethod.POST)
	public List<List<Double>> computeWordRelatednessScoreWordnet(@RequestBody SimilarityVector sv) {
		return wnVSMSimilarityService.computeWordRelatedness(sv.getVector1(), sv.getVector2(),
				sv.getSimilarityAlgorithm());
	}

	@RequestMapping(value = "/wiktionary", method = RequestMethod.GET)
	public double computeTokensRelatednessScoreWiktionary(@RequestParam("token1") String token1,
			@RequestParam("token2") String token2) {
		return wktnlVSMSimilarityService.computeRelatedness(Arrays.asList(token1), Arrays.asList(token2), "default");
	}

	@RequestMapping(value = "/wiktionary", method = RequestMethod.POST)
	public double computeTokensListRelatednessWiktionary(@RequestBody SimilarityVector sv) {
		return wktnlVSMSimilarityService.computeRelatedness(sv.getVector1(), sv.getVector2(),
				sv.getSimilarityAlgorithm());
	}

	@RequestMapping(value = "/wiktionary/sentence", method = RequestMethod.POST)
	public double computeSentenceRelatednessScoreWiktionary(@RequestBody SentenceSimilarityVector ssv) {
		List<String> text1tokens = new ArrayList<>(openNLPImplSpring.getTokensFromText(ssv.getText1()));
		List<String> text2tokens = new ArrayList<>(openNLPImplSpring.getTokensFromText(ssv.getText2()));
		return wktnlVSMSimilarityService.computeRelatedness(text1tokens, text2tokens, ssv.getSimilarityAlgorithm());
	}

	@RequestMapping(value = "/wiktionary/vector", method = RequestMethod.POST)
	public List<List<Double>> computeWordRelatednessScoreWiktionary(@RequestBody SimilarityVector sv) {
		return wktnlVSMSimilarityService.computeWordRelatedness(sv.getVector1(), sv.getVector2(),
				sv.getSimilarityAlgorithm());
	}

	@RequestMapping(value = "/wikipedia", method = RequestMethod.GET)
	public double computeTokensRelatednessScoreWikipedia(@RequestParam("token1") String token1,
			@RequestParam("token2") String token2) {
		return wikipediaVSMSimilarityService.computeRelatedness(Arrays.asList(token1), Arrays.asList(token2),
				"default");
	}

	@RequestMapping(value = "/wikipedia", method = RequestMethod.POST)
	public double computeTokensListRelatednessScoreWikipedia(@RequestBody SimilarityVector sv) {
		return wikipediaVSMSimilarityService.computeRelatedness(sv.getVector1(), sv.getVector2(),
				sv.getSimilarityAlgorithm());
	}

	@RequestMapping(value = "/wikipedia/sentence", method = RequestMethod.POST)
	public double computeSentenceRelatednessScoreWikipedia(@RequestBody SentenceSimilarityVector ssv) {
		List<String> text1tokens = new ArrayList<>(openNLPImplSpring.getTokensFromText(ssv.getText1()));
		List<String> text2tokens = new ArrayList<>(openNLPImplSpring.getTokensFromText(ssv.getText2()));
		return wikipediaVSMSimilarityService.computeRelatedness(text1tokens, text2tokens, ssv.getSimilarityAlgorithm());
	}

	@RequestMapping(value = "/wikipedia/vector", method = RequestMethod.POST)
	public List<List<Double>> computeWordRelatednessScoreWikipedia(@RequestBody SimilarityVector sv) {
		return wikipediaVSMSimilarityService.computeWordRelatedness(sv.getVector1(), sv.getVector2(),
				sv.getSimilarityAlgorithm());
	}

}

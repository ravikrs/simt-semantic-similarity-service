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
@RequestMapping("/similarity/wikipedia")
public class WikipediaSimilarityController {

	private static final Logger log = LoggerFactory.getLogger(WikipediaSimilarityController.class);

	@Autowired
	SimilarityRelatednessService wikipediaSimilarityService;

	@Autowired
	OpenNLPImplSpring openNLPImplSpring;

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public double computeTokensRelatednessScore(@RequestParam("token1") String token1,
			@RequestParam("token2") String token2) {
		return wikipediaSimilarityService.computeRelatedness(Arrays.asList(token1), Arrays.asList(token2), "default");
	}

	@RequestMapping(value = "/", method = RequestMethod.POST)
	public double computeTokensListRelatednessScore(@RequestBody SimilarityVector sv) {
		return wikipediaSimilarityService.computeRelatedness(sv.getVector1(), sv.getVector2(),
				sv.getSimilarityAlgorithm());
	}

	@RequestMapping(value = "/sentence", method = RequestMethod.POST)
	public double computeSentenceRelatednessScore(@RequestBody SentenceSimilarityVector ssv) {
		List<String> text1tokens = new ArrayList<>(openNLPImplSpring.getTokensFromText(ssv.getText1()));
		List<String> text2tokens = new ArrayList<>(openNLPImplSpring.getTokensFromText(ssv.getText2()));
		return wikipediaSimilarityService.computeRelatedness(text1tokens, text2tokens, ssv.getSimilarityAlgorithm());
	}

	@RequestMapping(value = "/vector", method = RequestMethod.POST)
	public List<List<Double>> computeWordRelatednessScore(@RequestBody SimilarityVector sv) {
		return wikipediaSimilarityService.computeWordRelatedness(sv.getVector1(), sv.getVector2(),
				sv.getSimilarityAlgorithm());
	}

}

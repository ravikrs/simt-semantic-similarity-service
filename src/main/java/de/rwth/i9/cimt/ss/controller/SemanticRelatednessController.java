package de.rwth.i9.cimt.ss.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import de.rwth.i9.cimt.ss.model.SimilarityVector;
import de.rwth.i9.cimt.ss.service.semanticmeasure.SRWordNet;

@RestController
@RequestMapping("/measure/semantic/relatedness")
public class SemanticRelatednessController {
	private static final Logger log = LoggerFactory.getLogger(SemanticRelatednessController.class);

	@Autowired
	SRWordNet srWordNet;

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String getSemanticRelatedness() {
		return "Semantic Relatedness URL";
	}

	@RequestMapping(value = "/vector/wordnet", method = RequestMethod.POST)
	public Double getSRVectorScoreWordNet(@RequestBody SimilarityVector sv) {
		return srWordNet.computeRelatedness(sv.getVector1(), sv.getVector2(), sv.getSimilarityAlgorithm());
	}

	@RequestMapping(value = "/word/wordnet", method = RequestMethod.POST)
	public List<List<Double>> getSRWordScoreWordNet(@RequestBody SimilarityVector sv) {
		return srWordNet.computeWordRelatedness(sv.getVector1(), sv.getVector2(), sv.getSimilarityAlgorithm());

	}

}

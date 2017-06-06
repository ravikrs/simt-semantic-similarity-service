package de.rwth.i9.cimt.ss.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SentenceSimilarityVector {
	private String text1;
	private String text2;
	private String similarityAlgorithm;

	public String getSimilarityAlgorithm() {
		return similarityAlgorithm;
	}

	public String getText1() {
		return text1;
	}

	public void setText1(String text1) {
		this.text1 = text1;
	}

	public String getText2() {
		return text2;
	}

	public void setText2(String text2) {
		this.text2 = text2;
	}

	public void setSimilarityAlgorithm(String similarityAlgorithm) {
		this.similarityAlgorithm = similarityAlgorithm;
	}

}

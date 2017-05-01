package de.rwth.i9.cimt.ss.algorithm.similarity.lsr;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.tudarmstadt.ukp.dkpro.lexsemresource.Entity;
import de.tudarmstadt.ukp.dkpro.lexsemresource.Entity.PoS;
import de.tudarmstadt.ukp.dkpro.lexsemresource.core.AbstractResource;
import de.tudarmstadt.ukp.dkpro.lexsemresource.exception.LexicalSemanticResourceException;
import de.tudarmstadt.ukp.dkpro.lexsemresource.wordnet.util.WordNetEntityIterable;
import de.tudarmstadt.ukp.dkpro.lexsemresource.wordnet.util.WordNetUtils;
import net.sf.extjwnl.JWNLException;
import net.sf.extjwnl.data.IndexWord;
import net.sf.extjwnl.data.POS;
import net.sf.extjwnl.data.PointerUtils;
import net.sf.extjwnl.data.Synset;
import net.sf.extjwnl.data.Word;
import net.sf.extjwnl.data.list.PointerTargetNode;
import net.sf.extjwnl.data.list.PointerTargetNodeList;
import net.sf.extjwnl.dictionary.Dictionary;
import net.sf.extjwnl.dictionary.Dictionary.Version;

public class WordNetResource extends AbstractResource {
	private final Log logger = LogFactory.getLog(getClass());

	private static final String RESOURCE_NAME = "WordNet";

	private Dictionary dict;
	// private final PointerUtils pUtils = PointerUtils.getInstance();
	private Version v;

	private int numberOfEntities = -1;

	public WordNetResource(String wordNetPropertiesFile) throws LexicalSemanticResourceException {
		try {
			InputStream is;
			URL url = getClass().getResource(wordNetPropertiesFile);
			if (url != null) {
				is = url.openStream();
			} else {
				try {
					url = new URL(wordNetPropertiesFile);
					is = url.openStream();
				} catch (MalformedURLException e) {
					// Ignore, we try if it is a file.
					is = new FileInputStream(wordNetPropertiesFile);
				}
			}
			this.dict = Dictionary.getInstance(is);
			this.v = dict.getVersion();
			setIsCaseSensitive(isCaseSensitive); // zhu
		} catch (IOException e) {
			logger.info("Could not access WordNet properties file: " + wordNetPropertiesFile);
			throw new LexicalSemanticResourceException(e);
		} catch (JWNLException e) {
			logger.info("JWNL exception while initializing reader.");
			throw new LexicalSemanticResourceException(e);
		}
	}

	@Override
	public boolean containsLexeme(String lexeme) throws LexicalSemanticResourceException {
		if (lexeme == null) {
			return false;
		}
		try {
			// zhu case sensitivity
			IndexWord[] indexWords = dict.lookupAllIndexWords(lexeme).getIndexWordArray();

			for (IndexWord item : indexWords) {
				// TODO how to do if the lookupAllIndexWords returns a stemmed
				// form?
				if (isCaseSensitive) {
					if (item.getLemma().equals(lexeme)) {
						return true;
					}
				} else {
					if (item.getLemma().equalsIgnoreCase(lexeme)) {
						return true;
					}
				}
			}
		} catch (JWNLException e) {
			throw new LexicalSemanticResourceException(e);
		}
		return false;
	}

	@Override
	public boolean containsEntity(Entity entity) throws LexicalSemanticResourceException {
		Set<Synset> synsets = WordNetUtils.entityToSynsets(dict, entity, isCaseSensitive);
		if (synsets.size() == 0) {
			return false;
		}
		return true;
	}

	@Override
	public Set<Entity> getEntity(String lexeme) throws LexicalSemanticResourceException {
		Set<Synset> synsets = WordNetUtils.toSynset(dict, lexeme, isCaseSensitive);
		return WordNetUtils.synsetsToEntities(synsets);
	}

	@Override
	public Set<Entity> getEntity(String lexeme, PoS pos) throws LexicalSemanticResourceException {
		Set<Synset> synsets = WordNetUtils.toSynset(dict, lexeme, pos, isCaseSensitive);
		return WordNetUtils.synsetsToEntities(synsets);
	}

	// Uses offset as sense ID
	@Override
	public Set<Entity> getEntity(String lexeme, PoS pos, String sense) throws LexicalSemanticResourceException {
		Set<Entity> entities = new HashSet<Entity>();
		Entity e = WordNetUtils.getExactEntity(dict, lexeme, pos, sense, isCaseSensitive);
		if (e != null) {
			entities.add(e);
		}
		return entities;
	}

	public Set<Entity> getEntity(String lexeme, PoS pos, int sense) throws LexicalSemanticResourceException {
		Set<Entity> entities = new HashSet<Entity>();
		Entity e = WordNetUtils.getExactEntity(dict, lexeme, pos, sense, isCaseSensitive);
		if (e != null) {
			entities.add(e);
		}
		return entities;
	}

	@Override
	public Set<Entity> getParents(Entity entity) throws LexicalSemanticResourceException {
		// deliberately used a set to collect results to allow other relation
		// types to be added
		Set<Entity> parents = new HashSet<Entity>();
		parents.addAll(getRelatedEntities(entity, SemanticRelation.hypernymy));
		return parents;
	}

	// TODO is there a more efficient way?
	@Override
	public int getNumberOfEntities() throws LexicalSemanticResourceException {
		if (this.numberOfEntities < 0) {
			int i = 0;
			try {
				Iterator<Synset> adjIter = dict.getSynsetIterator(POS.ADJECTIVE);
				Iterator<Synset> advIter = dict.getSynsetIterator(POS.ADVERB);
				Iterator<Synset> nounIter = dict.getSynsetIterator(POS.NOUN);
				Iterator<Synset> verbIter = dict.getSynsetIterator(POS.VERB);

				while (adjIter.hasNext()) {
					i++;
					adjIter.next();
				}
				while (advIter.hasNext()) {
					i++;
					advIter.next();
				}
				while (nounIter.hasNext()) {
					i++;
					nounIter.next();
				}
				while (verbIter.hasNext()) {
					i++;
					verbIter.next();
				}

				numberOfEntities = i;
			} catch (JWNLException e) {
				throw new LexicalSemanticResourceException(e);
			}
		}
		return numberOfEntities;
	}

	@Override
	public Iterable<Entity> getEntities() {
		return new WordNetEntityIterable(dict);
	}

	@Override
	public Set<Entity> getChildren(Entity entity) throws LexicalSemanticResourceException {
		// deliberately used a set to collect results to allow other relation
		// types to be added
		Set<Entity> children = new HashSet<Entity>();
		children.addAll(getRelatedEntities(entity, SemanticRelation.hyponymy));
		return children;
	}

	@Override
	public String getResourceName() {
		return RESOURCE_NAME;
	}

	@Override
	public String getResourceVersion() {
		return new Double(v.getNumber()).toString();
	}

	@Override
	public int getShortestPathLength(Entity firstEntity, Entity secondEntity) {
		throw new UnsupportedOperationException();
	}

	@Override
	public String getGloss(Entity entity) throws LexicalSemanticResourceException {
		StringBuilder sb = new StringBuilder();
		Set<Synset> synsets = WordNetUtils.entityToSynsets(dict, entity, isCaseSensitive);
		for (Synset synset : synsets) {
			sb.append(synset.getGloss());
			sb.append(" ");
		}
		return sb.toString().trim();
	}

	@Override
	public Set<String> getRelatedLexemes(String lexeme, PoS pos, String sense, LexicalRelation lexicalRelation)
			throws LexicalSemanticResourceException {

		Set<String> relatedLexemes = new HashSet<String>();

		// TODO this is caused by a JWNL bug I think - test whether this is
		// still necessary with each new JWNL release
		// 5-digit sense numbers are output as part of valid synsets by JWNL,
		// but asking for that synset via getSynsetAt causes
		// NumberFormatException
		if (sense.length() < 7) {
			return relatedLexemes;
		}

		for (POS gnPos : WordNetUtils.mapPos(pos)) {
			Synset synset = null;
			try {
				try {
					if (isCaseSensitive) {
						synset = WordNetUtils.toSynset(dict, lexeme, pos, sense, isCaseSensitive);
					} else {
						synset = dict.getSynsetAt(gnPos, new Long(sense));
					}
				} catch (NullPointerException e) {
					// silently ignore the exception thrown by JWNL
					continue;
				}
				if (synset == null) {
					continue;
				}

				PointerTargetNodeList nodeList = getNodeListByRelation(synset, lexicalRelation);

				if (nodeList != null) {
					for (Object node : nodeList) {
						PointerTargetNode ptNode = (PointerTargetNode) (node);
						Synset nodeSynset = ptNode.getSynset();
						List<Word> synsetWords = nodeSynset.getWords();
						for (Word synsetWord : synsetWords) {
							if (!synsetWord.getLemma().equals(lexeme)) {
								relatedLexemes.add(synsetWord.getLemma());
							}
						}
					}
				}

				// The PointerUtils method of JWNL does not get intra synset
				// lexemes as synonyms. Thus, we have to add it manually.
				if (lexicalRelation.equals(LexicalRelation.synonymy)) {
					// add the other lexemes from the synset
					List<Word> directSynonymWords = synset.getWords();
					for (Word synsetWord : directSynonymWords) {
						if (!synsetWord.getLemma().equals(lexeme)) {
							relatedLexemes.add(synsetWord.getLemma());
						}
					}
				}

			} catch (NumberFormatException e) {
				throw new LexicalSemanticResourceException(e);
			} catch (JWNLException e) {
				throw new LexicalSemanticResourceException(e);
			}
		}
		return relatedLexemes;
	}

	@Override
	public Set<Entity> getRelatedEntities(Entity entity, SemanticRelation semanticRelation)
			throws LexicalSemanticResourceException {
		Set<Entity> relatedEntities = new HashSet<Entity>();
		Set<Synset> synsets = WordNetUtils.entityToSynsets(dict, entity, isCaseSensitive);

		for (Synset synset : synsets) {

			PointerTargetNodeList nodeList = null;
			try {
				nodeList = getNodeListByRelation(synset, semanticRelation);
			} catch (JWNLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			if (nodeList != null) {
				for (Object node : nodeList) {
					PointerTargetNode ptNode = (PointerTargetNode) (node);
					Synset nodeSynset = ptNode.getSynset();
					relatedEntities.add(WordNetUtils.synsetToEntity(nodeSynset));
				}
			}
		}
		return relatedEntities;
	}

	private PointerTargetNodeList getNodeListByRelation(Synset synset, SemanticRelation relationType)
			throws LexicalSemanticResourceException, JWNLException {
		try {
			if (relationType.equals(SemanticRelation.holonymy)) {
				return PointerUtils.getHolonyms(synset);
			} else if (relationType.equals(SemanticRelation.hypernymy)) {
				return PointerUtils.getDirectHypernyms(synset);
			} else if (relationType.equals(SemanticRelation.hyponymy)) {
				return PointerUtils.getDirectHyponyms(synset);
			} else if (relationType.equals(SemanticRelation.meronymy)) {
				return PointerUtils.getMeronyms(synset);
			} else if (relationType.equals(SemanticRelation.cohyponymy)) {
				return PointerUtils.getCoordinateTerms(synset);
			}
		} catch (NullPointerException e) {
			// silently catch that - I do not know why JWNL throws that
			// exception here
		}
		return null;
	}

	private PointerTargetNodeList getNodeListByRelation(Synset synset, LexicalRelation lexicalRelation)
			throws LexicalSemanticResourceException, JWNLException {
		try {
			if (lexicalRelation.equals(LexicalRelation.antonymy)) {
				return PointerUtils.getAntonyms(synset);
			} else if (lexicalRelation.equals(LexicalRelation.synonymy)) {
				return PointerUtils.getSynonyms(synset);
			}
		} catch (NullPointerException e) {
			// silently catch that - I do not know why JWNL throws that
			// exception here
		}
		return null;
	}

	/**
	 * This is a small utility to get direct access to such JWNL relations and
	 * related synsets that are not handled with the generic LexSemResource API.
	 * Queries synsets related to the Entity connected via an arbitrary relation
	 * represented by a STRING (its name). It attempts to collect a NodeList via
	 * reflection, calling get"STRING"s() function. This is the usual naming
	 * followed by JWNL for all kinds of relations.
	 */
	public Set<Entity> getRelatedEntitiesByName(Entity entity, String semanticRelation)
			throws LexicalSemanticResourceException {
		Set<Entity> relatedEntities = new HashSet<Entity>();
		Set<Synset> synsets = WordNetUtils.entityToSynsets(dict, entity, isCaseSensitive);

		for (Synset synset : synsets) {

			PointerTargetNodeList nodeList = getNodeListByRelationName(synset, semanticRelation);

			if (nodeList != null) {
				for (Object node : nodeList) {
					PointerTargetNode ptNode = (PointerTargetNode) (node);
					Synset nodeSynset = ptNode.getSynset();
					relatedEntities.add(WordNetUtils.synsetToEntity(nodeSynset));
				}
			}
		}
		return relatedEntities;
	}

	private PointerTargetNodeList getNodeListByRelationName(Synset synset, String relationType)
			throws LexicalSemanticResourceException {
		try {
			Class<? extends PointerUtils> cls = PointerUtils.class;
			Class[] params = new Class[1];
			params[0] = Synset.class;
			String methodName = "get" + relationType + "s";

			Method method = cls.getDeclaredMethod(methodName, params);
			return (PointerTargetNodeList) method.invoke(null, synset);

		} catch (Exception e) {
			// silently catch that - I do not know why JWNL throws
			// NullPointerException exception here
			// if an Exception occured, we simply return a null as the result
			// NodeList
			// e.printStackTrace();
			// throw new LexicalSemanticResourceException(e.getMessage());
		}
		return null;
	}

	@Override
	public Entity getRoot() throws LexicalSemanticResourceException {
		Map<String, String> rootLexemes = new HashMap<String, String>();
		rootLexemes.put("entity", "1740");

		try {
			return this.getEntity(rootLexemes, PoS.n);
		} catch (UnsupportedOperationException e) {
			return null;
		}
	}

	@Override
	public Entity getRoot(PoS pos) throws LexicalSemanticResourceException {
		if (pos.equals(PoS.n)) {
			return getRoot();
		} else {
			return null;
		}
	}

	@Override
	public Entity getMostFrequentEntity(String lexeme) throws LexicalSemanticResourceException {
		for (PoS pos : PoS.values()) {
			Entity e = WordNetUtils.getMostFrequentEntity(dict, lexeme, pos, isCaseSensitive);
			if (e != null) {
				return e;
			}
		}

		return null;
	}

	@Override
	public Entity getMostFrequentEntity(String lexeme, PoS pos) throws LexicalSemanticResourceException {
		return WordNetUtils.getMostFrequentEntity(dict, lexeme, pos, isCaseSensitive);
	}

	public Dictionary getDict() {
		return dict;
	}
}
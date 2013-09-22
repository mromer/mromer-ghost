package com.mromer.ghost.model;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;


public class Dictionary {
	
	private final static Logger logger = (Logger) LoggerFactory.getLogger(Dictionary.class);

	private static final String MIN_WORD_LENGTH_PROP = "MIN_WORD_LENGTH_PROP";
	
	private int MIN_WORD_LENGTH;	

	private HashMap<Character, LetterNode> words = new HashMap<Character, LetterNode>();

	/**
	 * Add a word in dictionary with length >= MIN_WORD_LENGTH.
	 * MIN_WORD_LENGTH is configured in resources/dictionary/ghost.properties
	 * */
	private void addWord(String word) {
		if (word.length() >= MIN_WORD_LENGTH) {
			LetterNode node = words.get(Character.valueOf(word.charAt(0)));

			if (node == null)
				words.put(Character.valueOf(word.charAt(0)),new LetterNode(word));
			else
				node.addWord(word);
		}
	}

	/**
	 * Load dictionary from resources/dictionary/word.lst
	 * */
	public Dictionary() {
		
		Resource resourceProperties = new ClassPathResource("/dictionary/ghost.properties");		
		Properties props;
		try {
			
			props = PropertiesLoaderUtils.loadProperties(resourceProperties);
			MIN_WORD_LENGTH = Integer.parseInt(props.getProperty(MIN_WORD_LENGTH_PROP));
			
		} catch (IOException e) {			
			logger.error(e.getMessage());
		}
		

		Resource resourceDiccionary = new ClassPathResource("/dictionary/word.lst");
		
		try	{
			BufferedReader reader = new BufferedReader(new FileReader(resourceDiccionary.getFile()));
			String word = null;
			while ((word = reader.readLine()) != null)
				addWord(word);
			
			reader.close();
		} catch (FileNotFoundException fileNotFoundException) {
			logger.error("File " + resourceDiccionary.getFilename() + " not found.");			
			
		} catch (IOException ioException) {
			logger.error("Error reading file:  " + ioException.toString());
			
		}
	}

	/**
	 * Number of words in dictionary.
	 * */
	public int size() {
		int count = 0;

		for (LetterNode node : words.values()) {
			count += node.leafNodeCount();
		}			

		return count;
	}

	public HashMap<Character, LetterNode> getWords() {
		return words;
	}

	public void setWords(HashMap<Character, LetterNode> words) {
		this.words = words;
	}
	
}

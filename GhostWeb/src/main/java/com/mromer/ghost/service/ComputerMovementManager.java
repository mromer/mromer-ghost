package com.mromer.ghost.service;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.TreeSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mromer.ghost.model.Dictionary;
import com.mromer.ghost.model.LetterNode;
import com.mromer.ghost.model.Movement;



public class ComputerMovementManager {

	private final static Logger logger = (Logger) LoggerFactory.getLogger(ComputerMovementManager.class);	

	
	/**
	 * Calculate the next movement that the computer will do.
	 * 
	 * @param dictionary
	 * @param actualWord
	 * @return next movement
	 * */
	public Movement nextMovement(Dictionary dictionary, Movement movementHuman) {		
		
		Movement movementResult =  new Movement();
		
		String actualWord = movementHuman.getWord();
		
		
		LetterNode rootNode = getLetterNode(dictionary.getWords().get(actualWord.charAt(0)), actualWord);
		
		if (rootNode == null || rootNode.getNodes().size() == 0) {
			// There aren´t movements. Machine wins
			movementResult.setWord(actualWord);
			movementResult.setResult(2);			 
			return movementResult;
		}
				
		TreeSet<Character> candidateLetterWins = new TreeSet<Character>();
		TreeSet<Character> candidateLetterLose = new TreeSet<Character>();
		
		TreeSet<String> candidateWordsWins = new TreeSet<String>();
		TreeSet<String> candidateWordsLose = new TreeSet<String>();

		buildWordCandidates(rootNode, true, "", candidateWordsWins, 
				candidateWordsLose, candidateLetterWins, candidateLetterLose);
		
		logger.debug("candidateWordsWins:" + candidateWordsWins);
		logger.debug("candidateWordsLose:" + candidateWordsLose);
		logger.debug("candidateLetterWins:" + candidateLetterWins);
		logger.debug("candidateLetterLose:" + candidateLetterLose);
		
		// It gets a random letter from winners words more repited
		
		
		// It gets a random letter that it is´t in candidateLetterLose
		Character computerLetterMovement = findLetterWinner(candidateLetterWins, candidateLetterLose);		
		
		// Only in hard level
		if (movementHuman.getLevel() == Movement.LEVEL_HARD && computerLetterMovement == null && candidateWordsWins.size() > 0) {			
			// There isn´t a winner letter
			// We get the winner letter with more posibilities to win
			computerLetterMovement = getWinnerLetterDifferentialCounter(candidateWordsWins, candidateWordsLose);			
		}		
		
		if (computerLetterMovement == null && candidateLetterWins.size() > 0) {
			// There isn´t a winner letter but we don´t know if we will win
			// We get random letter
			candidateLetterWins.addAll(candidateLetterLose);			
			computerLetterMovement = getRandomLetter(candidateLetterWins);			
		}		
		
		if (computerLetterMovement == null && candidateWordsLose.size() > 0) {
			// we know that we will lose		
			
			TreeSet<String> biggestWords = getBiggestWords(candidateWordsLose);
			String wordRandom = getRandomWord(biggestWords);
			computerLetterMovement = wordRandom.charAt(0);
		}		
		
		movementResult.setWord(actualWord + computerLetterMovement.toString());
		movementResult.setLetter(computerLetterMovement.toString());
		
		// Check if there are sons
		rootNode = getLetterNode(dictionary.getWords().get(movementResult.getWord().charAt(0)), 
				movementResult.getWord());
		
		if (rootNode == null || rootNode.getNodes().size() == 0) {
			// There aren´t movements. Computer loses	
			movementResult.setResult(1);
			
		} else {
			// There are movements.			
			movementResult.setResult(0);
		}
		
		

		return movementResult;
	}
	
	/**
	 * Get biggest differential between set candidateWordsWins and
	 * candidateWordsLose that start with the same letter. Return a
	 * random character of this set.
	 * 
	 * @param candidateWordsWins winner words for the computer
	 * @param candidateWordsLose loser words for the computer
	 * @return random character
	 * 
	 * */
	private Character getWinnerLetterDifferentialCounter(
			TreeSet<String> candidateWordsWins, TreeSet<String> candidateWordsLose) {
		
		HashMap<Character, Integer> differentialCounterCharacters = new HashMap<Character, Integer>();
		
		HashMap<Character, Integer> counterCharacterWinner = getCharacterRepetitions(candidateWordsWins);
		HashMap<Character, Integer> counterCharacterLoser = getCharacterRepetitions(candidateWordsLose);		
		
		TreeSet<Character> winnerLettersResult = new TreeSet<Character>();
		
		if (candidateWordsWins != null && candidateWordsWins.size() > 0) {
			
			int maxDifferential = 0;
			boolean firstIteration = true;
			
			for (Character characterWinner : counterCharacterWinner.keySet()) {
				
				int counterLoser = counterCharacterLoser.get(characterWinner);
				
				int counterWinner = counterCharacterWinner.get(characterWinner);
				
				int differential = counterWinner - counterLoser;
				
				differentialCounterCharacters.put(characterWinner, differential);
				
				if (firstIteration == true) {
					maxDifferential = differential;
					firstIteration = false;
				}
				
				if (differential > maxDifferential) {
					maxDifferential = differential;					
				} 
			}
			
			logger.debug("differentialCounterCharacters:" + differentialCounterCharacters);			
			
			Iterator<Map.Entry<Character, Integer>> it = differentialCounterCharacters.entrySet().iterator();
			
		    while (it.hasNext()) {
		    	
		    	Map.Entry<Character, Integer> entry = it.next();
		        
		        if (entry.getValue().equals(maxDifferential)) {
		        	
		        	winnerLettersResult.add(entry.getKey());
		        }		       
		    }
		}	
		
		logger.debug("winnerLettersResult:" + winnerLettersResult);
		
		return getRandomLetter(winnerLettersResult);
	}
	
	
	/**
	 * Returns the repetitions of words that start with the same letter.
	 * @param candidateWords words
	 * @return repetitions of letters
	 * */
	private HashMap<Character,Integer> getCharacterRepetitions(
			TreeSet<String> candidateWords) {		
				
		HashMap<Character, Integer> counterCharacterResult = new HashMap<Character, Integer>();
		
		if (candidateWords != null && candidateWords.size() > 0) {
			
			for (String word : candidateWords) {
				
				Integer counter = counterCharacterResult.get(word.charAt(0));
				
				if (counter == null) {
					counter = 0;					
				} else {
					counter = counter + 1;					
				}				
				
				counterCharacterResult.put(word.charAt(0), counter);				
				
			}
		}
		
		return counterCharacterResult;
	}


	/**
	 * Find letters in candidateLetterWins that they aren´t in 
	 * candidateLetterLose.
	 * */
	private Character findLetterWinner(TreeSet<Character> candidateLetterWins, 
			TreeSet<Character> candidateLetterLose) {
		
		Character result = null;
		
		TreeSet<Character> candidateResult = new TreeSet<Character>();
		
		for (Character candidateLetterWin : candidateLetterWins) {
			
			if (!candidateLetterLose.contains(candidateLetterWin)) {
				
				candidateResult.add(candidateLetterWin);
				
			}
		}		
		
		result = getRandomLetter(candidateResult);
		
		logger.debug("findLetterWinner result:" + result);
		
		return result;
	}


	
	private Character getRandomLetter(TreeSet<Character> candidateResult) {
		
		Character result = null;
		
		if (candidateResult != null && candidateResult.size() > 0) {			
				
			Object[] arrayWinners = candidateResult.toArray();
			
			Random rn = new Random();
			
			int randomInt = rn.nextInt(arrayWinners.length);
			
			result = (Character) arrayWinners[randomInt];
		}
		
		logger.debug("getRandomLetter result:" + result);
		
		return result;
	}



	private TreeSet<String> getBiggestWords(TreeSet<String> words) {
		
		TreeSet<String> biggestWords = new TreeSet<String>();
		
		int maxLength = 0;
		
		for (String word : words) {
			
			if (word.length() > maxLength) {
				maxLength = word.length();
				
				biggestWords.clear();
				biggestWords.add(word);
				
			} else if (word.length() == maxLength) {
				biggestWords.add(word);
			}
		}
		
		logger.debug("biggestWords: " + biggestWords);		
		
		return biggestWords;
	}


	private String getRandomWord(TreeSet<String> words) {
		
		Object[] wordsList = words.toArray();
		
		Random rn = new Random();
		
		int randomInt = rn.nextInt(wordsList.length);

		
		logger.debug("getRandomWord: " + wordsList[randomInt]);
		
		return (String) wordsList[randomInt];		
	}
	
	
	/**
	 * Get all winning words, candidate winning characters, 
	 * loser words and candidate loser characters.
	 * */
	private void buildWordCandidates(LetterNode rootNode, boolean computerTurn, String wordResult, 
			TreeSet<String> candidateWordsWins, TreeSet<String> candidateWordsLose, 
			TreeSet<Character> candidateLetterWins, TreeSet<Character> candidateLetterLose) {		
		
		if (rootNode != null) {
			
			if (rootNode.getNodes().values().size() > 0) {
				for (LetterNode letterNode: rootNode.getNodes().values()) {					
					
						buildWordCandidates(letterNode, !computerTurn, wordResult + letterNode.getLetter(),
								candidateWordsWins, candidateWordsLose, candidateLetterWins, candidateLetterLose);					
					
				}

			}	else {
				if (computerTurn && wordResult.length() > 0) {
					candidateWordsWins.add(wordResult);
					candidateLetterWins.add(wordResult.charAt(0));
					
					
				} else if (wordResult.length() > 0) {
					candidateWordsLose.add(wordResult);
					candidateLetterLose.add(wordResult.charAt(0));
					
				}
			}
		}

		
	}

	/**
	 * Get the letter node from actualWord
	 * */
	public LetterNode getLetterNode(LetterNode letterNode, String actualWord) {

		if (letterNode == null || actualWord.length() == 1) {
			return letterNode;
		} else {
			return getLetterNode(letterNode.getNodes().get(actualWord.charAt(1)), actualWord.substring(1));
		}
	}



}

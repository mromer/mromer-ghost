package com.mromer.ghost.model;

import java.util.HashMap;

public class LetterNode {
	private char letter;
	private HashMap<Character, LetterNode> nodes = new HashMap<Character, LetterNode>();

	public LetterNode(String word) {
		
		if (word.length() > 0) {
			letter = word.charAt(0);
		}		
			

		if (word.length() > 1) {
			nodes.put(Character.valueOf(word.charAt(1)),
					new LetterNode(word.substring(1)));
		}
			
	}

	
	/**
	 * Add a word to a LetterNode structure.
	 * */
	public void addWord(String word) throws IllegalArgumentException {
		
		if (word.charAt(0) == letter) {
			
			if (!nodes.isEmpty()) {
				
				if (word.length() == 1) {		
				
					nodes.clear();
					
				} else {
					
					LetterNode nextNode = nodes.get(Character.valueOf(word.charAt(1)));

					if (nextNode == null) {
						
						nodes.put(Character.valueOf(word.charAt(1)),
								new LetterNode(word.substring(1)));
						
					} else {
						
						nextNode.addWord(word.substring(1));
						
					}
						
				}
			}
		} else {
			throw new IllegalArgumentException("Invalid first letter.");
		}
			
	}

	public boolean isLeafNode()	{
		return nodes.isEmpty();
	}

	public int leafNodeCount() {
		int leaves = 0;

		if (isLeafNode())
			leaves = 1;
		else
			for (LetterNode node : nodes.values())
				leaves += node.leafNodeCount();

		return leaves;
	}


	public HashMap<Character, LetterNode> getNodes() {
		return nodes;
	}

	public void setNodes(HashMap<Character, LetterNode> nodes) {
		this.nodes = nodes;
	}


	public char getLetter() {
		return letter;
	}


	public void setLetter(char letter) {
		this.letter = letter;
	}
	
	
	
} 
package com.mromer.ghost.model;

public class Movement {
	
	public static final int RESULT_CONTINUE 		= 0;
	public static final int RESULT_HUMAN_WINS 		= 1;
	public static final int RESULT_COMPUTER_WINS 	= 2;
	
	public static final int LEVEL_EASY 	= 0;
	public static final int LEVEL_HARD 	= 1;	
	
	private String word;
	
	private String letter;
	
	private int result;
	
	private int level;

	public String getWord() {
		return word;
	}

	public void setWord(String word) {
		this.word = word;
	}

	public String getLetter() {
		return letter;
	}

	public void setLetter(String letter) {
		this.letter = letter;
	}

	public int getResult() {
		return result;
	}

	public void setResult(int result) {
		this.result = result;
	}	
	
	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	@Override 
	public String toString() {
	    StringBuilder result = new StringBuilder();
	    
	    result.append("{");
	    result.append("Word: " + getWord());
	    result.append(" Letter: " + getLetter());
	    result.append(" Level: " + getLevel());
	    result.append(" Result: " + getResult());	    
	    result.append("}");

	    return result.toString();
	  }

}

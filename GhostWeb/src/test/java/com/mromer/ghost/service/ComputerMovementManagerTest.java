package com.mromer.ghost.service;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.mromer.ghost.model.Dictionary;
import com.mromer.ghost.model.Movement;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:/spring/application-config.xml")
public class ComputerMovementManagerTest {
	
	@Autowired
	private ComputerMovementManager computerMovementManager;
	
	private Dictionary dictionary;

	@Before
	public void setUp()	{
		dictionary = new Dictionary();		
	}


	@Test
	public void testComputerWin() {
		
		Movement movement = new Movement();
		movement.setWord("aargh");
		movement.setLevel(Movement.LEVEL_EASY);
		
		// Human send letter h. H is the last
		Movement movementResult = computerMovementManager.nextMovement(dictionary, movement);
		assertEquals(Movement.RESULT_COMPUTER_WINS, movementResult.getResult());

	}

	@Test
	public void testComputerLose() {
		
		Movement movement = new Movement();
		movement.setWord("aasvoge");
		movement.setLevel(Movement.LEVEL_EASY);

		// Human send letter e. aasvogel is the only solution
		Movement movementResult = computerMovementManager.nextMovement(dictionary, movement);
		assertEquals(Movement.RESULT_HUMAN_WINS, movementResult.getResult());	
		assertEquals("aasvogel", movementResult.getWord());
		assertEquals("l", movementResult.getLetter());
	}

	@Test
	public void testContinuePlaying() {
		
		Movement movement = new Movement();
		movement.setWord("aasvog");
		movement.setLevel(Movement.LEVEL_EASY);

		// There are more posibilities to play
		Movement movementResult = computerMovementManager.nextMovement(dictionary, movement);
		assertEquals(Movement.RESULT_CONTINUE, movementResult.getResult());
		assertEquals("aasvoge", movementResult.getWord());
		assertEquals("e", movementResult.getLetter());

	}

	@Test
	public void testComputerLoseBigWord() {

		Movement movement = new Movement();
		movement.setWord("aki");
		movement.setLevel(Movement.LEVEL_EASY);
		
		// Computer will lose. Select longer word
		Movement movementResult = computerMovementManager.nextMovement(dictionary, movement);
		assertEquals(Movement.RESULT_CONTINUE, movementResult.getResult());
		assertEquals("akim", movementResult.getWord());
		assertEquals("m", movementResult.getLetter());

	}
	
	
	
	/**
	 * Computer selects the option with more probability to win
	 * */
	@Test
	public void testComputerBestOption() {

		Movement movement = new Movement();
		movement.setWord("exc");
		movement.setLevel(Movement.LEVEL_HARD);
		
		// Computer will lose. Select longer word
		Movement movementResult = computerMovementManager.nextMovement(dictionary, movement);
		assertEquals(Movement.RESULT_CONTINUE, movementResult.getResult());
		assertEquals("excl", movementResult.getWord());
		assertEquals("l", movementResult.getLetter());

	}




}

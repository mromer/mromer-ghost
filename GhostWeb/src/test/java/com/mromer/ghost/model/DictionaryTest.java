package com.mromer.ghost.model;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.mromer.ghost.model.Dictionary;



@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:/spring/application-config.xml")
public class DictionaryTest {
	
	Dictionary dictionary;
	
	@Before
	public void setUp()
	{
		dictionary = new Dictionary();
	}

	@Test
	public void testDiccionarySize() {
		assertEquals(43622, dictionary.size());
	}
}


package com.mromer.ghost.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mromer.ghost.model.Movement;
import com.mromer.ghost.service.ComputerMovementManager;
import com.mromer.ghost.service.DictionaryManager;

@Controller
@RequestMapping("/rest")
public class OptimalGhostController {
	
	private final static Logger logger = (Logger) LoggerFactory.getLogger(OptimalGhostController.class);
	
	@Autowired
	private DictionaryManager dictionaryManager;
	
	@Autowired
	private ComputerMovementManager computerMovementManager;
	
	@RequestMapping(value="/", method=RequestMethod.POST)
	public @ResponseBody Movement playerMove(@ModelAttribute(value="movement") Movement movement) {	
		
		logger.debug("Start playerMove. Movement:" + movement);		
		
		Movement movementResult = computerMovementManager.nextMovement(dictionaryManager.getDictionary(), movement);

		logger.debug("End playerMove. MovementResult:" + movementResult);
		
		return movementResult;

	}
	
}

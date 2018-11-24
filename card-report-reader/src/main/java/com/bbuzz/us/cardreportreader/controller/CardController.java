package com.bbuzz.us.cardreportreader.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("/read")
public class CardController {


	@GetMapping(value="/text")
	public void cardTextFileReader()
	{

	}
}

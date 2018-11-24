package com.bbuzz.us.cardreportreader.jobs;


import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

import com.bbuzz.us.cardreportreader.model.CreditCardBill;

public class CardProcessor implements ItemProcessor<CreditCardBill, CreditCardBill>{

	private static final Logger log = LoggerFactory.getLogger(CardProcessor.class);

	@Override
	public CreditCardBill process(CreditCardBill card) throws Exception {

		log.info("Entered Card Processor");
		String dueDate = card.getBillDueDate();
		log.info("Indian Date: "+dueDate);
		SimpleDateFormat indianFormat = new SimpleDateFormat("dd/MM/YYYY");
		Date date = indianFormat.parse(dueDate);

		SimpleDateFormat usFormat = new SimpleDateFormat("MM/dd/YYYY");
		String finalDueDate = usFormat.format(date);
		log.info("US Date: "+ finalDueDate);
		card.setBillDueDate(finalDueDate);

		log.info("Setting this Card object: "+card.toString());
		return card;
	}

}

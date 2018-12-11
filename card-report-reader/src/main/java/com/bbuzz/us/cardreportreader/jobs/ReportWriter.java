package com.bbuzz.us.cardreportreader.jobs;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemStreamException;
import org.springframework.batch.item.ItemStreamWriter;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemWriter;

import com.bbuzz.us.cardreportreader.model.ReportDTO;

public class ReportWriter implements ItemWriter<List<ReportDTO>> {

    private static final Logger logger = LoggerFactory.getLogger(ReportWriter.class);

    private FlatFileItemWriter<ReportDTO> delegate;


	@Override
	public void write(List<? extends List<ReportDTO>> items) throws Exception {
		
		logger.info("Inside Writer---------------------");
	}

}

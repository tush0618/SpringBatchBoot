package com.bbuzz.us.cardreportreader.jobs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import com.bbuzz.us.cardreportreader.model.ReportDTO;

@Component
public class ReportProcessor implements ItemProcessor<ReportDTO, ReportDTO>{

		private static final Logger log = LoggerFactory.getLogger(ReportProcessor.class);

		@Override
		public ReportDTO process(ReportDTO report) throws Exception {

			log.info("Entered Report Processor");
			log.info("------------------------REPORT DTO--------------");
			log.info(report.toString());
			return report;
		}
}

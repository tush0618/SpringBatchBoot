package com.bbuzz.us.cardreportreader.jobs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.AfterStep;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.mapping.PassThroughFieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.batch.item.file.transform.FixedLengthTokenizer;
import org.springframework.batch.item.file.transform.LineTokenizer;
import org.springframework.batch.item.file.transform.PatternMatchingCompositeLineTokenizer;
import org.springframework.batch.item.file.transform.Range;
import org.springframework.batch.item.validator.ValidationException;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import com.bbuzz.us.cardreportreader.model.ReportDTO;
import com.bbuzz.us.cardreportreader.model.RowDTO;

@Component
public class ReportReader implements ItemReader<ReportDTO> {

	private static final Logger logger = LoggerFactory.getLogger(ReportReader.class);
	private FlatFileItemReader
	<FieldSet> delegate;
	private static final String H_RUNDATE = "RMDS*";
	private static final String H_REPORTDATE = "REPORTDATE*";
	private static final String H_DATEHEADER = "    	   DATE*";
	private static final String H_DASHHEADER = "     ---*";
	private static final String B_DATA  = "     0*";
	
	private static final String F_TOTAL = "  TOTAL*";
	private static final String F_REJCHECKFREE = "REJECTCHECK*";
	private static final String F_REJOTHER = "REJECTOTHER*";
	private static final String F_FEDTOTAL = "FEDGRANDTOTAL*";

	@BeforeStep
	public void beforeStep(StepExecution stepExecution) {
		delegate = new FlatFileItemReader<>();
		delegate.setResource(new ClassPathResource("FedReport.txt"));
		delegate.setLinesToSkip(1);
		delegate.setRecordSeparatorPolicy(new BlankLineRecordSeparatorPolicy());

		final DefaultLineMapper	<FieldSet> defaultLineMapper = new DefaultLineMapper<>();
		final PatternMatchingCompositeLineTokenizer	orderFileTokenizer = new PatternMatchingCompositeLineTokenizer();
		final Map<String, LineTokenizer> tokenizers = new HashMap<>();
		tokenizers.put(H_RUNDATE, runDateTokenizer());
		tokenizers.put(H_REPORTDATE, reportDateTokenizer());
		tokenizers.put(H_DATEHEADER, rowHeaderTokenizer());
		tokenizers.put(H_DASHHEADER, dashTokenizer());
		tokenizers.put(B_DATA, rowDataTokenizer());
		
		tokenizers.put(F_TOTAL, totalRowTokenizer());
		tokenizers.put(F_REJCHECKFREE, rejCheckfreeTokenizer());
		tokenizers.put(F_REJOTHER, rejOtherTokenizer());
		tokenizers.put(F_FEDTOTAL, fedTotalTokenizer());

		orderFileTokenizer.setTokenizers(tokenizers);

		defaultLineMapper.setLineTokenizer(orderFileTokenizer);
		defaultLineMapper.setFieldSetMapper(new PassThroughFieldSetMapper());

		delegate.setLineMapper(defaultLineMapper);
		delegate.open(stepExecution.getExecutionContext());
	}

	@AfterStep
	public void afterStep(StepExecution stepExecution) {
		delegate.close();
	}

	@Override
	public ReportDTO read() throws Exception, 
	UnexpectedInputException, ParseException, NonTransientResourceException {
		logger.info("start read");

		ReportDTO record = new ReportDTO();;

		FieldSet line;
		List<RowDTO> rowList = new ArrayList<>();
		
		while ((line = delegate.read()) != null) {
			logger.info("+============="+line);
			logger.info("+============="+record.toString());
			String prefix = line.readString("lineType");
			if (prefix.equals("RMDS")) {
				
				record.setRunDate(line.readString("runDate"));
			}
			else if (prefix.equals("REPORTDATE")) {
				record.setReportDate(line.readString("reportDate"));
			}
			else if (prefix.equals("TOTAL")) {
				record.setTotalTrans(line.readString("totalTrans"));
				record.setTotalAmt(line.readString("totalTrans"));
			}
			else if (prefix.equals("REJECTCHECK")) {
				record.setRejCheckfreeTrans(line.readString("rejCheckfreeTrans"));
				record.setRejCheckfreeAmt(line.readString("rejCheckfreeAmt"));
			}
			else if (prefix.equals("REJECTOTHER")) {
				record.setRejotherTrans(line.readString("rejotherTrans"));
				record.setRejOtherAmt(line.readString("rejOtherAmt"));
			}
			else if (prefix.equals("FEDGRANDTOTAL")) {
				record.setFedTrans(line.readString("fedTrans"));
				record.setFedAmt(line.readString("fedAmt"));
				break;
			}
			else if(prefix.equals("----") || prefix.equals("DATE"))
			{
				
			}
			else if (prefix.equals("0")) {
				RowDTO row = new RowDTO();
				row.setDate(line.readString("date"));
				row.setTrans(line.readString("trans"));
				row.setAmount(line.readString("amount"));
				rowList.add(row);
			} 
			record.setRows(rowList);
		}
		logger.info("end read");
		return record;
	}

	private LineTokenizer runDateTokenizer() {
		FixedLengthTokenizer tokenizer = new FixedLengthTokenizer();
		tokenizer.setColumns(new Range[]{ new Range(1, 4), new Range(47, 56) });
		tokenizer.setNames(new String[]{"lineType", "runDate"});
		tokenizer.setStrict(false);
		return tokenizer;
	}


	private LineTokenizer reportDateTokenizer() {
		FixedLengthTokenizer tokenizer = new FixedLengthTokenizer();
		tokenizer.setColumns(new Range[]{ new Range(1, 10), new Range(13, 22) });
		tokenizer.setNames(new String[]{"lineType", "reportDate" });
		tokenizer.setStrict(false);
		return tokenizer;
	}

	private LineTokenizer totalRowTokenizer() {
		FixedLengthTokenizer tokenizer = new FixedLengthTokenizer();
		tokenizer.setColumns(new Range[]{ new Range(1, 7), new Range(18, 29), new Range(35, 40) });
		tokenizer.setNames(new String[]{"lineType", "totalTrans", "totalAmt" });
		tokenizer.setStrict(false);
		return tokenizer;
	}

	private LineTokenizer rejCheckfreeTokenizer() {
		FixedLengthTokenizer tokenizer = new FixedLengthTokenizer();
		tokenizer.setColumns(new Range[]{ new Range(1, 11), new Range(18, 29), new Range(35, 40)});
		tokenizer.setNames(new String[]{"lineType", "rejCheckfreeTrans", "rejCheckfreeAmt" });
		tokenizer.setStrict(false);
		return tokenizer;
	}

	private LineTokenizer rejOtherTokenizer() {
		FixedLengthTokenizer tokenizer = new FixedLengthTokenizer();
		tokenizer.setColumns(new Range[]{ new Range(1, 11), new Range(18, 29), new Range(35, 40) });
		tokenizer.setNames(new String[]{"lineType", "rejotherTrans", "rejOtherAmt" });
		tokenizer.setStrict(false);
		return tokenizer;
	}

	private LineTokenizer fedTotalTokenizer() {
		FixedLengthTokenizer tokenizer = new FixedLengthTokenizer();
		tokenizer.setColumns(new Range[]{ new Range(1, 13), new Range(18, 29), new Range(35, 40) });
		tokenizer.setNames(new String[]{"lineType", "fedTrans", "fedAmt" });
		tokenizer.setStrict(false);
		return tokenizer;
	}
	
	
	private LineTokenizer dashTokenizer() {
		FixedLengthTokenizer tokenizer = new FixedLengthTokenizer();
		tokenizer.setColumns(new Range[]{new Range(12, 15) });
		tokenizer.setNames(new String[]{"lineType" });
		tokenizer.setStrict(false);
		return tokenizer;
	}
	
	private LineTokenizer rowHeaderTokenizer() {
		FixedLengthTokenizer tokenizer = new FixedLengthTokenizer();
		tokenizer.setColumns(new Range[]{new Range(6, 9), new Range(18, 29), new Range(35, 40)});
		tokenizer.setNames(new String[]{"lineType" });
		tokenizer.setStrict(false);
		return tokenizer;
	}
	
	private LineTokenizer rowDataTokenizer() {
		FixedLengthTokenizer tokenizer = new FixedLengthTokenizer();
		tokenizer.setColumns(new Range[]{
				new Range(1,6), new Range(6, 15), new Range(18, 29), new Range(35, 40)});
		tokenizer.setNames(new String[]{"lineType", "date", "trans", "amount" });
		tokenizer.setStrict(false);
		return tokenizer;
	}
	


}

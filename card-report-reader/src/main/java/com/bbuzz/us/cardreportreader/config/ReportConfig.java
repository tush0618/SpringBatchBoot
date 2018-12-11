package com.bbuzz.us.cardreportreader.config;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.bbuzz.us.cardreportreader.jobs.ReportProcessor;
import com.bbuzz.us.cardreportreader.jobs.ReportReader;
import com.bbuzz.us.cardreportreader.jobs.ReportWriter;
import com.bbuzz.us.cardreportreader.model.CreditCardBill;
import com.bbuzz.us.cardreportreader.model.ReportDTO;
import com.mysql.cj.log.Log;

@Configuration
@EnableBatchProcessing
public class ReportConfig {

	private static final Logger log = LoggerFactory.getLogger(ReportConfig.class);

	@Qualifier("H2DataSource")
	@Autowired
	private DataSource dataSource;

	@Autowired
	private JobBuilderFactory jobBuilderFactory;

	@Autowired
	private StepBuilderFactory stepBuilderFactory;

	@Autowired
	private ReportProcessor process;

	@Autowired
	private ReportReader reader;
	
	//@Bean
//	public FlatFileItemWriter<ReportDTO> writer()
//	{
//		FlatFileItemWriter<ReportDTO> fileItemWriter = new FlatFileItemWriter<>();
//		
//		log.info("Inside Writer");
//		return fileItemWriter;
//		
//	}
	
	@Bean
	public JdbcBatchItemWriter<ReportDTO> cardWriter()
	{
		log.info("Entering DB Writer");
		JdbcBatchItemWriter<ReportDTO> jdbcBatchItemWriter = new JdbcBatchItemWriter<>();
		jdbcBatchItemWriter.setDataSource(dataSource);


		jdbcBatchItemWriter.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<ReportDTO>());
		log.info("Bean set as SQL Parameter");
		jdbcBatchItemWriter.setSql("INSERT INTO CREDIT_CARD_BILL(CardNumber, BillDueDate, DueAmount, CardType) VALUES"
				+ "(:cardNumber, :billDueDate, :dueAmount, :cardType)");
		log.info("Query Executed");
		return jdbcBatchItemWriter;

	}
	@Bean
	public Job getJob()
	{
		log.info("Inside Job");
		return jobBuilderFactory.get("getJob").incrementer(new RunIdIncrementer()).flow(step1()).end().build(); 
	}

	@Bean
	public Step step1()
	{
		log.info("Inside Step1");
		return stepBuilderFactory.get("step1").<ReportDTO, ReportDTO>chunk(1).reader(reader).processor(process).writer(cardWriter()).build();

	}

}

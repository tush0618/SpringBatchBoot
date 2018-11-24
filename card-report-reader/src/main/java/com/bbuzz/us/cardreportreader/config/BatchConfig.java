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
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import com.bbuzz.us.cardreportreader.jobs.CardProcessor;
import com.bbuzz.us.cardreportreader.listener.BatchJobListener;
import com.bbuzz.us.cardreportreader.model.CreditCardBill;

@Configuration
@EnableBatchProcessing
public class BatchConfig {

	private static final Logger log = LoggerFactory.getLogger(BatchConfig.class);

	@Autowired
	private JobBuilderFactory jobBuilderFactory;

	@Autowired
	private StepBuilderFactory stepBuilderFactory;

	@Autowired
	private BatchJobListener batchJobListener;


	@Qualifier("H2DataSource")
	@Autowired
	private DataSource dataSource;

	@Bean
	public FlatFileItemReader<CreditCardBill> cardReader()
	{
		log.info("Entered Reader");
		FlatFileItemReader<CreditCardBill> fReader  = new FlatFileItemReader<>();
		fReader.setResource(new ClassPathResource("cards-bill.txt"));
		fReader.setName("Text Reader");
		fReader.setLineMapper(lineMapper());
		log.info("Exiting Reader");
		return fReader;

	}

	@Bean
	public LineMapper<CreditCardBill> lineMapper(){
		log.info("Entered Line Mapper");
		DefaultLineMapper<CreditCardBill> defaultLineMapper = new  DefaultLineMapper<>();
		DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();

		lineTokenizer.setDelimiter(",");
		lineTokenizer.setNames(new String[] {"cardNumber","billDueDate","dueAmount","cardType",});

		BeanWrapperFieldSetMapper<CreditCardBill> fieldSetMapper = new BeanWrapperFieldSetMapper<>();
		fieldSetMapper.setTargetType(CreditCardBill.class);

		defaultLineMapper.setFieldSetMapper(fieldSetMapper);
		defaultLineMapper.setLineTokenizer(lineTokenizer);
		log.info("Exiting Mapper");
		return defaultLineMapper;

	}

	@Bean 
	public CardProcessor cardProcessor()
	{
		return new CardProcessor();

	}

	@Bean
	public JdbcBatchItemWriter<CreditCardBill> cardWriter()
	{
		log.info("Entering DB Writer");
		JdbcBatchItemWriter<CreditCardBill> jdbcBatchItemWriter = new JdbcBatchItemWriter<>();
		jdbcBatchItemWriter.setDataSource(dataSource);


		jdbcBatchItemWriter.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<CreditCardBill>());
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
		return jobBuilderFactory.get("getJob").incrementer(new RunIdIncrementer()).listener(batchJobListener).flow(step1()).end().build(); 
	}

	@Bean
	public Step step1()
	{
		log.info("Inside Step1");
		return stepBuilderFactory.get("step1").<CreditCardBill, CreditCardBill>chunk(100).reader(cardReader()).processor(cardProcessor()).writer(cardWriter()).build();

	}



}

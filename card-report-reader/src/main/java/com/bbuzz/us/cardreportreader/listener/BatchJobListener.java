package com.bbuzz.us.cardreportreader.listener;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import com.bbuzz.us.cardreportreader.model.CreditCardBill;

@Component
public class BatchJobListener extends JobExecutionListenerSupport{

	private static final Logger log = LoggerFactory.getLogger(BatchJobListener.class);
	private final JdbcTemplate jdbcTemplate;

	@Autowired
	public BatchJobListener(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public void afterJob(JobExecution jobExecution) {
		if (jobExecution.getStatus() == BatchStatus.COMPLETED) {
			log.info("!!! JOB FINISHED !! It's time to verify the results!!");

			List<CreditCardBill> results = jdbcTemplate.query(
					"SELECT CardNumber, BillDueDate, DueAmount, CardType FROM CREDIT_CARD_BILL", new RowMapper<CreditCardBill>() {

						@Override
						public CreditCardBill mapRow(ResultSet rs, int row) throws SQLException {
							return new CreditCardBill(rs.getString(1), rs.getString(2), rs.getDouble(3), rs.getString(4));
						}
					});

			for (CreditCardBill bill : results) {
				log.info("Found this detail for Card Bills <" + bill + "> in the database.");
			}
		}
	}
}

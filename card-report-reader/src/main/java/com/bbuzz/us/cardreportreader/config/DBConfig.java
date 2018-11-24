package com.bbuzz.us.cardreportreader.config;


import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

@Configuration
public class DBConfig {

	private static final Logger log = LoggerFactory.getLogger(DBConfig.class);

	@Value("${mysql.url}")
	private String MySQL_DB_URL;

	@Value("${mysql.username}")
	private String MySQL_DB_USERNAME;

	@Value("$mysql.password")
	private String MySQL_DB_PASSWORD;


	@Primary
	@Bean(name="HSQLDataSource")
	public DataSource hsqlDataSource()
	{
		log.info("Entered HSQL DataSource");
		EmbeddedDatabaseBuilder databaseBuilder = new EmbeddedDatabaseBuilder();
		EmbeddedDatabase hsqldb = databaseBuilder.setType(EmbeddedDatabaseType.HSQL)
				.addScript("cards-bill.sql")
				.build();
		log.info("Created Table in HSQL DB");
		return hsqldb;
	}

	@Bean(name="H2DataSource")
	public DataSource h2DataSource()
	{
		log.info("Entered H2 DataSource");
		EmbeddedDatabaseBuilder databaseBuilder = new EmbeddedDatabaseBuilder();
		EmbeddedDatabase h2db = databaseBuilder.setType(EmbeddedDatabaseType.H2)
				.addScript("cards-bill.sql")
				.build();
		log.info("Created Table in H2 DB");
		return h2db;
	}

	@Bean(name="MySQLDataSource")
	public DataSource mysqlDataSource()
	{
		log.info("Enterd MySQL DataSource");
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setUrl(MySQL_DB_URL);
		dataSource.setUsername(MySQL_DB_USERNAME);
		dataSource.setPassword(MySQL_DB_PASSWORD);
		dataSource.setSchema("cards-bill.sql");
		log.info("Created Table in MySQL DB");
		return dataSource;

	}

}

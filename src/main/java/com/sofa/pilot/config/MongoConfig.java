package com.sofa.pilot.config;

import java.util.Collection;
import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;

@Configuration
@EnableMongoRepositories(basePackages = "com.sofa.pilot.repository")
public class MongoConfig extends AbstractMongoClientConfiguration {

	private static final String DATABASE_SCHOOL = "school";
	private static final String PACKAGE_DOMAIN = "com.sofa.pilot.domain";
	private static final String MONGO_HOST = "MONGO_HOST";
	private static final String MONGO_PORT = "MONGO_PORT";

	private PropertyLoader propertyLoader;

	public MongoConfig(@Autowired PropertyLoader propertyLoader) {
		this.propertyLoader = propertyLoader;
	}

	@Override
	protected String getDatabaseName() {
		return DATABASE_SCHOOL;
	}

	@Override
	public MongoClient mongoClient() {
		String host = propertyLoader.getValueOf(MONGO_HOST);
		String port = propertyLoader.getValueOf(MONGO_PORT);
		ConnectionString connectionString = new ConnectionString(
//				"mongodb://" + host + ":" + port + "/" + DATABASE_SCHOOL);
				"mongodb://" + "13.212.120.239" + ":" + "27017" + "/" + DATABASE_SCHOOL);// For demo only
				System.out.println("connectionString : "+connectionString);
		MongoClientSettings mongoClientSettings = MongoClientSettings.builder().applyConnectionString(connectionString)
				.build();

		return MongoClients.create(mongoClientSettings);
	}

	@Override
	protected Collection<String> getMappingBasePackages() {
		return Collections.singleton(PACKAGE_DOMAIN);
	}
}

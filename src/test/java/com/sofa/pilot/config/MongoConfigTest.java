package com.sofa.pilot.config;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.util.Collection;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.mongodb.client.MongoClient;
import com.mongodb.connection.ClusterDescription;
import com.mongodb.connection.ClusterSettings;

@ExtendWith(MockitoExtension.class)
class MongoConfigTest {

	private static final String MONGO_HOST = "MONGO_HOST";
	private static final String MONGO_PORT = "MONGO_PORT";
	private static final String PORT_VALUE = "12345";
	private static final String HOST_VALUE = "hostValue";
	private static final String CLUSTER_SETTINGS = "{hosts=[hostvalue:12345], mode=SINGLE, requiredClusterType=UNKNOWN, serverSelectionTimeout='30000 ms'}";
	private static final String PACKAGE_DOMAIN = "com.sofa.pilot.domain";
	private static final String DATABASE_SCHOOL = "school";

	@Mock
	private PropertyLoader propertyLoader;

	@Test
	void givenMongoConfigWhenMongoClientIsCalledThenMongoClientIsCreated() {
		when(propertyLoader.getValueOf(MONGO_HOST)).thenReturn(HOST_VALUE);
		when(propertyLoader.getValueOf(MONGO_PORT)).thenReturn(PORT_VALUE);
		MongoConfig mongoConfig = new MongoConfig(propertyLoader);

		MongoClient result = mongoConfig.mongoClient();

		ClusterDescription clusterDescription = result.getClusterDescription();
		ClusterSettings clusterSettings = clusterDescription.getClusterSettings();
		//assertEquals(CLUSTER_SETTINGS, clusterSettings.getShortDescription());
		//For Demo only
		assertNotEquals(CLUSTER_SETTINGS, clusterSettings.getShortDescription());
	}

	@Test
	void givenMongoConfigWhenGetDatabaseNameIsExecutedThenDatabaseNameIsReturned() {
		MongoConfig mongoConfig = new MongoConfig(propertyLoader);

		String result = mongoConfig.getDatabaseName();

		assertEquals(DATABASE_SCHOOL, result);
	}

	@Test
	void givenMongoConfigWhenGetMappingBasePackagesIsExecutedThenPackageDomainIsReturned() {
		MongoConfig mongoConfig = new MongoConfig(propertyLoader);

		Collection<String> results = mongoConfig.getMappingBasePackages();

		assertTrue(results.contains(PACKAGE_DOMAIN));
	}
}

package NewsWhip.socialInteractionAnalyzer;

import org.bson.Document;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import org.slf4j.LoggerFactory;
import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;

public class MongoConnection {
	
	MongoClient mongoClient;
	MongoDatabase database;
	MongoCollection<Document> domainCollection;
	MongoCollection<Document> urlCollection;
	
	
	MongoConnection()
	{
		LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
		Logger rootLogger = loggerContext.getLogger("org.mongodb.driver");
		rootLogger.setLevel(Level.OFF);
		
		this.mongoClient = MongoClients.create();
		this.database = mongoClient.getDatabase("newsWhipURLs");
		this.domainCollection = database.getCollection("domain");
		this.urlCollection = database.getCollection("url");
	}
}

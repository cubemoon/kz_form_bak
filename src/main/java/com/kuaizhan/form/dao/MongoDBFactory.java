package com.kuaizhan.form.dao;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kuaizhan.form.common.StringUtil;
import com.kuaizhan.form.conf.Config;
import com.mongodb.DB;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.WriteConcern;

public class MongoDBFactory {

	private final static Logger log = LoggerFactory.getLogger(MongoDBFactory.class);

	private final static String MONGO_KEY = "mongodb";
	private final static String MONGO_USERNAME_KEY = "mongo_user";
	private final static String MONGO_PASSWORD_KEY = "mongo_pass";
	private final static String MONGO_DB_NAME_KEY = "mongo_db";

	protected static String DB_NAME = null;
	private static MongoClient mongoClient = null;
	

	private static List<String> getMongoSource() {
		String mongoSources = Config.get(MONGO_KEY);
		return Arrays.asList(mongoSources.split(","));
	}

	private static List<ServerAddress> parseMongoSource(List<String> sources) {
		List<ServerAddress> addrList = new ArrayList<ServerAddress>(sources.size());
		for (String source : sources) {
			int index = source.indexOf(':');
			if (index == -1) {
				log.error("Invalid data source properties format!");
				System.exit(-1);
			}
			try {
				addrList.add(new ServerAddress(source.substring(0, index), Integer.parseInt(source
						.substring(index + 1))));
			} catch (NumberFormatException e) {
				log.error("Invalid port format in data source properties!");
				System.exit(-1);
			} catch (UnknownHostException e) {
				log.error("Unkown host: " + source);
				System.exit(-1);
			}
		}
		return addrList;
	}

	private static List<MongoCredential> buildCredential() {
		List<MongoCredential> cred = new ArrayList<MongoCredential>();
		String username = Config.get(MONGO_USERNAME_KEY, null);
		String password = Config.get(MONGO_PASSWORD_KEY, null);
		DB_NAME = Config.get(MONGO_DB_NAME_KEY);
		if (!StringUtil.isEmpty(username) && !StringUtil.isEmpty(password)) {
			cred.add(MongoCredential.createMongoCRCredential(username, DB_NAME,
					password.toCharArray()));
		}
		return cred;
	}

	private static MongoClientOptions buildOptions() {
		MongoClientOptions.Builder builder = new MongoClientOptions.Builder();
		builder.connectionsPerHost(500);
		builder.minConnectionsPerHost(5);
		builder.writeConcern(WriteConcern.ACKNOWLEDGED);
		return builder.build();
	}

	public static MongoClient getClient() {
		if (mongoClient == null) {
			synchronized (MongoDBFactory.class) {
				if (mongoClient == null) {
					List<String> sources = getMongoSource();
					List<ServerAddress> addrList = parseMongoSource(sources);
					mongoClient = new MongoClient(addrList, buildCredential(), buildOptions());
					log.info(String.format("Mongo connected: %s", sources));
				}
			}
		}
		return mongoClient;
	}

	public static DB getDB() {
		return getClient().getDB(DB_NAME);
	}
}

package com.kuaizhan.form.dao;

import java.net.UnknownHostException;

import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;

import com.github.jmkgreen.morphia.logging.MorphiaLoggerFactory;
import com.github.jmkgreen.morphia.logging.slf4j.SLF4JLogrImplFactory;
import com.mongodb.MongoClient;


public class DaoHelper {
	
	static{
		MorphiaLoggerFactory.registerLogger(SLF4JLogrImplFactory.class );
	}
	
	public static Datastore getDatastore() {
		Morphia morphia = new Morphia();
		Datastore ds = morphia.createDatastore(MongoDBFactory.getClient(), MongoDBFactory.DB_NAME);
		return ds;
	}
	
	static MongoClient old_mongoClient;
	static{ 
		try {
			old_mongoClient = new MongoClient("10.13.94.204");
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
 	}
	
	public static Datastore getOldDatastore(){
		Morphia morphia = new Morphia();
		Datastore ds = morphia.createDatastore(old_mongoClient, "kz_plugin");
		return ds;
	}

}

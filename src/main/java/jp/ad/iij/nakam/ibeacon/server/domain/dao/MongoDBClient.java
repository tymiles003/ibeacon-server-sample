package jp.ad.iij.nakam.ibeacon.server.domain.dao;

import java.net.UnknownHostException;

import org.vertx.java.core.json.JsonObject;

import com.mongodb.DB;
import com.mongodb.MongoClient;

public class MongoDBClient {

    private DB db;

    public MongoDBClient(JsonObject config) throws UnknownHostException {
        String mongodbUrl = config.getString("mongodb_host");
        Integer mongodbPort = config.getInteger("mongodb_port");
        MongoClient mongoClient = null;
        mongoClient = new MongoClient(mongodbUrl, mongodbPort);
        db = mongoClient.getDB("ble");
    }

    public DB getDB() {
        return db;
    }
}

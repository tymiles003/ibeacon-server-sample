package jp.ad.iij.nakam.ibeacon.server;

import org.vertx.java.core.json.JsonObject;
import org.vertx.java.platform.Verticle;

/**
 * Chat
 */
public class MainVerticle extends Verticle {

    /**
     * startメソッド
     */
    @Override
    public void start() {
        JsonObject config = container.config();
        container.deployVerticle(CreateWebSocketVerticle.class.getName(), config);
        container.deployVerticle(CreateHttpServerVerticle.class.getName(), config);
    }
}
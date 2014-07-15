package jp.ad.iij.nakam.ibeacon.server;

import java.net.UnknownHostException;

import jp.ad.iij.nakam.ibeacon.server.domain.dao.MongoDBClient;

import org.vertx.java.core.Handler;
import org.vertx.java.core.buffer.Buffer;
import org.vertx.java.core.eventbus.EventBus;
import org.vertx.java.core.eventbus.Message;
import org.vertx.java.core.http.HttpServerRequest;
import org.vertx.java.core.http.RouteMatcher;
import org.vertx.java.core.http.ServerWebSocket;
import org.vertx.java.core.json.JsonObject;
import org.vertx.java.core.logging.Logger;
import org.vertx.java.platform.Verticle;

import com.mongodb.DB;
import com.mongodb.DBObject;

/**
 * Chat
 */
public class MainVerticle extends Verticle {

    private static final String WS_ADDRESS = "ibeacon.log";
    private static final String INDEX_PAGE = "index.html";

    MongoDBClient mongo;

    /**
     * startメソッド
     */
    @Override
    public void start() {

        // config
        JsonObject config = container.config();
        String serverUrl = config.getString("server_url");
        int httpPort = config.getInteger("http_port");
        int wsPort = config.getInteger("ws_port");
        final EventBus eventBus = vertx.eventBus();
        final Logger logger = container.logger();
        try {
            mongo = new MongoDBClient(config);
        } catch (UnknownHostException e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        }
        final DB db = mongo.getDB();
        // HTTPリクエストサーバー

        Handler<HttpServerRequest> indexFileMatcherHandler = new Handler<HttpServerRequest>() {
            @Override
            public void handle(HttpServerRequest event) {
                event.response().sendFile(INDEX_PAGE);
            }
        };
        Handler<HttpServerRequest> jsCssFileMatcherHandler = new Handler<HttpServerRequest>() {
            @Override
            public void handle(HttpServerRequest event) {
                event.response().sendFile(event.path().substring(1));
            }
        };

        Handler<HttpServerRequest> inputHandler = new Handler<HttpServerRequest>() {
            @Override
            public void handle(final HttpServerRequest request) {
                request.dataHandler(new Handler<Buffer>() {
                    @Override
                    public void handle(Buffer event) {
                        String param = event.toString("UTF-8");
                        System.out.println(param);
                        Object o = com.mongodb.util.JSON.parse(param);
                        DBObject dbObj = (DBObject) o;
                        db.getCollection("messages").insert(dbObj);
                        eventBus.publish(WS_ADDRESS, param);
                        request.response().sendFile(INDEX_PAGE);
                    }
                });
            }
        };

        RouteMatcher route = new RouteMatcher().get("/", indexFileMatcherHandler)
                .get(".*\\.(css|js)$", jsCssFileMatcherHandler).post("/input", inputHandler);

        vertx.createHttpServer().requestHandler(route).listen(httpPort, serverUrl);

        vertx.createHttpServer().websocketHandler(new Handler<ServerWebSocket>() {

            @Override
            public void handle(final ServerWebSocket ws) {

                // def ハンドラ
                final Handler<Message<String>> messageHandler = new Handler<Message<String>>() {
                    @Override
                    public void handle(Message<String> message) {
                        String response = message.body();
                        ws.writeTextFrame(response);
                    }
                };

                // WebSocketが切れたとき、unregisterHandlerを行う
                ws.closeHandler(new Handler<Void>() {
                    @Override
                    public void handle(final Void event) {
                        eventBus.unregisterHandler(WS_ADDRESS, messageHandler);
                    }
                });

                // 入力されることはない
                ws.dataHandler(new Handler<Buffer>() {
                    @Override
                    public void handle(final Buffer buffer) {
                        //eventBus.publish(WS_ADDRESS, buffer.toString());
                    }
                });
                // eventBus経由でデータを受けた時のイベントハンドラー
                eventBus.registerHandler(WS_ADDRESS, messageHandler);
            }
        }).listen(wsPort, serverUrl);
    }
}
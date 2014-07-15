package jp.ad.iij.nakam.ibeacon.server;

import jp.ad.iij.nakam.ibeacon.server.common.ConstantConfiguration;

import org.vertx.java.core.Handler;
import org.vertx.java.core.buffer.Buffer;
import org.vertx.java.core.eventbus.EventBus;
import org.vertx.java.core.eventbus.Message;
import org.vertx.java.core.http.ServerWebSocket;
import org.vertx.java.core.json.JsonObject;
import org.vertx.java.platform.Verticle;

public class CreateWebSocketVerticle extends Verticle {

    private static final String WS_ADDRESS = ConstantConfiguration.WS_ADDRESS;

    public void start() {

        JsonObject config = container.config();
        String serverUrl = config.getString("server_url");
        int wsPort = config.getInteger("ws_port");
        final EventBus eventBus = vertx.eventBus();

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

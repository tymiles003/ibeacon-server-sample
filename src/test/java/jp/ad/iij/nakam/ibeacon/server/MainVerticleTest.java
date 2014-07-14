package jp.ad.iij.nakam.ibeacon.server;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;
import org.junit.Ignore;
import org.junit.Test;
import org.vertx.testtools.TestVerticle;

public class MainVerticleTest extends TestVerticle {

    @Test
    @Ignore
    public void test() {

        String url = "http://localhost:8080/input";
        @SuppressWarnings({ "deprecation", "resource" })
        HttpClient httpClient = new DefaultHttpClient();
        HttpPost post = new HttpPost(url);

        JSONObject json = new JSONObject();
        json.append("key", "value");
        json.append("num", 123);
        json.append("flag", true);
        String message = json.toString();

        @SuppressWarnings("unused")
        HttpResponse res = null;

        try {
            post.setEntity(new StringEntity(message, "UTF-8"));
            res = httpClient.execute(post);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}

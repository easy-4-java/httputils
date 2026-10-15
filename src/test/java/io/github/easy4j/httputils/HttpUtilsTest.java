package io.github.easy4j.httputils;

import com.sun.net.httpserver.HttpServer;
import io.github.easy4j.httputils.handler.PlainTextResponseHandler;
import org.junit.Test;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertEquals;

/** Verifies that the public GET helper handles a local HTTP response. */
public class HttpUtilsTest {

    @Test
    public void getReturnsLoopbackResponse() throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress("127.0.0.1", 0), 0);
        server.createContext("/health", exchange -> {
            byte[] body = "ready".getBytes(StandardCharsets.UTF_8);
            exchange.getResponseHeaders().set("Content-Type", "text/plain; charset=UTF-8");
            exchange.sendResponseHeaders(200, body.length);
            try (OutputStream output = exchange.getResponseBody()) {
                output.write(body);
            }
        });
        server.start();
        try {
            String url = "http://127.0.0.1:" + server.getAddress().getPort() + "/health";
            assertEquals("ready", HttpClientUtils.httpRequestWithGet(url, new PlainTextResponseHandler()));
        } finally {
            server.stop(0);
        }
    }
}

package service;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

public class HttpTaskServer {
    private static final int PORT = 8080;

    private TaskManager taskManager;

    public static void main(String[] args) throws IOException {

        HttpServer httpServer = HttpServer.create(new InetSocketAddress(PORT), 0);
        httpServer.createContext("/hello", new TaskHandler());
        httpServer.start();

    }

    static class TaskHandler implements HttpHandler {


        @Override
        public void handle(HttpExchange exchange) throws IOException {

        }
    }
}

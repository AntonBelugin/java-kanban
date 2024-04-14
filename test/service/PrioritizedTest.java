package service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import task.Epic;
import task.Subtask;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class PrioritizedTest {

    File file = new File("resources/task.csv");
    TaskManager taskManager = new FileBackedTaskManager(file);
    HttpTaskServer taskServer = new HttpTaskServer(taskManager);

    URI uri = URI.create("http://localhost:8080/prioritized");

    @BeforeEach
    public void beforeEach() {
        taskServer.start();
        taskManager.makeEpic(new Epic("Переезд",
                "Переехать в другую квартиру"));

        taskManager.makeSubtask(new Subtask(1, "Собрать коробки",
                "собрать в коробки вещи", "12.12.2023 15:00", 30));

        taskManager.makeSubtask(new Subtask(1, "Собрать коробки2",
                "собрать в коробки вещи2", "12.12.2023 16:00", 25));
    }

    @AfterEach
    public void afterEach() {
        taskManager.clearEpics();
        taskServer.stop();
    }

    @Test
    void getPrioritized() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(uri)
                .header("Accept", "application/json")
                .build();

        HttpResponse<String> response = sendResponse(request);
        Assertions.assertEquals(200, response.statusCode());
    }

    public HttpResponse<String> sendResponse(HttpRequest request) throws IOException, InterruptedException {
        try (HttpClient client = HttpClient.newHttpClient()) {
            HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
            return client.send(request, handler);
        }
    }

}

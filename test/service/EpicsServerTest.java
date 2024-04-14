package service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import convertors.DurationAdapter;
import convertors.LocalDateTimeAdapter;
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
import java.time.Duration;
import java.time.LocalDateTime;

public class EpicsServerTest {

    File file = new File("resources/task.csv");
    TaskManager taskManager = new FileBackedTaskManager(file);
    HttpTaskServer taskServer = new HttpTaskServer(taskManager);

    URI uri = URI.create("http://localhost:8080/epics");

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
    void getEpics() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(uri)
                .header("Accept", "application/json")
                .build();

        HttpResponse<String> response = sendResponse(request);
        Assertions.assertEquals(200, response.statusCode());
    }

    @Test
    void getEpicByIdError() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(uri.toString() + "/2"))
                .header("Accept", "application/json")
                .build();

        HttpResponse<String> response = sendResponse(request);
        Assertions.assertEquals(404, response.statusCode());
    }

    @Test
    void getEpicById() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(uri.toString() + "/1"))
                .header("Accept", "application/json")
                .build();

        HttpResponse<String> response = sendResponse(request);
        Assertions.assertEquals(200, response.statusCode());
    }

    @Test
    void getEpicByIdSubtasks() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(uri.toString() + "/1/subtasks"))
                .header("Accept", "application/json")
                .build();

        HttpResponse<String> response = sendResponse(request);
        Assertions.assertEquals(200, response.statusCode());
    }

    @Test
    void postCreateEpic() throws IOException, InterruptedException {
        Gson gson = gsonCreate();
        Epic epic = new Epic("Переезд2",
                "Переехать в другую квартиру2");
        String json = gson.toJson(epic);
        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .uri(uri)
                .header("Accept", "application/json")
                .build();

        HttpResponse<String> response = sendResponse(request);
        Assertions.assertEquals(201, response.statusCode());
        Assertions.assertNotNull(taskManager.getEpicById(4));
    }

    @Test
    void deleteEpic() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .DELETE()
                .uri(URI.create(uri.toString() + "/1"))
                .header("Accept", "application/json")
                .build();

        HttpResponse<String> response = sendResponse(request);
        Assertions.assertEquals(200, response.statusCode());
        Assertions.assertNull(taskManager.getEpicById(1));
    }

    public HttpResponse<String> sendResponse(HttpRequest request) throws IOException, InterruptedException {
        try (HttpClient client = HttpClient.newHttpClient()) {
            HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
            return client.send(request, handler);
        }
    }

    public Gson gsonCreate() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.serializeNulls();
        gsonBuilder.registerTypeAdapter(Duration.class, new DurationAdapter());
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter());
        return gsonBuilder.create();
    }
}
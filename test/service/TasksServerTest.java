package service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import convertors.DurationAdapter;
import convertors.LocalDateTimeAdapter;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import task.Task;
import task.TaskStatus;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;

public class TasksServerTest {

    File file = new File("resources/task.csv");
    TaskManager taskManager = new FileBackedTaskManager(file);
    HttpTaskServer taskServer = new HttpTaskServer(taskManager);

    URI uri = URI.create("http://localhost:8080/tasks");

    @BeforeEach
    public void beforeEach() {
        taskServer.start();
        taskManager.makeTask(new Task("Помыть посуду",
                "помыть посуду горячей водой", "12.12.2023 20:00", 40));
    }

    @AfterEach
    public void afterEach() {
        taskManager.clearTasks();
        taskServer.stop();
    }

    @Test
    void getTasks() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(uri)
                .header("Accept", "application/json")
                .build();

        HttpResponse<String> response = sendResponse(request);
        Assertions.assertEquals(200, response.statusCode());
    }

    @Test
    void getTaskByIdError() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(uri.toString() + "/2"))
                .header("Accept", "application/json")
                .build();

        HttpResponse<String> response = sendResponse(request);
        Assertions.assertEquals(404, response.statusCode());
    }

    @Test
    void getTaskById() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(uri.toString() + "/1"))
                .header("Accept", "application/json")
                .build();

        HttpResponse<String> response = sendResponse(request);
        Assertions.assertEquals(200, response.statusCode());
    }

    @Test
    void postCreateTask() throws IOException, InterruptedException {
        Gson gson = gsonCreate();
        Task task = new Task("Помыть посуду2",
                "помыть посуду горячей водой2", "13.12.2023 20:00", 40);
        String json = gson.toJson(task);
        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .uri(uri)
                .header("Accept", "application/json")
                .build();

        HttpResponse<String> response = sendResponse(request);
        Assertions.assertEquals(201, response.statusCode());
    }

    @Test
    void updateTask() throws IOException, InterruptedException {
        Gson gson = gsonCreate();
        Task task = new Task(1,"Помыть посуду",
                "помыть посуду горячей водой", TaskStatus.DONE,"12.12.2023 20:00", 40);
        String json = gson.toJson(task);
        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .uri(uri)
                .header("Accept", "application/json")
                .build();

        HttpResponse<String> response = sendResponse(request);
        Assertions.assertEquals(200, response.statusCode());
    }

    @Test
    void deleteTask() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .DELETE()
                .uri(URI.create(uri.toString() + "/1"))
                .header("Accept", "application/json")
                .build();

        HttpResponse<String> response = sendResponse(request);
        Assertions.assertEquals(204, response.statusCode());
        Assertions.assertNull(taskManager.getTaskById(1));
    }

    @Test
    void postCreateErrorTimeTask() throws IOException, InterruptedException {
        Gson gson = gsonCreate();
        Task task = new Task("Помыть посуду2",
                "помыть посуду горячей водой2", "13.12.2023 20:00", 40);
        String json = gson.toJson(task);
        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .uri(uri)
                .header("Accept", "application/json")
                .build();

        HttpResponse<String> response = sendResponse(request);
        HttpResponse<String> response2 = sendResponse(request);
        Assertions.assertEquals(406, response2.statusCode());
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

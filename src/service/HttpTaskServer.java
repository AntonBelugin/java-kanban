package service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import convertors.DurationAdapter;
import convertors.LocalDateTimeAdapter;
import task.Epic;
import task.Subtask;
import task.Task;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;

public class HttpTaskServer {
    private static final int PORT = 8080;

    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;

    private static TaskManager taskManager;

    private HttpServer server;

    private Gson gson;

    public HttpTaskServer(TaskManager taskManager) {
        this.taskManager = taskManager;
        this.gson = gsonCreate();
        try {
            server = HttpServer.create(new InetSocketAddress(PORT), 0);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        server.createContext("/tasks", new TasksHandler(taskManager));
        server.createContext("/epics", new EpicsHandler(taskManager));
        server.createContext("/subtasks", new SubtasksHandler(taskManager));
        server.createContext("/history", new HistoryHandler(taskManager));
        server.createContext("/prioritized", new PrioritizedHandler(taskManager));

    }

    public void start() {
        server.start();
        System.out.println("HTTP-сервер запущен на " + PORT + " порту!");

    }

    public void stop() {
        server.stop(0);
    }

    public Gson gsonCreate() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.serializeNulls();
        gsonBuilder.registerTypeAdapter(Duration.class, new DurationAdapter());
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter());
        gsonBuilder.setPrettyPrinting();
        return gsonBuilder.create();
    }

    public static void main(String[] args)  {

        File file = new File("resources/task.csv");
        TaskManager fileBackedTaskManager = new FileBackedTaskManager(file);
        HttpTaskServer taskServer = new HttpTaskServer(fileBackedTaskManager);
        taskServer.start();

        taskManager.makeTask(new Task("Помыть посуду",
                "помыть посуду горячей водой", "12.12.2023 20:00", 40));

        taskManager.makeEpic(new Epic("Переезд",
                "Переехать в другую квартиру"));

        taskManager.makeSubtask(new Subtask(2, "Собрать коробки",
                "собрать в коробки вещи", "12.12.2023 15:00", 30));

        taskManager.makeSubtask(new Subtask(2, "Собрать коробки2",
                "собрать в коробки вещи2", "12.12.2023 16:00", 25));

        taskManager.makeTask(new Task("Помыть посуду2",
                "помыть посуду горячей водой2", "12.12.2023 14:00", 40));

        taskManager.makeEpic(new Epic("Переезд3",
                "Переехать в другую квартиру3"));
    }

     class TasksHandler implements HttpHandler {
         TaskManager taskManager;

         public TasksHandler(TaskManager taskManager) {
             this.taskManager = taskManager;
         }

         @Override
         public void handle(HttpExchange exchange) {
             try {
                 String endpoint = exchange.getRequestMethod();
                 switch (endpoint.toLowerCase()) {
                     case "get": {
                         getTasks(exchange);
                         break;
                     }
                     case "post": {
                         postTasks(exchange);
                         break;
                     }
                     case "delete": {
                         deleteTasks(exchange);
                         break;
                     }
                     default:
                         writeResponse(exchange,
                                 "Такого эндпоинта не существует" + endpoint, 404);
                 }
             } catch (IOException e) {
                 throw new RuntimeException(e);
             }

         }

         private void getTasks(HttpExchange exchange) throws IOException {
             String[] pathParts = exchange.getRequestURI().getPath().split("/");
             if (pathParts.length < 3) {
                 writeResponse(exchange, gson.toJson(taskManager.getTasks()), 200);
             } else {
                 Optional<Integer> taskIdOpt = getId(exchange);
                 if (taskIdOpt.isEmpty()) {
                     writeResponse(exchange, "Такой задачи нет", 404);
                 } else {
                     int taskId = taskIdOpt.get();
                     if (taskManager.getMapTasks().get(taskId) != null) {
                         writeResponse(exchange, gson.toJson(taskManager.getTaskById(taskId)), 200);
                     } else {
                         writeResponse(exchange, "Такой задачи нет", 404);
                     }
                 }
             }
         }

         private void postTasks(HttpExchange exchange) throws IOException {
             InputStream inputStream = exchange.getRequestBody();
             String body = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
             Task task = gson.fromJson(body, Task.class);
             if ((task.getId() != 0) && (taskManager.getMapTasks().get(task.getId()) != null)) {
                 if (taskManager.updateTask(task) != null) {
                     writeResponse(exchange, "Задача обновлена", 200);
                 } else {
                     writeResponse(exchange, "Задача не обновлена. " +
                             "Задача пересекается с существующими", 406);
                 }
             } else  if (task.getId() == 0) {
                 if (taskManager.makeTask(task) != null) {
                     writeResponse(exchange, "Задача создана", 201);
                 } else {
                     writeResponse(exchange, "Задача не создана. " +
                             "Задача пересекается с существующими", 406);
                 }
             } else {
                 writeResponse(exchange, "Неверно указан номер задачи ", 406);
             }
         }

         private void deleteTasks(HttpExchange exchange) throws IOException {
             String[] pathParts = exchange.getRequestURI().getPath().split("/");
             if (pathParts.length < 3) {
                 taskManager.clearTasks();
                 writeResponse(exchange, "Все задачи удалены", 204);
             } else {
                 Optional<Integer> taskIdOpt = getId(exchange);
                 if (taskIdOpt.isEmpty()) {
                     writeResponse(exchange, "Такой задачи нет", 404);
                 } else {
                     int taskId = taskIdOpt.get();
                     if (taskManager.getMapTasks().get(taskId) != null) {
                         taskManager.deleteTask(taskId);
                         writeResponse(exchange, "Задача удалена", 204);
                     } else {
                         writeResponse(exchange, "Такой задачи не существует", 404);
                     }
                 }
             }
         }
     }

    class EpicsHandler implements HttpHandler {
        TaskManager taskManager;

        public EpicsHandler(TaskManager taskManager) {
            this.taskManager = taskManager;
        }

        @Override
        public void handle(HttpExchange exchange) {
            try {
                String endpoint = exchange.getRequestMethod();
                switch (endpoint.toLowerCase()) {
                    case "get": {
                        getEpics(exchange);
                        break;
                    }
                    case "post": {
                        postEpics(exchange);
                        break;
                    }
                    case "delete": {
                        deleteEpics(exchange);
                        break;
                    }
                    default:
                        writeResponse(exchange,
                                "Такого эндпоинта не существует" + endpoint, 404);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        private void getEpics(HttpExchange exchange) throws IOException {
            String[] pathParts = exchange.getRequestURI().getPath().split("/");
            if (pathParts.length < 3) {
                writeResponse(exchange, gson.toJson(taskManager.getEpics()), 200);
            } else {
                Optional<Integer> taskIdOpt = getId(exchange);
                if (taskIdOpt.isEmpty()) {
                    writeResponse(exchange, "Такого эпика нет", 404);
                } else {
                    int taskId = taskIdOpt.get();
                    if (taskManager.getMapEpics().get(taskId) != null) {
                        if (pathParts.length < 4) {
                            writeResponse(exchange, gson.toJson(taskManager.getEpicById(taskId)), 200);
                        } else if (pathParts[3].equals("subtasks")) {
                            writeResponse(exchange, gson.toJson(taskManager.getEpicById(taskId).epicSubtasks),
                                    200);
                        } else {
                            writeResponse(exchange, "Неверный запрос", 404);
                        }
                    } else {
                        writeResponse(exchange, "Такого эпика нет", 404);
                    }
                }
            }
        }

        private void postEpics(HttpExchange exchange) throws IOException {
            InputStream inputStream = exchange.getRequestBody();
            String body = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
            Epic epic = gson.fromJson(body, Epic.class);
                if (taskManager.makeEpic(new Epic(epic.getName(), epic.getDescription())) != null) {
                    writeResponse(exchange, "Эпик создан", 201);
                } else {
                    writeResponse(exchange, "Эпик не создан.", 404);
                }
        }

        private void deleteEpics(HttpExchange exchange) throws IOException {
            String[] pathParts = exchange.getRequestURI().getPath().split("/");
            if (pathParts.length < 3) {
                taskManager.clearEpics();
                writeResponse(exchange, "Все эпики удалены", 200);
            } else {
                Optional<Integer> taskIdOpt = getId(exchange);
                if (taskIdOpt.isEmpty()) {
                    writeResponse(exchange, "Такого эпика нет", 404);
                } else {
                    int taskId = taskIdOpt.get();
                    if (taskManager.getMapEpics().get(taskId) != null) {
                        taskManager.deleteEpic(taskId);
                        writeResponse(exchange, "Эпик удален", 200);
                    } else {
                        writeResponse(exchange, "Такого эпика не существует", 404);
                    }
                }
            }
        }
    }

    class SubtasksHandler implements HttpHandler {
        TaskManager taskManager;

        public SubtasksHandler(TaskManager taskManager) {
            this.taskManager = taskManager;
        }

        @Override
        public void handle(HttpExchange exchange) {
            try {
                String endpoint = exchange.getRequestMethod();
                switch (endpoint.toLowerCase()) {
                    case "get": {
                        getSubtasks(exchange);
                        break;
                    }
                    case "post": {
                        postSubtasks(exchange);
                        break;
                    }
                    case "delete": {
                        deleteSubtasks(exchange);
                        break;
                    }
                    default:
                        writeResponse(exchange,
                                "Такого эндпоинта не существует" + endpoint, 404);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }

        private void getSubtasks(HttpExchange exchange) throws IOException {
            String[] pathParts = exchange.getRequestURI().getPath().split("/");
            if (pathParts.length < 3) {
                writeResponse(exchange, gson.toJson(taskManager.getSubtasks()), 200);
            } else {
                Optional<Integer> taskIdOpt = getId(exchange);
                if (taskIdOpt.isEmpty()) {
                    writeResponse(exchange, "Такой субзадачи нет", 404);
                } else {
                    int taskId = taskIdOpt.get();
                    if (taskManager.getMapSubtasks().get(taskId) != null) {
                        writeResponse(exchange, gson.toJson(taskManager.getSubtaskById(taskId)), 200);
                    } else {
                        writeResponse(exchange, "Такой субзадачи нет", 404);
                    }
                }
            }
        }

        private void postSubtasks(HttpExchange exchange) throws IOException {
            InputStream inputStream = exchange.getRequestBody();
            String body = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
            Subtask subtask = gson.fromJson(body, Subtask.class);
            if ((subtask.getId() != 0) && (taskManager.getMapSubtasks().get(subtask.getId()) != null)) {
                if (taskManager.updateSubtask(subtask) != null) {
                    writeResponse(exchange, "СубЗадача обновлена", 201);
                } else {
                    writeResponse(exchange, "СубЗадача не обновлена. " +
                            "СубЗадача пересекается с существующими", 406);
                }
            } else if ((subtask.getId() == 0)) {
                if (taskManager.makeSubtask(subtask) != null) {
                    writeResponse(exchange, "СубЗадача создана", 201);
                } else {
                    writeResponse(exchange, "СубЗадача не создана. " +
                            "СубЗадача пересекается с существующими", 406);
                }
            } else {
                writeResponse(exchange, "Неверно указан номер СубЗадачи", 406);
            }
        }

        private void deleteSubtasks(HttpExchange exchange) throws IOException {
            String[] pathParts = exchange.getRequestURI().getPath().split("/");
            if (pathParts.length < 3) {
                writeResponse(exchange, "Не задан номер СубЗадачи", 404);
            } else {
                Optional<Integer> taskIdOpt = getId(exchange);
                if (taskIdOpt.isEmpty()) {
                    writeResponse(exchange, "Такой СубЗадачи нет", 404);
                } else {
                    int taskId = taskIdOpt.get();
                    if (taskManager.getMapSubtasks().get(taskId) != null) {
                        taskManager.deleteSubtask(taskId);
                        writeResponse(exchange, "СубЗадача удалена", 200);
                    } else {
                        writeResponse(exchange, "Такой СубЗадачи нет", 404);
                    }
                }
            }
        }
    }

    class HistoryHandler implements HttpHandler {
        TaskManager taskManager;

        public HistoryHandler(TaskManager taskManager) {
            this.taskManager = taskManager;
        }

        @Override
        public void handle(HttpExchange exchange) {
            try {
                String endpoint = exchange.getRequestMethod();
                if (endpoint.equalsIgnoreCase("get")) {
                    getHistory(exchange);
                } else {
                    writeResponse(exchange,
                            "Такого эндпоинта не существует" + endpoint, 404);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }

        private void getHistory(HttpExchange exchange) throws IOException {
            String[] pathParts = exchange.getRequestURI().getPath().split("/");
            if (pathParts.length < 3) {
                writeResponse(exchange, gson.toJson(taskManager.getHistory()), 200);
            } else {
                writeResponse(exchange, "Такой страницы нет", 404);
            }
        }
    }

    class PrioritizedHandler implements HttpHandler {
        TaskManager taskManager;

        public PrioritizedHandler(TaskManager taskManager) {
            this.taskManager = taskManager;
        }

        @Override
        public void handle(HttpExchange exchange) {
            try {
                String endpoint = exchange.getRequestMethod();
                if (endpoint.equalsIgnoreCase("get")) {
                    getPrioritized(exchange);
                } else {
                    writeResponse(exchange,
                            "Такого эндпоинта не существует" + endpoint, 404);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        private void getPrioritized(HttpExchange exchange) throws IOException {
            String[] pathParts = exchange.getRequestURI().getPath().split("/");
            if (pathParts.length < 3) {
                writeResponse(exchange, gson.toJson(taskManager.getPrioritizedTasks()), 200);
            } else {
                writeResponse(exchange, "Такой страницы нет", 404);
            }
        }
    }

    private Optional<Integer> getId(HttpExchange exchange) {
        String[] pathParts = exchange.getRequestURI().getPath().split("/");
        try {
            return Optional.of(Integer.parseInt(pathParts[2]));
        } catch (NumberFormatException exception) {
            return Optional.empty();
        }
    }

    private void writeResponse(HttpExchange exchange,
                               String responseString,
                               int responseCode) throws IOException {
        try (OutputStream os = exchange.getResponseBody()) {
            exchange.sendResponseHeaders(responseCode, 0);
            os.write(responseString.getBytes(DEFAULT_CHARSET));
            System.out.println(responseCode + "/n" + responseString);
        } catch (IOException e) {
            exchange.sendResponseHeaders(500, 0);
        }
        exchange.close();
    }
}
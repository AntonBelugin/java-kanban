package service;

import convertors.EpicConvertor;
import convertors.SubtaskConvertor;
import convertors.TaskConvertor;
import task.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FileBackedTaskManager extends InMemoryTaskManager {

    private static File file;
    private static Map<Integer, Task> allTasks = new HashMap<>();

    public FileBackedTaskManager(File file) {
        super(new InMemoryHistoryManager());
        FileBackedTaskManager.file = file;
    }

    public static void main(String[] args) {
        File file = new File("resources/task.csv");
        TaskManager fileBackedTaskManager = new FileBackedTaskManager(file);

        fileBackedTaskManager.makeTask(new Task("Помыть посуду", "помыть посуду горячей водой"));
        fileBackedTaskManager.makeEpic(new Epic("Переезд", "Переехать в другую квартиру"));
        fileBackedTaskManager.makeSubtask(new Subtask(2, "Собрать коробки",
                "собрать в коробки вещи"));
        fileBackedTaskManager.getTaskById(1);
        fileBackedTaskManager.getEpicById(2);
        fileBackedTaskManager.getSubtaskById(3);

        printHistory(fileBackedTaskManager);

        TaskManager taskManager2 = loadFromFile(file);
        taskManager2.getTaskById(1);
        printHistory(taskManager2);

        System.out.println(fileBackedTaskManager.getEpics());
        System.out.println(taskManager2.getEpics());
    }

    @Override
    public List<Task> getHistory() {
        return super.getHistory();
    }

    @Override
    public Task makeTask(Task task) {
        Task taskNew = super.makeTask(task);
        save();
        return taskNew;
    }

    @Override
    public List<Task> getTasks() {
        return super.getTasks();
    }

    @Override
    public void clearTasks() {
        super.clearTasks();
        save();
    }

    @Override
    public Task getTaskById(int id) {
        Task task = super.getTaskById(id);
        save();
        return task;
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public void deleteTask(int id) {
        super.deleteTask(id);
        save();
    }

    @Override
    public Epic makeEpic(Epic epic) {
        Epic epicNew = super.makeEpic(epic);
        save();
        return epicNew;
    }

    @Override
    public List<Epic> getEpics() {
        return super.getEpics();
    }

    @Override
    public void clearEpics() {
        super.clearEpics();
        save();
    }

    @Override
    public Epic getEpicById(int id) {
        Epic epic = super.getEpicById(id);
        save();
        return epic;
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
    }

    @Override
    public void deleteEpic(int id) {
        super.deleteEpic(id);
        save();
    }

    @Override
    public Subtask makeSubtask(Subtask subtask) {
        Subtask subtaskNew = super.makeSubtask(subtask);
        save();
        return subtaskNew;
    }

    @Override
    public List<Subtask> getSubtasks() {
        return super.getSubtasks();
    }

    @Override
    public void clearSubtasks() {
        super.clearSubtasks();
        save();
    }

    @Override
    public Subtask getSubtaskById(int id) {
        Subtask subtask = super.getSubtaskById(id);
        save();
        return subtask;
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        super.updateSubtask(subtask);
        save();
    }

    @Override
    public void deleteSubtask(int id) {
        super.deleteSubtask(id);
        save();
    }

    @Override
    public List<Subtask> getSubtasksByEpic(int id) {
        return super.getSubtasksByEpic(id);
    }

    private void save() {
        try (BufferedWriter writeFile = new BufferedWriter(new FileWriter(file, StandardCharsets.UTF_8))) {
            writeFile.write("id,type,name,status,description,epic\n");
            TaskConvertor taskConvertor = new TaskConvertor();
            EpicConvertor epicConvertor = new EpicConvertor();
            SubtaskConvertor subtaskConvertor = new SubtaskConvertor();
            for (Task task : tasks.values()) {
                    writeFile.write(taskConvertor.toString(task) + "\n");
                }
            for (Task epic : epics.values()) {
                writeFile.write(epicConvertor.toString(epic) + "\n");
            }
            for (Task subtask : subtasks.values()) {
                writeFile.write(subtaskConvertor.toString(subtask) + "\n");
            }
            writeFile.write(historyToString(historyManager));
        } catch (IOException e) {
          throw new ManagerSaveException("Произошла ошибка во время записи файла: " + file.getName(), e);
        }
    }

    static String historyToString(HistoryManager manager) {
        StringBuilder stringHistory = new StringBuilder();
        stringHistory.append("\n");
        for (int i = 0; i < manager.getHistoryList().size(); i++) {
            stringHistory.append(manager.getHistoryList().get(i).getId());
            if (i < (manager.getHistoryList().size() - 1)) {
                stringHistory.append(",");
            }
        }
        return stringHistory.toString();
    }

    static List<Integer> historyFromString(List<String> dataFromFile) {
        List<Integer> historyId = new ArrayList<>();
        String[] split = dataFromFile.get((dataFromFile.size() - 1)).split(",");
        for (String id : split) {
            historyId.add(Integer.parseInt(id));
        }
        return historyId;
    }

    static FileBackedTaskManager loadFromFile(File file) {
        try {
            FileBackedTaskManager fileBackedTaskManager2 = new FileBackedTaskManager(file);
            recovery(readFromFile(file));
            return fileBackedTaskManager2;
        } catch (IOException e) {
            System.out.println("Произошла ошибка во время чтения файла.");
            return null;
        }
    }

    private static List<String> readFromFile(File file) throws IOException {
        List<String> dataFromFile = new ArrayList<>();
       BufferedReader readFile = new BufferedReader(new FileReader(file, StandardCharsets.UTF_8));
            while (readFile.ready()) {
                dataFromFile.add(readFile.readLine());
            }
            return dataFromFile;
    }

    static void recovery(List<String> dataFromFile) {
        for (int i = 1; i < (dataFromFile.size() - 2); i++) {
            String[] taskData = dataFromFile.get(i).split(",");
            int id = Integer.parseInt(taskData[0]);
            TaskType taskType = TaskType.valueOf(taskData[1]);
            String name = taskData[2];
            TaskStatus status = TaskStatus.valueOf(taskData[3]);
            String description = taskData[4];
            switch (taskType) {
                case TASK:
                    Task task = new Task(name, description);
                    task.setId(id);
                    task.setTaskStatus(status);
                    putToTasks(task);
                    allTasks.put(task.getId(), task);
                    break;
                case EPIC:
                    Epic epic = new Epic(name, description);
                    epic.setId(id);
                    epic.setTaskStatus(status);
                    putToEpics(epic);
                    allTasks.put(epic.getId(), epic);
                    break;
                case SUBTASK:
                    Subtask subtask = new Subtask(Integer.parseInt(taskData[5]), name, description);
                    subtask.setId(id);
                    subtask.setTaskStatus(status);
                    putToSubtasks(subtask);
                    allTasks.put(subtask.getId(), subtask);
                    break;
            }
        }
        for (Subtask subtask : subtasks.values()) {
            epics.get(subtask.getEpicId()).epicSubtasks.add(subtask);
        }
        List<Integer> historyId = historyFromString(dataFromFile);
        for (Integer id : historyId) {
            historyManager.addHistoryView(allTasks.get(id));
        }
    }

    private static void printHistory(TaskManager taskManager) {
        for (Task task : taskManager.getHistory()) {
            System.out.println(task);
        }
        System.out.println();
    }
}
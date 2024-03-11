package service;

import task.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FileBackedTaskManager extends InMemoryTaskManager implements TaskManager {

    private String filename;
    private Map<Integer, Task> allTasks = new HashMap<>();

    public FileBackedTaskManager(HistoryManager historyManager, String filename) {
        super(historyManager);
        this.filename = filename;
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
        try (BufferedWriter writFile = new BufferedWriter(new FileWriter(filename, StandardCharsets.UTF_8))) {
            writFile.write("id,type,name,status,description,epic\n");
            for (Task task : tasks.values()) {
                writFile.write(taskToString(task) + "\n");
            }
            for (Task epic : epics.values()) {
                writFile.write(epicToString(epic) + "\n");
            }
            for (Task subtask : subtasks.values()) {
                writFile.write(subtaskToString(subtask) + "\n");
            }
            writFile.write(historyToString(historyManager));
        } catch (FileNotFoundException e) {
            System.out.println("Произошла ошибка во время записи файла.");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String taskToString(Task task) {
        return task.getId() + "," + task.getTaskType() + "," + task.getName() +
                "," + task.getTaskStatus() + "," + task.getDescription();
    }

    private String epicToString(Task epic) {
        return epic.getId() + "," + epic.getTaskType() + "," + epic.getName() +
                "," + epic.getTaskStatus() + "," + epic.getDescription();
    }

    private String subtaskToString(Task subtask) {
        return subtask.getId() + "," + subtask.getTaskType() + "," + subtask.getName() +
                "," + subtask.getTaskStatus() + "," + subtask.getDescription() + "," +
                subtask.getEpicId();
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

    static List<Integer> historyFromString(List<String> value) {
        List<Integer> historyId = new ArrayList<>();
        for (String string : value) {
            String[] split = string.split(",");
            for (String field : split) {
                historyId.add(Integer.parseInt(field));
            }
        }
        return historyId;
    }

    public void recovery(List<String> dataFromFile) {
        for (int i = 1; i < (dataFromFile.size() - 1); i++) {
            String[] taskData = dataFromFile.get(i).split(",");
            switch (TaskType.valueOf(taskData[1])) {
                case TASK:
                    Task task = new Task(taskData[2], taskData[4]);
                    task.setId(Integer.parseInt(taskData[0]));
                    task.setTaskStatus(TaskStatus.valueOf(taskData[1]));
                    putToTasks(task);
                    allTasks.put(task.getId(), task);
                    break;
                case EPIC:
                    Epic epic = new Epic(taskData[2], taskData[4]);
                    epic.setId(Integer.parseInt(taskData[0]));
                    epic.setTaskStatus(TaskStatus.valueOf(taskData[1]));
                    putToEpics(epic);
                    allTasks.put(epic.getId(), epic);
                    break;
                case SUBTASK:
                    Subtask subtask = new Subtask(Integer.parseInt(taskData[5]), taskData[2], taskData[4]);
                    subtask.setId(Integer.parseInt(taskData[0]));
                    subtask.setTaskStatus(TaskStatus.valueOf(taskData[1]));
                    putToSubtasks(subtask);
                    allTasks.put(subtask.getId(), subtask);
                    break;
            }
        }
        for (Subtask subtask : subtasks.values()) {
            epics.get(subtask.getEpicId()).epicSubtasks.add(subtask);
        }
        try {
            for (Integer id : historyFromString(readFromFile(filename))) {
                historyManager.addHistoryView(allTasks.get(id));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static List<String> readFromFile(String filename) throws IOException {
        List<String> dataFromFile = new ArrayList<>();
        try (BufferedReader readFile = new BufferedReader(new FileReader(filename, StandardCharsets.UTF_8))) {
            int i = 0;
            while (readFile.ready()) {
                dataFromFile.add(readFile.readLine());
            }
            return dataFromFile;
        } catch (FileNotFoundException e) {
            System.out.println("Произошла ошибка во время чтения файла.");
            return dataFromFile;
        }

    }


}





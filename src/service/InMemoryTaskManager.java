package service;

import task.Epic;
import task.Subtask;
import task.Task;
import task.TaskStatus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryTaskManager implements TaskManager {
    protected int id = 0;
    protected static Map<Integer, Task> tasks;
    protected static Map<Integer, Epic> epics;
    protected static Map<Integer, Subtask> subtasks;
    protected static HistoryManager historyManager;

    public InMemoryTaskManager(HistoryManager historyManager) {
        this.tasks = new HashMap<>();
        this.epics = new HashMap<>();
        this.subtasks = new HashMap<>();
        this.historyManager = historyManager;
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistoryList();
    }

    // Методы для Task

    @Override
    public Task makeTask(Task task) {
        task.setId(generateId());
        task.setTaskStatus(TaskStatus.NEW);
        tasks.put(task.getId(), task);
        return task;
    }

    @Override
    public List<Task> getTasks() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public void clearTasks() {
        for (Integer id : tasks.keySet()) {
            historyManager.removeNode(id);
        }
        tasks.clear();
    }

    @Override
    public Task getTaskById(int id) {
        historyManager.addHistoryView(tasks.get(id));
        return tasks.get(id);
    }

    @Override
    public void updateTask(Task task) {
        Task saved = tasks.get(task.getId());
        saved.name = task.name;
        saved.description = task.description;
        saved.setTaskStatus(task.getTaskStatus());
        tasks.put(task.getId(), saved);
    }

    @Override
    public void deleteTask(int id) {
        tasks.remove(id);
        historyManager.removeNode(id);
    }

    // Методы для Epic
    @Override
    public Epic makeEpic(Epic epic) {
        epic.setId(generateId());
        epic.setTaskStatus(TaskStatus.NEW);
        epics.put(epic.getId(), epic);
        return epic;
    }

    @Override
    public List<Epic> getEpics() {
        return new ArrayList<>(epics.values());
    }

    @Override
    public void clearEpics() {
        for (Integer id : epics.keySet()) {
            for (Subtask subtask : epics.get(id).epicSubtasks) {
                historyManager.removeNode(subtask.getId());
            }
            historyManager.removeNode(id);
        }
        epics.clear();
    }

    @Override
    public Epic getEpicById(int id) {
        historyManager.addHistoryView(epics.get(id));
        return epics.get(id);
    }

    @Override
    public void updateEpic(Epic epic) {
        Epic saved = epics.get(epic.getId());
        saved.name = epic.name;
        saved.description = epic.description;
        epics.put(epic.getId(), saved);
    }

    @Override
    public void deleteEpic(int id) {
        for (Subtask subtask : epics.get(id).epicSubtasks) {
            historyManager.removeNode(subtask.getId());
        }
        epics.remove(id);
        historyManager.removeNode(id);
    }

    // Методы для Task.Subtask

    @Override
    public Subtask makeSubtask(Subtask subtask) {
        subtask.setId(generateId());
        subtask.setTaskStatus(TaskStatus.NEW);
        Epic epic = epics.get(subtask.getEpicId());
        epic.epicSubtasks.add(subtask);
        epic.setTaskStatus(countStatus(epic));
        epics.put(epic.getId(), epic);
        subtasks.put(subtask.getId(), subtask);
        return subtask;
    }

    @Override
    public List<Subtask> getSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    @Override
    public void clearSubtasks() {
        for (Integer id : subtasks.keySet()) {
            historyManager.removeNode(id);
        }
        subtasks.clear();
        for (Epic epic: epics.values()) {
            epic.setTaskStatus(TaskStatus.NEW);
            epic.epicSubtasks.clear();
        }
    }

    @Override
    public Subtask getSubtaskById(int id) {
        historyManager.addHistoryView(subtasks.get(id));
        return subtasks.get(id);
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        Epic epic = epics.get(subtask.getEpicId());
        epic.epicSubtasks.remove(getSubtaskById(subtask.getId()));
        epic.epicSubtasks.add(subtask);
        epic.setTaskStatus(countStatus(epic));
        epics.put(epic.getId(), epic);
        subtasks.put(subtask.getId(), subtask);
    }

    @Override
    public void deleteSubtask(int id) {
        Subtask subtask = subtasks.get(id);
        Epic epic = epics.get(subtask.getEpicId());
        epic.epicSubtasks.remove(subtask);
        subtasks.remove(id);
        epic.setTaskStatus(countStatus(epic));
        epics.put(epic.getId(), epic);
        historyManager.removeNode(id);
    }

    @Override
    public List<Subtask> getSubtasksByEpic(int id) {
        return getEpicById(id).epicSubtasks;
    }

    private int generateId() {
        return ++id;
    }

    //Метод для рассчета статуса Epic
    private TaskStatus countStatus(Epic epic) {
        List<Subtask> subtasksEpic = epic.epicSubtasks;
        if (subtasksEpic.isEmpty()) {
            return TaskStatus.NEW;
        } else {
            int statusNew = 0;
            int statusDone = 0;
            int statusInProgress = 0;
            for (Subtask subtask: subtasksEpic) {
                switch (subtask.getTaskStatus()) {
                    case NEW:
                        statusNew += 1;
                        break;
                    case DONE:
                        statusDone += 1;
                        break;
                    case IN_PROGRESS:
                        statusInProgress += 1;
                        break;
                }
            }
            if (statusDone < 1 && statusInProgress < 1) {
                return TaskStatus.NEW;
            } else if (statusNew < 1 && statusInProgress < 1) {
                return TaskStatus.DONE;
            } else {
                return TaskStatus.IN_PROGRESS;
            }
        }
    }

    protected static void putToTasks(Task task) {
        tasks.put(task.getId(), task);
    }

    protected static void putToEpics(Epic epic) {
        epics.put(epic.getId(), epic);
    }

    protected static void putToSubtasks(Subtask subtask) {
        subtasks.put(subtask.getId(), subtask);
    }
}
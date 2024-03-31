package service;

import task.Epic;
import task.Subtask;
import task.Task;
import task.TaskStatus;

import java.util.*;

public class InMemoryTaskManager implements TaskManager {
    protected int id = 0;
    protected static Map<Integer, Task> tasks;
    protected static Map<Integer, Epic> epics;
    protected static Map<Integer, Subtask> subtasks;
    protected static HistoryManager historyManager;
    protected static TreeSet<Task> priorityTask = new TreeSet<>();

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
        if (isTimeTaskFree(task)) {
            task.setId(generateId());
            task.setTaskStatus(TaskStatus.NEW);
            tasks.put(task.getId(), task);
            priorityTask.add(task);
            return task;
        } else {
            System.out.println("Время начала задачи не удовлетворяет условиям");
            return null;
        }
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
        for (Task task: tasks.values()) {
            priorityTask.remove(task);
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
        if (isTimeTaskFree(task)) {
            Task saved = tasks.get(task.getId());
            saved.name = task.name;
            saved.description = task.description;
            saved.setTaskStatus(task.getTaskStatus());
            priorityTask.remove(task);
            priorityTask.add(task);
            tasks.put(task.getId(), saved);
        } else {
            System.out.println("Время начала задачи не удовлетворяет условиям");
        }
    }

    @Override
    public void deleteTask(int id) {
        priorityTask.remove(tasks.get(id));
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
                priorityTask.remove(subtask);
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
            priorityTask.remove(subtask);
            historyManager.removeNode(subtask.getId());
        }
        epics.remove(id);
        historyManager.removeNode(id);
    }

    // Методы для Task.Subtask

    @Override
    public Subtask makeSubtask(Subtask subtask) {
        if (isTimeTaskFree(subtask)) {
            subtask.setId(generateId());
            subtask.setTaskStatus(TaskStatus.NEW);
            Epic epic = epics.get(subtask.getEpicId());
            epic.epicSubtasks.add(subtask);
            epic.setParameters(epic);
            epics.put(epic.getId(), epic);
            priorityTask.add(subtask);
            subtasks.put(subtask.getId(), subtask);
            return subtask;
        } else {
            System.out.println("Время начала задачи не удовлетворяет условиям");
            return null;
        }
    }

    @Override
    public List<Subtask> getSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    @Override
    public void clearSubtasks() {
        for (Integer id : subtasks.keySet()) {
            for (Subtask subtask: subtasks.values()) {
                priorityTask.remove(subtask);
            }
            historyManager.removeNode(id);
        }
        subtasks.clear();
        for (Epic epic: epics.values()) {
            epic.setParameters(epic);
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
        priorityTask.remove(getSubtaskById(subtask.getId()));
        if (isTimeTaskFree(subtask)) {
            Epic epic = epics.get(subtask.getEpicId());
            epic.epicSubtasks.remove(getSubtaskById(subtask.getId()));
            epic.epicSubtasks.add(subtask);
            epic.setParameters(epic);
            priorityTask.add(subtask);
            epics.put(epic.getId(), epic);
            subtasks.put(subtask.getId(), subtask);
        } else {
            System.out.println("Время начала задачи не удовлетворяет условиям");
        }
    }

    @Override
    public void deleteSubtask(int id) {
        priorityTask.remove(getSubtaskById(id));
        Subtask subtask = subtasks.get(id);
        Epic epic = epics.get(subtask.getEpicId());
        epic.epicSubtasks.remove(subtask);
        subtasks.remove(id);
        epic.setParameters(epic);
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

    protected static void putToTasks(Task task) {
        tasks.put(task.getId(), task);
    }

    protected static void putToEpics(Epic epic) {
        epics.put(epic.getId(), epic);
    }

    protected static void putToSubtasks(Subtask subtask) {
        subtasks.put(subtask.getId(), subtask);
    }

    protected static void putToPriority(Task task) {
        priorityTask.add(task);
    }

    public TreeSet<Task> getPrioritizedTasks() {
        return priorityTask;
    }

    public boolean isTimeTaskFree(Task newTask) {
        return priorityTask.stream()
                .filter(task -> !task.getEndTime().isBefore(newTask.getStartTime()))
                .noneMatch(task -> task.getStartTime().isBefore(newTask.getEndTime()));
    }

}
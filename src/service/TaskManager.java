package service;

import task.TaskStatus;
import task.Subtask;
import task.Task;
import task.Epic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TaskManager {
    private int id = 0;
    private Map<Integer, Task> tasks;
    private Map<Integer, Epic> epics;
    private Map<Integer, Subtask> subtasks;

    public TaskManager() {
        this.tasks = new HashMap<>();
        this.epics = new HashMap<>();
        this.subtasks = new HashMap<>();
    }

    // Методы для Task

    public Task makeTask(Task task) {
        task.setId(generateId());
        task.setTaskStatus(TaskStatus.NEW);
        tasks.put(task.getId(), task);
        return task;
    }

    public List<Task> getTasks() {
        return new ArrayList<>(tasks.values());
    }

    public void clearTasks() {
        tasks.clear();
    }

    public Task getTaskById(int id) {
        return tasks.get(id);
    }

    public void updateTask(Task task) {
        Task saved = tasks.get(task.getId());
        saved.name = task.name;
        saved.description = task.description;
        saved.setTaskStatus(task.getTaskStatus());
        tasks.put(task.getId(), saved);
    }

    public void deleteTask(int id) {
        tasks.remove(id);
    }

    // Методы для Epic
    public Epic makeEpic(Epic epic) {
        epic.setId(generateId());
        epic.setTaskStatus(TaskStatus.NEW);
        epics.put(epic.getId(), epic);
        return epic;
    }

    public List<Epic> getEpics() {
        return new ArrayList<>(epics.values());
    }

    public void clearEpics() {
        epics.clear();
    }

    public Epic getEpicById(int id) {
        return epics.get(id);
    }

    public void updateEpic(Epic epic) {
        Epic saved = epics.get(epic.getId());
        saved.name = epic.name;
        saved.description = epic.description;
        epics.put(epic.getId(), saved);
    }

    public void deleteEpic(int id) {
        epics.remove(id);
    }


    // Методы для Task.Subtask
    public Subtask makeSubtask(Subtask subtask) {
        subtask.setId(generateId());
        subtask.setTaskStatus(TaskStatus.NEW);
        Epic epic = subtask.getEpic();
        epic.epicSubtasks.add(subtask);
        epic.setTaskStatus(countStatus(epic));
        epics.put(epic.getId(), epic);
        subtasks.put(subtask.getId(), subtask);
        return subtask;
    }

    public List<Subtask> getSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    public void clearSubtasks() {
        subtasks.clear();
        for (Epic epic: epics.values()) {
            epic.setTaskStatus(TaskStatus.NEW);
            epic.epicSubtasks.clear();
        }

    }

    public Subtask getSubtaskById(int id) {
        return subtasks.get(id);
    }

    public void updateSubtask(Subtask subtask) {
        Epic epic = subtask.getEpic();
        epic.epicSubtasks.remove(getSubtaskById(subtask.getId()));
        epic.epicSubtasks.add(subtask);
        epic.setTaskStatus(countStatus(epic));
        epics.put(epic.getId(), epic);
        subtasks.put(subtask.getId(), subtask);
    }

    public void deleteSubtask(int id) {
        Subtask subtask = subtasks.get(id);
        Epic epic = subtask.getEpic();
        epic.epicSubtasks.remove(subtask);
        subtasks.remove(id);
        epic.setTaskStatus(countStatus(epic));
        epics.put(epic.getId(), epic);
    }
    public List<Subtask> getSubtasksByEpic(int id) {
        return getEpicById(id).epicSubtasks;
    }

    private int generateId() { return ++id; }

    //Метод для рассчета статуса Epic
    private TaskStatus countStatus(Epic epic) {
        List<Subtask> subtasksEpic = epic.epicSubtasks;
        if (subtasksEpic.isEmpty()) {
            return TaskStatus.NEW;
        } else {
            int statusNew = 0;
            int statusDone = 0;
            int statusIn_progress = 0;
            for (Subtask subtask: subtasksEpic) {
                switch (subtask.getTaskStatus()) {
                    case NEW:
                        statusNew +=1;
                        break;
                    case DONE:
                        statusDone +=1;
                        break;
                    case IN_PROGRESS:
                        statusIn_progress +=1;
                        break;
                }
            }
            if (statusDone < 1 && statusIn_progress < 1) {
                return TaskStatus.NEW;
            } else if (statusNew < 1 && statusIn_progress < 1) {
                return TaskStatus.DONE;
            } else {
                return TaskStatus.IN_PROGRESS;
            }
        }
    }
}
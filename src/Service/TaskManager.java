package Service;

import Task.TaskStatus;
import Task.Subtask;
import Task.Task;
import Task.Epic;

import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {
    private int id = 1;
    HashMap<Integer, Task> tasks;
    HashMap<Integer, Epic> epics;
    HashMap<Integer, Subtask> subtasks;

    public TaskManager() {
        this.tasks = new HashMap<>();
        this.epics = new HashMap<>();
        this.subtasks = new HashMap<>();
    }

    private int generateId() { return ++id; }

    // Методы для Task.Task

    public Task makeTask(Task task) {
        task.setId(generateId());
        task.setTaskStatus(TaskStatus.NEW);
        tasks.put(task.getId(), task);
        return task;
    }

    public ArrayList<Task> getTasks() {
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
        updateTask.id = id;
        updateTask.taskStatus = taskStatus;
        tasks.put(updateTask.getId(), updateTask);
    }

    public void deleteTask(int id) {
        tasks.remove(id);
    }

    // Методы для Task.Epic
    public void makeEpic(Epic epic) {
        epic.id = id;
        epic.taskStatus = TaskStatus.NEW;
        epics.put(id, epic);
        id +=1;
    }
    public ArrayList<Epic> getEpics() {
        return new ArrayList<>(epics.values());
    }

    public void clearEpics() {
        epics.clear();
    }

    public Epic getEpicById(int id) {
        return epics.get(id);
    }

    public void updateEpic(int id, Epic updateEpic) {
        ArrayList<Subtask> tempList = epics.get(id).epicSubtasks;
        updateEpic.id = id;
        updateEpic.epicSubtasks = tempList;
        updateEpic.taskStatus = countStatus(updateEpic);
        epics.put(updateEpic.getId(), updateEpic);
    }

    public void deleteEpic(int id) {
        epics.remove(id);
    }

    //Метод для рассчета статуса Task.Epic

    public TaskStatus countStatus(Epic epic) {
        ArrayList<Subtask> subtasksEpic = epic.epicSubtasks;
        if (subtasksEpic.isEmpty()) {
            return TaskStatus.NEW;
        } else {
            int statusNew = 0;
            int statusDone = 0;
            int statusIn_progress = 0;
            for (Subtask subtask: subtasksEpic) {
                switch (subtask.taskStatus) {
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


    // Методы для Task.Subtask
    public void makeSubtask(Subtask subtask, int epicId) {
        subtask.id = id;
        subtask.epicId = epicId;
        subtask.taskStatus = TaskStatus.NEW;
        subtasks.put(id, subtask);
        Epic epic = epics.get(epicId);
        epic.epicSubtasks.add(subtask);
        epic.taskStatus = countStatus(epic);
        epics.put(epic.getId(), epic);
        id +=1;
    }

    public ArrayList<Subtask> getSubtaskById() {
        return new ArrayList<>(subtasks.values());
    }

    public void clearSubtasks() {
        subtasks.clear();
        for (Epic epic: epics.values()) {
            epic.taskStatus = TaskStatus.NEW;
            epic.epicSubtasks.clear();
        }

    }

    public Subtask getIdSubtask(int id) {
        return subtasks.get(id);
    }

    public void updateSubtask(int id, Subtask updateSubtask, TaskStatus subtaskStatus) {
        Epic epic = epics.get(getIdSubtask(id).epicId);
        epic.epicSubtasks.remove(getIdSubtask(id));
        updateSubtask.id = id;
        updateSubtask.epicId = epic.id;
        updateSubtask.taskStatus = subtaskStatus;
        epic.epicSubtasks.add(updateSubtask);
        subtasks.put(id, updateSubtask);
        epic.taskStatus = countStatus(epic);
        epics.put(epic.getId(), epic);
    }

    public void deleteSubtask(int id) {
        Subtask subtask = subtasks.get(id);
        Epic epic = epics.get(subtask.epicId);
        epic.epicSubtasks.remove(subtask);
        subtasks.remove(id);
        epic.taskStatus = countStatus(epic);
        epics.put(epic.getId(), epic);
    }
    public ArrayList<Subtask> getSubtasksEpic(int id) {
        return getEpicById(id).epicSubtasks;
    }
}

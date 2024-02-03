package service;

import task.Epic;
import task.Subtask;
import task.Task;

import java.util.ArrayList;
import java.util.List;

public interface TaskManager {
    Task makeTask(Task task);

    List<Task> getTasks();

    void clearTasks();

    Task getTaskById(int id);

    void updateTask(Task task);

    void deleteTask(int id);

    // Методы для Epic
    Epic makeEpic(Epic epic);

    List<Epic> getEpics();

    void clearEpics();

    Epic getEpicById(int id);

    void updateEpic(Epic epic);

    void deleteEpic(int id);

    // Методы для Task.Subtask
    Subtask makeSubtask(Subtask subtask);

    List<Subtask> getSubtasks();

    void clearSubtasks();

    Subtask getSubtaskById(int id);

    void updateSubtask(Subtask subtask);

    void deleteSubtask(int id);

   List<Subtask> getSubtasksByEpic(int id);

    List<Task> getHistory();
}

package service;

import task.Epic;
import task.Subtask;
import task.Task;

import java.util.List;
import java.util.Map;
import java.util.TreeSet;

public interface TaskManager {
    Task makeTask(Task task);

    Map<Integer, Task> getTasks();

    void clearTasks();

    Task getTaskById(int id);

    Task updateTask(Task task);

    void deleteTask(int id);

    // Методы для Epic
    Epic makeEpic(Epic epic);

    Map<Integer, Epic> getEpics();

    void clearEpics();

    Epic getEpicById(int id);

    Epic updateEpic(Epic epic);

    void deleteEpic(int id);

    // Методы для Task.Subtask
    Subtask makeSubtask(Subtask subtask);

    Map<Integer, Subtask> getSubtasks();

    void clearSubtasks();

    Subtask getSubtaskById(int id);

    Subtask updateSubtask(Subtask subtask);

    void deleteSubtask(int id);

   List<Subtask> getSubtasksByEpic(int id);

    List<Task> getHistory();

    TreeSet<Task> getPrioritizedTasks();

}

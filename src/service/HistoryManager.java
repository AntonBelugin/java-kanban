package service;

import task.Task;

import java.util.List;

public interface HistoryManager {
    void addHistoryView(Task task);

    void removeNode(int id);

    List<Task> getHistoryList();
}

package service;

import task.Task;

import java.util.List;

public interface HistoryManager {
    void addHistoryView(Task task);
    List<Task> getHistoryList();
}

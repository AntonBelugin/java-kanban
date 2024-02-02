package service;

public class Managers {
    public TaskManager getDefault() {
        HistoryManager historyManager = new InMemoryHistoryManager();
        return new InMemoryTaskManager(historyManager);
    }
    static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}

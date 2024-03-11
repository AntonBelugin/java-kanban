package service;

public class Managers {
    public TaskManager getDefault() {
        return new InMemoryTaskManager(getDefaultHistory());
    }
    static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}

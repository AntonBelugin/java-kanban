package service;

import task.Task;
import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {

    private int sizeHistoryView = 10;

    public List<Task> historyView = new ArrayList<>(10);

    @Override
    public void addHistoryView(Task task) {
        if (historyView.size() < sizeHistoryView) {
            historyView.add(task);
        } else {
            historyView.remove(0);
            historyView.add(task);
        }
    }

    @Override
    public List<Task> getHistory() {
        return historyView;
    }

}

package service;

import task.Task;
import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {

    private int historySize = 10;
    public List<Task> historyView = new ArrayList<>(historySize);

    @Override
    public void addHistoryView(Task task) {
        if (historyView.size() < historySize) {
            historyView.add(task);
        } else {
            historyView.remove(0);
            historyView.add(task);
        }
    }

    @Override
    public List<Task> getHistoryList() {
        return historyView;
    }

}

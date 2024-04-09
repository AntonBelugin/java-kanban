package service;

import org.junit.jupiter.api.*;
import task.Epic;
import task.Subtask;
import task.Task;

class HistoryManagerTest {
     Task task;
     Task task2;
     Subtask subtask1;
     Subtask subtask2;
     Epic epic;
     Managers managers = new Managers();
     HistoryManager historyManager = new InMemoryHistoryManager();
     TaskManager taskManager = managers.getDefault();

    @BeforeEach
    void beforeEach() {
        epic = taskManager.makeEpic(new Epic("Переезд",
                "Переехать в другую квартиру"));

        subtask1 = taskManager.makeSubtask(new Subtask(1, "Собрать коробки",
                "собрать в коробки вещи", "12.12.2023 15:00", 30));

        subtask2 = taskManager.makeSubtask(new Subtask(1, "Собрать коробки2",
                "собрать в коробки вещи2", "12.12.2023 16:00", 25));

        task = taskManager.makeTask(new Task("Помыть посуду",
                "помыть посуду горячей водой", "12.12.2023 14:00", 40));

        task2 = taskManager.makeTask(new Task("Помыть посуду2",
                "помыть посуду горячей водой2",
                "13.12.2023 14:00", 40));
    }

    @AfterEach
    public void garbageCollections() {
        taskManager.clearTasks();
        taskManager.clearEpics();
        taskManager.clearSubtasks();
    }

    @Test
    void addHistoryView() {
        historyManager.addHistoryView(task);
        Assertions.assertEquals(task, historyManager.getHistoryList().get(0));
    }

    @Test
    void addHistoryViewDouble() {
        historyManager.addHistoryView(task);
        historyManager.addHistoryView(task);
        Assertions.assertEquals(1, historyManager.getHistoryList().size());
    }

    @Test
    void removeNode() {
        historyManager.addHistoryView(task);
        historyManager.addHistoryView(epic);
        historyManager.addHistoryView(subtask1);
        historyManager.addHistoryView(subtask2);
        historyManager.addHistoryView(task2);

        historyManager.removeNode(4);
        Assertions.assertFalse(historyManager.getHistoryList().contains(task));

        historyManager.removeNode(2);
        Assertions.assertFalse(historyManager.getHistoryList().contains(subtask1));

        historyManager.removeNode(5);
        Assertions.assertFalse(historyManager.getHistoryList().contains(task2));
    }

    @Test
    void getHistoryList() {
        historyManager.addHistoryView(task);
        historyManager.addHistoryView(epic);
        historyManager.addHistoryView(subtask1);
        historyManager.addHistoryView(subtask2);
        historyManager.addHistoryView(task2);

        Assertions.assertNotNull(historyManager.getHistoryList());
    }
}

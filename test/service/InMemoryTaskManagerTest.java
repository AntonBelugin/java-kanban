package service;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import task.Epic;
import task.Subtask;
import task.Task;

class InMemoryTaskManagerTest {
    Managers managers = new Managers();
    TaskManager taskManager = managers.getDefault();

    @Test
    void shouldInMemoryTaskManagerWork() {
        Task task = taskManager.makeTask(new Task( "Помыть посуду",
                "помыть посуду горячей водой"));
        Epic epic = taskManager.makeEpic(new Epic("Переезд",
                "Переехать в другую квартиру"));
        Subtask subtask = taskManager.makeSubtask(new Subtask(2,
                "Собрать коробки","собрать в коробки вещи"));

        Assertions.assertEquals(task, taskManager.getTaskById(1));
        Assertions.assertEquals(epic, taskManager.getEpicById(2));
        Assertions.assertEquals(subtask, taskManager.getSubtaskById(3));
    }

    @Test
    void shouldInMemoryHistoryManagerWork() {
        Task task = taskManager.makeTask(new Task( "Помыть посуду",
                "помыть посуду горячей водой"));
        taskManager.getTaskById(1);
        Assertions.assertEquals(task, taskManager.getHistory().get(0));
        taskManager.deleteTask(1);
        Assertions.assertEquals(0, taskManager.getHistory().size());
    }

    @Test
    void canNotChangeIdByTask() {
        Task task = taskManager.makeTask(new Task( "Помыть посуду",
                "помыть посуду горячей водой"));
        task.setId(2);
        Assertions.assertEquals(1, task.getId());
    }

    @Test
    void shouldDeleteSubtaskByEpic() {
        Epic epic = taskManager.makeEpic(new Epic("Переезд",
                "Переехать в другую квартиру"));
        Subtask subtask = taskManager.makeSubtask(new Subtask(1,
                "Собрать коробки","собрать в коробки вещи"));
        taskManager.deleteSubtask(2);
        Assertions.assertFalse(taskManager.getEpicById(1).epicSubtasks.contains(subtask));
    }

    }
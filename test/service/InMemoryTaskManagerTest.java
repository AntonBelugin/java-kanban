package service;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import task.Epic;
import task.Subtask;
import task.Task;
import task.TaskStatus;

class InMemoryTaskManagerTest {
    Managers managers = new Managers();
    TaskManager taskManager = managers.getDefault();

    @Test
    void shouldInMemoryTaskManagerWork() {
        Task task = taskManager.makeTask(new Task( "Помыть посуду",
                "помыть посуду горячей водой"));
        Epic epic = taskManager.makeEpic(new Epic("Переезд",
                "Переехать в другую квартиру"));
        Subtask subtask = taskManager.makeSubtask(new Subtask(taskManager.getEpicById(2),
                "Собрать коробки","собрать в коробки вещи"));

        Assertions.assertEquals(task, taskManager.getTaskById(1));
        Assertions.assertEquals(epic, taskManager.getEpicById(2));
        Assertions.assertEquals(subtask, taskManager.getSubtaskById(3));
    }
    }
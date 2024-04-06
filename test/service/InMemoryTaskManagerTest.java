package service;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import task.Epic;
import task.Subtask;
import task.Task;

class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {
    Managers managers = new Managers();
    TaskManager taskManager = managers.getDefault();

    @Override
    InMemoryTaskManager createManager() {
        Managers managers = new Managers();
        return new InMemoryTaskManager(managers.getDefaultHistory());
    }

    @Test
    void canNotChangeIdByTask() {
        Task task = taskManager.makeTask(new Task("Помыть посуду",
                "помыть посуду горячей водой", "12.12.2023 19:00", 40));
        task.setId(2);
        Assertions.assertEquals(1, task.getId());
    }

}
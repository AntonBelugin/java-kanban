package service;

import org.junit.jupiter.api.*;
import task.Epic;
import task.Subtask;
import task.Task;
import task.TaskStatus;

import java.time.Duration;

abstract class TaskManagerTest<T extends TaskManager> {
    protected T taskManager;
    protected Task task;
    protected Subtask subtask1;
    protected Subtask subtask2;
    protected Epic epic;

    abstract T createManager();

    @BeforeEach
    public void beforeEach() {
        taskManager = createManager();
        epic = taskManager.makeEpic(new Epic("Переезд",
                "Переехать в другую квартиру"));

        subtask1 = taskManager.makeSubtask(new Subtask(1, "Собрать коробки",
                "собрать в коробки вещи", "12.12.2023 15:00", 30));

        subtask2 = taskManager.makeSubtask(new Subtask(1, "Собрать коробки2",
                "собрать в коробки вещи2", "12.12.2023 16:00", 25));

        task = taskManager.makeTask(new Task("Помыть посуду",
                "помыть посуду горячей водой", "12.12.2023 14:00", 40));
    }

   @AfterEach
    public void garbageCollections() {
        taskManager.clearTasks();
        taskManager.clearEpics();
        taskManager.clearSubtasks();
    }

    @Test
    void makeTask() {
        Assertions.assertEquals(task, taskManager.getTaskById(4));
    }

    @Test
    void getTasks() {
        Assertions.assertEquals(task, taskManager.getTaskById(4));
    }

    @Test
    void clearTasks() {
        taskManager.clearTasks();
        Assertions.assertTrue(taskManager.getTasks().isEmpty());
    }

    @Test
    void getTaskById() {
        Assertions.assertEquals(task, taskManager.getTaskById(4));
    }

    @Test
    void updateTask() {
        task.setTaskStatus(TaskStatus.DONE);
        taskManager.updateTask(task);
        Assertions.assertEquals(TaskStatus.DONE, taskManager.getTaskById(4).getTaskStatus());
    }

    @Test
    void deleteTask() {
        taskManager.deleteTask(4);
        Assertions.assertNull(taskManager.getTaskById(4));
    }

    @Test
    void makeEpic() {
        Assertions.assertEquals(epic, taskManager.getEpicById(1));
    }

    @Test
    void getEpics() {
        Assertions.assertEquals(epic, taskManager.getEpics().get(1));
    }

    @Test
    void clearEpics() {
        taskManager.clearEpics();
        Assertions.assertTrue(taskManager.getEpics().isEmpty());
    }

    @Test
    void getEpicById() {
        Assertions.assertEquals(epic, taskManager.getEpicById(1));
    }

    @Test
    void updateEpic() {
        epic.setName("Новое имя");
        taskManager.updateEpic(epic);
        Assertions.assertEquals("Новое имя", epic.getName());
    }

    @Test
    void deleteEpic() {
        taskManager.deleteEpic(1);
        Assertions.assertNull(taskManager.getEpicById(1));
    }

    @Test
    void makeSubtask() {
        Assertions.assertEquals(subtask1, taskManager.getSubtaskById(2));
    }

    @Test
    void getSubtasks() {
        Assertions.assertEquals(subtask2, taskManager.getSubtasks().get(3));
    }

    @Test
    void clearSubtasks() {
        taskManager.clearSubtasks();
        Assertions.assertNull(taskManager.getSubtaskById(1));
    }

    @Test
    void getSubtaskById() {
        Assertions.assertEquals(subtask1, taskManager.getSubtaskById(2));
    }

    @Test
    void updateSubtask() {
        subtask1.setDuration(Duration.ofMinutes(45));
        taskManager.updateSubtask(subtask1);
        Assertions.assertEquals(45, subtask1.getDuration().toMinutes());
    }

    @Test
    void deleteSubtask() {
        taskManager.deleteSubtask(2);
        Assertions.assertNull(taskManager.getSubtaskById(2));
    }

    @Test
    void getSubtasksByEpic() {
        Assertions.assertEquals(subtask1, taskManager.getSubtasksByEpic(1).get(0));
    }

    @Test
    void getHistory() {
        Assertions.assertNotNull(taskManager.getHistory());
        taskManager.getTaskById(4);
        Assertions.assertEquals(taskManager.getTaskById(4), taskManager.getHistory().get(0));
    }

    @Test
    void getPrioritizedTasks() {
        Assertions.assertNotNull(taskManager.getPrioritizedTasks());
        Assertions.assertEquals(taskManager.getTaskById(4), taskManager.getPrioritizedTasks().first());
    }

    @Test
    void shouldEpicSetStatusWork() {

        taskManager.getSubtaskById(2).setTaskStatus(TaskStatus.NEW);
        taskManager.updateSubtask(taskManager.getSubtaskById(2));
        taskManager.getSubtaskById(3).setTaskStatus(TaskStatus.NEW);
        taskManager.updateSubtask(taskManager.getSubtaskById(3));
        Assertions.assertEquals(taskManager.getEpicById(1).getTaskStatus(),
                TaskStatus.NEW);

        taskManager.getSubtaskById(2).setTaskStatus(TaskStatus.DONE);
        taskManager.updateSubtask(taskManager.getSubtaskById(2));
        taskManager.getSubtaskById(3).setTaskStatus(TaskStatus.DONE);
        taskManager.updateSubtask(taskManager.getSubtaskById(3));
        Assertions.assertEquals(TaskStatus.DONE, taskManager.getEpicById(1).getTaskStatus());

        taskManager.getSubtaskById(2).setTaskStatus(TaskStatus.NEW);
        taskManager.updateSubtask(taskManager.getSubtaskById(2));
        taskManager.getSubtaskById(3).setTaskStatus(TaskStatus.DONE);
        taskManager.updateSubtask(taskManager.getSubtaskById(3));
        System.out.println();
        Assertions.assertEquals(TaskStatus.IN_PROGRESS, taskManager.getEpicById(1).getTaskStatus());

        taskManager.getSubtaskById(2).setTaskStatus(TaskStatus.IN_PROGRESS);
        taskManager.updateSubtask(taskManager.getSubtaskById(2));
        taskManager.getSubtaskById(3).setTaskStatus(TaskStatus.IN_PROGRESS);
        taskManager.updateSubtask(taskManager.getSubtaskById(3));
        Assertions.assertEquals(TaskStatus.IN_PROGRESS, taskManager.getEpicById(1).getTaskStatus());

        //Assertions.assertEquals(1, taskManager.getSubtaskById(2).getEpicId());
    }

    @Test
    void shouldSubtaskHaveEpic() {
        Assertions.assertEquals(1, taskManager.getSubtaskById(2).getEpicId());
    }

    @Test
    void shouldRunTimeTaskTest() {
        Assertions.assertNull(taskManager.makeSubtask(new Subtask(1, "Собрать коробки3",
                "собрать в коробки вещи3", "12.12.2023 16:00", 25)));
    }

    @Test
    void shouldHistoryEmpty() {
        Assertions.assertTrue(taskManager.getHistory().isEmpty());
    }

    @Test
    void shouldHistoryNotDouble() {
        taskManager.getTaskById(4);
        taskManager.getTaskById(4);
        Assertions.assertEquals(1, taskManager.getHistory().size());
    }

   @Test
    void shouldHistoryDelete() {
       taskManager.makeTask(new Task("Помыть посуду2",
                "помыть посуду горячей водой2",
                "13.12.2023 14:00", 40));
       taskManager.getEpicById(1);
       taskManager.getSubtaskById(2);
       taskManager.getSubtaskById(3);
       taskManager.getTaskById(4);
       taskManager.getTaskById(5);

       taskManager.deleteTask(5);
       Assertions.assertFalse(taskManager.getHistory().contains(taskManager.getTaskById(5)));

       taskManager.deleteSubtask(3);
       Assertions.assertFalse(taskManager.getHistory().contains(taskManager.getSubtaskById(3)));

       taskManager.deleteEpic(1);
       Assertions.assertFalse(taskManager.getHistory().contains(taskManager.getEpicById(1)));
    }
}

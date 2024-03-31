package service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import task.Epic;
import task.Subtask;
import task.TaskStatus;

import java.io.File;
import java.io.IOException;

class FileBackedTaskManagerTest {

    File tempFile;

    {
        try {
            tempFile = File.createTempFile("data", "csv");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    TaskManager fileBackedTaskManager = new FileBackedTaskManager(tempFile);

    @Test
    void shouldEpicSetStatusWork() {
        fileBackedTaskManager.makeEpic(new Epic("Переезд",
                "Переехать в другую квартиру"));

        fileBackedTaskManager.makeSubtask(new Subtask(1, "Собрать коробки",
                "собрать в коробки вещи", "12.12.2023 16:00", 30));

        fileBackedTaskManager.makeSubtask(new Subtask(1, "Собрать коробки2",
                "собрать в коробки вещи2", "12.12.2023 15:00", 25));

        fileBackedTaskManager.getSubtaskById(2).setTaskStatus(TaskStatus.NEW);
        fileBackedTaskManager.updateSubtask(fileBackedTaskManager.getSubtaskById(2));
        fileBackedTaskManager.getSubtaskById(3).setTaskStatus(TaskStatus.NEW);
        fileBackedTaskManager.updateSubtask(fileBackedTaskManager.getSubtaskById(3));
        Assertions.assertEquals(fileBackedTaskManager.getEpicById(1).getTaskStatus(),
                TaskStatus.NEW);

        fileBackedTaskManager.getSubtaskById(2).setTaskStatus(TaskStatus.DONE);
        fileBackedTaskManager.updateSubtask(fileBackedTaskManager.getSubtaskById(2));
        fileBackedTaskManager.getSubtaskById(3).setTaskStatus(TaskStatus.DONE);
        fileBackedTaskManager.updateSubtask(fileBackedTaskManager.getSubtaskById(3));
        Assertions.assertEquals(TaskStatus.DONE, fileBackedTaskManager.getEpicById(1).getTaskStatus());

        fileBackedTaskManager.getSubtaskById(2).setTaskStatus(TaskStatus.NEW);
        fileBackedTaskManager.updateSubtask(fileBackedTaskManager.getSubtaskById(2));
        fileBackedTaskManager.getSubtaskById(3).setTaskStatus(TaskStatus.DONE);
        fileBackedTaskManager.updateSubtask(fileBackedTaskManager.getSubtaskById(3));
        System.out.println();
        Assertions.assertEquals(TaskStatus.IN_PROGRESS, fileBackedTaskManager.getEpicById(1).getTaskStatus());

        fileBackedTaskManager.getSubtaskById(2).setTaskStatus(TaskStatus.IN_PROGRESS);
        fileBackedTaskManager.updateSubtask(fileBackedTaskManager.getSubtaskById(2));
        fileBackedTaskManager.getSubtaskById(3).setTaskStatus(TaskStatus.IN_PROGRESS);
        fileBackedTaskManager.updateSubtask(fileBackedTaskManager.getSubtaskById(3));
        Assertions.assertEquals(TaskStatus.IN_PROGRESS, fileBackedTaskManager.getEpicById(1).getTaskStatus());

        Assertions.assertEquals(1, fileBackedTaskManager.getSubtaskById(2).getEpicId());
    }

    @Test
    void shouldSubtaskHaveEpic() {
        fileBackedTaskManager.makeEpic(new Epic("Переезд",
                "Переехать в другую квартиру"));

        fileBackedTaskManager.makeSubtask(new Subtask(1, "Собрать коробки",
                "собрать в коробки вещи", "13.12.2023 16:00", 30));

        fileBackedTaskManager.makeSubtask(new Subtask(1, "Собрать коробки2",
                "собрать в коробки вещи2", "13.12.2023 15:00", 25));

        Assertions.assertEquals(1, fileBackedTaskManager.getSubtaskById(2).getEpicId());
    }

    @Test
    void shouldRunTimeTaskTest() {
        fileBackedTaskManager.makeEpic(new Epic("Переезд",
                "Переехать в другую квартиру"));

        fileBackedTaskManager.makeSubtask(new Subtask(1, "Собрать коробки",
                "собрать в коробки вещи", "14.12.2023 16:00", 30));

        Assertions.assertNull(fileBackedTaskManager.makeSubtask(new Subtask(1, "Собрать коробки2",
                "собрать в коробки вещи2", "14.12.2023 16:00", 25)));
    }
}
